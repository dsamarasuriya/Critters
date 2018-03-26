package assignment4;

import java.util.*;
import java.util.function.Predicate;

import javax.naming.directory.DirContext;

/**
 * This critter becomes stronger the more of its kind are on the board. Once 7 or more Critter1s are on the board, it enables all Critter1s to move in 
 * any of the 8 possible directions. In addition, if the critter has enough energy to reproduce during a fight scenario, it will reproduce and create 
 * a new critter, while also returning false (forfeiting the fight).
 * @author Dilan
 *
 */
public class Critter1 extends Critter {
	
	private static int strengthInNumbers = 0;
	private int dir;
	
	public Critter1() {
		strengthInNumbers = strengthInNumbers + 1;
		dir = 0;
	}

	/**
	 * doTimeStep: Moves critter in 0-7 (depending on number of Critter1s on the board) direction with a 1/5 chance of reproducing 
	 */
	@Override
	public void doTimeStep() {
		if(strengthInNumbers >= 7) {
			dir = getRandomInt(8);
			run(dir);
		}
		else {
			dir = getRandomInt(strengthInNumbers);
			walk(dir);
		}	
		if (getRandomInt(5) == 3 && getEnergy() > Params.min_reproduce_energy) {
			Critter1 child = new Critter1();
			child.dir = 0;
			reproduce(child, 0);
		}
	}

	/**
	 * fight determines what Critter1 does in an encounter (Being either to reproduce or fight)
	 * @param opponent is the string representation of the opponent
	 * @return false and create a new critter if energy is greater than 70, otherwise returns true 
	 */
	@Override
	public boolean fight(String opponent) {
		if (getEnergy() > 70) {
			Critter1 child = new Critter1();
			child.dir = 0;
			reproduce(child, 0);
			return false;
		}
		return true;
	}
	
	
	
	public String toString() {
		return "1";
	}
	
	public static void runStats(java.util.List<Critter> crit1) {
		int numofCrit1s = 0;
		System.out.println("" + crit1.size() + " total Critter1s in the horde.");
		for (Object obj : crit1) {
			numofCrit1s = numofCrit1s +1;
			Critter1 c = (Critter1) obj;
			System.out.println("Critter1 #" + numofCrit1s + ": Energy left - " + c.getEnergy());
		}
		System.out.println();
	}
}
