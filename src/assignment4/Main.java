package assignment4;
/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Fall 2016
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    private static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console

    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
        if (args.length != 0) {
            try {
                inputFile = args[0];
                kb = new Scanner(new File(inputFile));			
            } catch (FileNotFoundException e) {
                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
            }
            if (args.length >= 2) {
                if (args[1].equals("test")) { // if the word "test" is the second argument to java
                    // Create a stream to hold the output
                    testOutputString = new ByteArrayOutputStream();
                    PrintStream ps = new PrintStream(testOutputString);
                    // Save the old System.out.
                    old = System.out;
                    // Tell Java to use the special stream; all console output will be redirected here from now
                    System.setOut(ps);
                }
            }
        } else { // if no arguments to main
            kb = new Scanner(System.in); // use keyboard and console
        }
        /* Do not alter the code above for your submission. */
        /* Write your code below. */
        Boolean quitRecieved = false;
        while(quitRecieved == false) {
        	System.out.print("critters>");
        	List<String> input = new ArrayList<String>();
        	input = parse(kb);
        	if(input.get(0).equals("quit")) {
        		try {
        			if(input.size() > 1)
    				{
    					throw new ClassNotFoundException();
    				}
            		quitRecieved = true;
        		}
        		catch(ClassNotFoundException c){
        			System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
					System.out.println("");
        		}
        	}
        	else if(input.get(0).equals("show")) {
        		try {
        			if(input.size() > 1)
    				{
    					throw new ClassNotFoundException();
    				}
            		Critter.displayWorld();
        		}
        		catch (ClassNotFoundException c) {
        			System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
					System.out.println("");
        		}
        	}
        	else if(input.get(0).equals("step")){
        		try {
        			if(input.size() > 2)
    				{
    					throw new ClassNotFoundException();
    				}
    				if(input.size() == 1) {
    					Critter.worldTimeStep();
    				}
    				else {
    					for(int i = 0; i < Integer.parseInt(input.get(1)); i++) {
    						Critter.worldTimeStep();
    					}
    				}
        		}
        		catch (ClassNotFoundException c) {
        			System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
					System.out.println("");
        		}
			}
        	else if(input.get(0).equals("seed")){
        		try {
        			if(input.size() > 2)
					{
						throw new NumberFormatException();
					}
        			int seedNum = Integer.parseInt(input.get(1));
    				Critter.setSeed(seedNum);
        		}
        		catch (NumberFormatException n) {
        			System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
					System.out.println("");
				}
   			}
        	else if(input.get(0).equals("make")) {
        		try {
        			if(input.size() > 3)
					{
						throw new NumberFormatException();
					}
        			else if(input.size() == 2) {
    					Critter.makeCritter(myPackage+"."+input.get(1));
    				}
    				else {
    					for(int i = 0; i < Integer.parseInt(input.get(2)); i++) {
        					Critter.makeCritter(myPackage+"."+input.get(1));
    					}
    				}

          		} 
        	    catch (InvalidCritterException e) {
        	    	System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
					System.out.println("");
        		}
        		catch (NumberFormatException n) {
        			System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
					System.out.println("");
				}
           	}
        	else if(input.get(0).equals("stats")){
				try {
					if(input.size() > 2)
					{
						throw new ClassNotFoundException();
					}
					List<Critter> instances = Critter.getInstances(myPackage+"."+input.get(1));
					Class c = Class.forName(myPackage+"."+input.get(1));
					//instances.get(0).runStats(instances);
					Class[] types = {List.class};
					Method method = c.getMethod("runStats", types);
					method.invoke(c, instances);
				} 
		        catch (InvalidCritterException e) {
					System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
				}
				catch(ClassNotFoundException e) {
					System.out.print("error processing: ");
					for(int i = 0; i < input.size(); i++) {
	        			System.out.print(input.get(i)+" ");
	        		}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
				} catch (SecurityException e) {
					e.printStackTrace();
				}
			}
        	else {
        		System.out.print("invalid command: ");
        		for(int i = 0; i < input.size(); i++) {
        			System.out.print(input.get(i)+" ");
        		}
        		System.out.println("");
        	}
        }
        /* Write your code above */
        System.out.flush();

    }
    
    public static List<String> parse(Scanner keyboard) {
		List<String> input = new ArrayList<String>();
		String token = keyboard.nextLine();
		input = Arrays.asList(token.split(" "));
		return input;
	}

}
