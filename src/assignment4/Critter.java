package assignment4;
/* CRITTERS Critter.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 *	Dilan Samarasuriya
 * 	DTS777
 * 
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */

import java.nio.channels.Pipe;
import java.util.List;

//test sample
/* see the PDF for descriptions of the methods and fields in this class
 * you may add fields, methods or inner classes to Critter ONLY if you make your additions private
 * no new public, protected or default-package code or data can be added to Critter
 */


public abstract class Critter {
	private static String myPackage;
	private	static List<Critter> population = new java.util.ArrayList<Critter>();
	private static List<Critter> babies = new java.util.ArrayList<Critter>();

	// Gets the package name.  This assumes that Critter and its subclasses are all in the same package.
	static {
		myPackage = Critter.class.getPackage().toString().split(" ")[1];
	}
	
	private static java.util.Random rand = new java.util.Random();
	public static int getRandomInt(int max) {
		return rand.nextInt(max);
	}
	
	public static void setSeed(long new_seed) {
		rand = new java.util.Random(new_seed);
	}
	
	
	/* a one-character long string that visually depicts your critter in the ASCII interface */
	public String toString() { return "x"; }
	
	private int energy = 0;
	protected int getEnergy() { return energy; }
	
	private int x_coord;
	private int y_coord;
	private boolean hasMoved = false;
	private boolean fightOccuring = true;
	
	/**
	 * Simple method that sets hasMoved to true 
	 */
	private void critterHasMoved() {
		hasMoved = true;
	}
	
	/**
	 * walk: Moves critter 1 unit in specified direction and deducts energy for doing so.
	 * @param direction is an int from 0-7 representing which direction the critter will move 
	 */
	protected final void walk(int direction) {
		if(hasMoved == false){ //Will not move critter if it has already been moved during the current world time step 
			if(anotherCritterInSpot(direction, 1) == false && fightOccuring == true) {
				setXAndY(direction, 1);
				critterHasMoved(); //Sets hasMoved to true 
			}
			else if(fightOccuring == false) {
				setXAndY(direction, 1);
				critterHasMoved(); //Sets hasMoved to true 
			}
		}
		energy = energy - Params.walk_energy_cost; //Deducts energy regardless of whether any movement occured 
	}
	
	/**
	 * run: Moves critter 1 unit in specified direction and deducts energy for doing so.
	 * @param direction is an int from 0-7 representing which direction the critter will move 
	 */
	protected final void run(int direction) {
		if(hasMoved == false){ //Same comments as walk()
			if(anotherCritterInSpot(direction, 2) == false && fightOccuring == true) {
				setXAndY(direction, 2);
				critterHasMoved(); //Sets hasMoved to true 
			}
			else if(fightOccuring == false) {
				setXAndY(direction, 2);
				critterHasMoved(); //Sets hasMoved to true 
			}
		}
		energy = energy - Params.run_energy_cost;
	}
	
	/**
	 * Sets the x and y coordinate based on the passed direction. Also has checks to make sure the new x and y coordinates 
	 * don't go out of the set world bounds 
	 * @param direction is an int from 0-7 representing which direction the critter will move 
	 * @param runOrWalk dictates how far in the given direction the critter will move
	 */
	private void setXAndY(int direction, int runOrWalk){
		switch(direction) {
		case 0:	if((x_coord + runOrWalk) < Params.world_width && (x_coord + runOrWalk) >= 0) {
					x_coord = x_coord + runOrWalk;
				}
				break;
		case 1:	if(x_coord + runOrWalk < Params.world_width && x_coord + runOrWalk >= 0) {
					x_coord = x_coord + runOrWalk;
				}
				if(y_coord + runOrWalk < Params.world_height && y_coord + runOrWalk >= 0) {
					y_coord = y_coord + runOrWalk;
				}
				break;
		case 2:	if(y_coord + runOrWalk < Params.world_height && y_coord + runOrWalk >= 0) {
					y_coord = y_coord + runOrWalk;
				}
				break;
		case 3:	if(x_coord - runOrWalk < Params.world_width && x_coord - runOrWalk >= 0) {
					x_coord = x_coord - runOrWalk;
				}
				if(y_coord + runOrWalk < Params.world_height && y_coord + runOrWalk >= 0) {
					y_coord = y_coord + runOrWalk;
				}
				break;
		case 4:	if(x_coord - runOrWalk < Params.world_width && x_coord - runOrWalk >= 0) {
					x_coord = x_coord - runOrWalk;
				}
				break;
		case 5:	if(x_coord - runOrWalk < Params.world_width && x_coord - runOrWalk >= 0) {
					x_coord = x_coord - runOrWalk;
				}
				if(y_coord - runOrWalk < Params.world_height && y_coord - runOrWalk >= 0) {
					y_coord = y_coord - runOrWalk;
				}
				break;
		case 6:	if(y_coord - runOrWalk < Params.world_height && y_coord - runOrWalk >= 0) {
					y_coord = y_coord - runOrWalk;
				}
				break;
		case 7:	if(x_coord + runOrWalk < Params.world_width && x_coord + runOrWalk >= 0) {
					x_coord = x_coord + runOrWalk;
				}
				if(y_coord - runOrWalk < Params.world_height && y_coord - runOrWalk >= 0) {
					y_coord = y_coord - runOrWalk;
				}
				break;
		}
	}
	
	/**
	 * reproduce: if the critter is not dead and has enough energy to reproduce, creates a new child, giving half it's energy
	 * @param offspring
	 * @param direction
	 */
	protected final void reproduce(Critter offspring, int direction) {
		if(energy >= Params.min_reproduce_energy && energy != 0) { 
			offspring.energy = energy/2;
			energy = (int)Math.ceil(energy/2);
			offspring.x_coord = x_coord;
			offspring.y_coord = y_coord;
			offspring.walk(direction);
			babies.add(offspring);
		}
	}

	public abstract void doTimeStep();
	public abstract boolean fight(String oponent);
	
	/**
	 * create and initialize a Critter subclass.
	 * critter_class_name must be the unqualified name of a concrete subclass of Critter, if not,
	 * an InvalidCritterException must be thrown.
	 * (Java weirdness: Exception throwing does not work properly if the parameter has lower-case instead of
	 * upper. For example, if craig is supplied instead of Craig, an error is thrown instead of
	 * an Exception.)
	 * @param critter_class_name
	 * @throws InvalidCritterException
	 */
	public static void makeCritter(String critter_class_name) throws InvalidCritterException {
		try {
			Class c = Class.forName(critter_class_name);
			Critter v = (Critter) c.newInstance();
			v.x_coord = getRandomInt(Params.world_width);
			v.y_coord = getRandomInt(Params.world_height);
			v.energy = Params.start_energy;
			population.add(v);
		}
		catch(ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (IllegalAccessException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		catch (InstantiationException e) {
			throw new InvalidCritterException(critter_class_name);
		}
	}
	
	/**
	 * Gets a list of critters of a specific type.
	 * @param critter_class_name What kind of Critter is to be listed.  Unqualified class name.
	 * @return List of Critters.
	 * @throws InvalidCritterException
	 */
	public static List<Critter> getInstances(String critter_class_name) throws InvalidCritterException {
		List<Critter> result = new java.util.ArrayList<Critter>();
		try {
			Class c = Class.forName(critter_class_name);
			for(int i = 0; i < population.size(); i++) {
				if(c == population.get(i).getClass()) {
					result.add(population.get(i));
				}
			}
		}
		catch(ClassNotFoundException e) {
			throw new InvalidCritterException(critter_class_name);
		}
		return result;
	}
	
	/**
	 * Prints out how many Critters of each type there are on the board.
	 * @param critters List of Critters.
	 */
	public static void runStats(List<Critter> critters) {
		System.out.print("" + critters.size() + " critters as follows -- ");
		java.util.Map<String, Integer> critter_count = new java.util.HashMap<String, Integer>();
		for (Critter crit : critters) {
			String crit_string = crit.toString();
			Integer old_count = critter_count.get(crit_string);
			if (old_count == null) {
				critter_count.put(crit_string,  1);
			} else {
				critter_count.put(crit_string, old_count.intValue() + 1);
			}
		}
		String prefix = "";
		for (String s : critter_count.keySet()) {
			System.out.print(prefix + s + ":" + critter_count.get(s));
			prefix = ", ";
		}
		System.out.println();		
	}
	
	/**
	 * sameSpot: Checks to see if given critter is in the same spot as the current critter 
	 * @param c is a critter subclass object (Craig, Critter1, etc)
	 * @return
	 */
	private boolean sameSpot(Critter c) {
		if(x_coord == c.x_coord && y_coord == c.y_coord) {
			return true;
		}
		return false;
	}
	/* the TestCritter class allows some critters to "cheat". If you want to 
	 * create tests of your Critter model, you can create subclasses of this class
	 * and then use the setter functions contained here. 
	 * 
	 * NOTE: you must make sure that the setter functions work with your implementation
	 * of Critter. That means, if you're recording the positions of your critters
	 * using some sort of external grid or some other data structure in addition
	 * to the x_coord and y_coord functions, then you MUST update these setter functions
	 * so that they correctly update your grid/data structure.
	 */
	static abstract class TestCritter extends Critter {
		protected void setEnergy(int new_energy_value) {
			super.energy = new_energy_value;
		}
		
		protected void setX_coord(int new_x_coord) {
			super.x_coord = new_x_coord;
		}
		
		protected void setY_coord(int new_y_coord) {
			super.y_coord = new_y_coord;
		}
		
		protected int getX_coord() {
			return super.x_coord;
		}
		
		protected int getY_coord() {
			return super.y_coord;
		}
		

		/*
		 * This method getPopulation has to be modified by you if you are not using the population
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.
		 */
		protected static List<Critter> getPopulation() {
			return population;
		}
		
		/*
		 * This method getBabies has to be modified by you if you are not using the babies
		 * ArrayList that has been provided in the starter code.  In any case, it has to be
		 * implemented for grading tests to work.  Babies should be added to the general population 
		 * at either the beginning OR the end of every timestep.
		 */
		protected static List<Critter> getBabies() {
			return babies;
		}
	}

	/**
	 * Clear the world of all critters, dead and alive
	 */
	public static void clearWorld() {
		population.clear();
		babies.clear();
		// Complete this method.
	}
	
	/**
	 * worldTimeStep: Simulates a time step in the model environment, changing elements of the world as a result 
	 */
	public static void worldTimeStep() {
		//Move all critters
		for(int i = 0; i < population.size(); i++) {
			population.get(i).doTimeStep();
		}
		//Check encounters 
		//displayWorld();
		
		for(int i = 0; i < population.size(); i++) {
			for(int j = 0; j < population.size(); j++) {
				if(i != j && population.get(i).getClass() == Algae.class && population.get(j).energy > 0) {
					population.get(j).energy = population.get(j).getEnergy() + population.get(i).getEnergy();
					population.get(i).energy = 0;
				}
				else if(i != j && population.get(j).getClass() == Algae.class && population.get(i).energy > 0) {
					population.get(i).energy = population.get(i).getEnergy() + population.get(j).getEnergy();
					population.get(j).energy = 0;
				} //Two previous If statements check to see if either object is Algae; will kill algae and add it's energy to other critter in the same spot 
				else if(i != j && population.get(i).sameSpot(population.get(j))) {
					population.get(i).fightOccuring = true;
					population.get(j).fightOccuring = true;
					Boolean critter1 = population.get(i).fight(population.get(j).toString());
					Boolean critter2 = population.get(j).fight(population.get(i).toString());
					//Add in fight clause 
					if((critter1 == true || critter2 == true) && population.get(i).sameSpot(population.get(j)) == true && population.get(i).energy > 0 && population.get(j).energy > 0) {
						int critter1Roll = 0;
						int critter2Roll = 0;
						if(critter1 == true && critter2 == false) { //If Either critter elected to fight, simulates the fight if they are both still in the same spot
							critter1Roll = getRandomInt(population.get(i).energy);
							if(critter1Roll > critter2Roll) {
								population.get(i).energy = population.get(i).energy + (population.get(j).energy)/2;
								population.get(j).energy = 0;
							}
							else {
								population.get(j).energy = population.get(j).energy + (population.get(i).energy)/2;
								population.get(i).energy = 0;
							}
						}
						else if(critter2 == true && critter1 == false){
							critter2Roll = getRandomInt(population.get(j).energy);
							if(critter2Roll > critter1Roll) {
								population.get(j).energy = population.get(j).energy + (population.get(i).energy)/2;
								population.get(i).energy = 0;
							}
							else {
								population.get(i).energy = population.get(i).energy + (population.get(j).energy)/2;
								population.get(j).energy = 0;
							}
						}
						else {
							critter1Roll = getRandomInt(population.get(i).energy);
							critter2Roll = getRandomInt(population.get(j).energy);
							while(critter1Roll == critter2Roll) {
								critter1Roll = getRandomInt(population.get(i).energy);
								critter2Roll = getRandomInt(population.get(j).energy);
							}
							if (critter2Roll > critter1Roll) {
								population.get(j).energy = population.get(j).energy + (population.get(i).energy)/2;
								population.get(i).energy = 0;
							}
							else {
								population.get(i).energy = population.get(i).energy + (population.get(j).energy)/2;
								population.get(j).energy = 0;
							}
						}
					}
					if(critter1 == false && critter2 == false && population.get(i).sameSpot(population.get(j))) {
						population.get(i).energy = 0;
						population.get(j).energy = 0;
					}
					population.get(i).fightOccuring = false;
					population.get(j).fightOccuring = false;
				}
			}
		}
		//Add new critters to population 
		for(int i = 0; i < babies.size(); i++) {
			population.add(babies.get(i));
		}
		babies.clear();
		//Remove dead critters
		for(int i = 0; i < population.size(); i++) {
			if(population.get(i).hasMoved == true) {
				population.get(i).hasMoved = false;
			}
			if(population.get(i).energy <= 0) {
				population.remove(i);
			}
		}
		//Refresh Algae in world
		for(int i = 0; i< Params.refresh_algae_count; i++) {
			try {
				Critter.makeCritter(myPackage+"."+"Algae");
			} catch (InvalidCritterException e) {
    			System.out.println("Invalid class name");
			}
		}
		// Complete this method.
	}
	
		
	public static void displayWorld() {
		String[][] world = new String[Params.world_height][Params.world_width];
		String topBottomBorder = new String();
		topBottomBorder = "";
		topBottomBorder = topBottomBorder + "+";
		for(int i = 0; i < Params.world_width; i++) {
			topBottomBorder = topBottomBorder + "-";
		}
		topBottomBorder = topBottomBorder + "+";
		for(int i = 0; i < population.size(); i++) {
			world[population.get(i).y_coord][population.get(i).x_coord] = population.get(i).toString();
		}
		System.out.println(topBottomBorder);
		for(int i = 0; i < Params.world_height; i++) {
			System.out.print("|");
			for(int j = 0; j < Params.world_width; j++) {
				if(world[i][j] == null) {
					System.out.print(" ");
				}
				else {
					System.out.print(world[i][j]);
				}
			}
			System.out.println("|");
		}
		System.out.println(topBottomBorder);
		// Complete this method.
	}
	
	/**
	 * anotherCritterInSpot: Checks to see if another critter is occupying the spot the critter plans to move to (For fight scenarios)
	 * @param dir is the direction 0-7 which the critter plans to move
	 * @return true if a critter is in that position, otherwise returns false 
	 */
	private boolean anotherCritterInSpot(int dir, int runOrWalk) {
		int potentialXCoord = 0;
		int potentialYCoord = 0;
		switch(dir) {
			case 0: potentialXCoord = x_coord + runOrWalk;
					break;
			case 1: potentialXCoord = x_coord + runOrWalk;
					potentialYCoord = y_coord + runOrWalk;
					break;
			case 2: potentialYCoord = y_coord + runOrWalk;
					break;
			case 3: potentialYCoord = y_coord + runOrWalk;
					potentialXCoord = x_coord - runOrWalk;
					break;
			case 4: potentialXCoord = x_coord - runOrWalk;
					break;
			case 5: potentialXCoord = x_coord - runOrWalk;
					potentialYCoord = y_coord - runOrWalk;
			case 6: potentialYCoord = y_coord - runOrWalk;
					break;
			case 7: potentialYCoord = y_coord - runOrWalk;
					potentialXCoord = x_coord + runOrWalk;
					break;
		}
		for(int i = 0; i < population.size(); i++) {
			if(population.get(i).x_coord == potentialXCoord && population.get(i).y_coord == potentialYCoord && population.get(i) != this) {
				//population.get(i) != this checks to see if they are the SAME critter (i.e. the same object occupying the same space in memory), not if they are equivalent critters 
				return true;
			}
		}
		return false;
	}

}
