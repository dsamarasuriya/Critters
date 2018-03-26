package assignment4;

/**
 * The Class for the second critter I created for this project.
 * The main thing that differentiates this critter from Critter1 is that it can only move if it meets a certain energy threshold. By extension, when 
 * the critter is in a fight, it will attempt to run away (or walk), only taking the fight when it has low energy. Basically, this critter fights for energy only when it needs it,
 * and likes to stay in one spot if possible. 
 * @author Dilan
 *
 */
public class Critter2 extends Critter{
	private int dir;
	private int fightsEscaped;
	
	public Critter2() {
		dir = getRandomInt(8);
		fightsEscaped = 0;
	}

	@Override
	public void doTimeStep() {
		dir = getRandomInt(8);
		if(getEnergy() > 95) {
			run(dir);
		}
	}

	@Override
	public boolean fight(String opponent) {
		int checkedDirections;
		dir = 0;
		if(getEnergy() > 50) { //If more than 50 energy, will attempt to run 
			checkedDirections = 0;
			run(dir);
			fightsEscaped = fightsEscaped +1;
			return false;
		}
		else if (getEnergy() > 20){ //if more than 20 energy, will attempt to walk
			walk(dir);
			fightsEscaped = fightsEscaped +1;
			return false;
		}
		return true; //Will only fight if critter has less than 20 energy
	}
	
	
	
	public String toString() {
		return "2";
	}
	
	public static void runStats(java.util.List<Critter> crit2) {
		int numofCrit2s = 0;
		System.out.println("" + crit2.size() + " total Critter2s.");
		for (Object obj : crit2) {
			numofCrit2s = numofCrit2s +1;
			Critter2 c = (Critter2) obj;
			System.out.println("Critter1 #" + numofCrit2s + ": Energy left - " + c.getEnergy() + "; Deaths defied - " + c.fightsEscaped);
			//Prints out energy and deaths defied of each critter2 - 'deaths defied' being the number of fights the critter managed the escape 
		}
		System.out.println();
	}
	
	
}
