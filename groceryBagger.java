import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class groceryBagger {

	private static ArrayList<String> groceries = new ArrayList<String>();
	private static File groceryProblem;
	private static int maxBags;
	private static int bagSize;
	private static Scanner scan;
	private static Scanner tempScan = new Scanner(System.in);
	private static String tempInput;
	
	public static void main(String[] args) {
		
		//Command Line Argument Handling.
		if(args.length < 1) { //Not enough arguments
			System.out.println("Usage: groceryBagger <fileName>");
			System.exit(1);
		}
		if(args.length > 1) { //Too many arguments
			boolean ignore = false;
			int attempts = -1;
			
			while(!ignore) {
				attempts++;
				respond(attempts);
				ignore = checkInput();
			}
		}
		
		//Parse file and get grocery info.
		groceryProblem = new File(args[0]);
		try {
			//Initialize Scanner.
			scan = new Scanner(groceryProblem);
			
			//Get data from scanner and close it.
			//maxBags = Integer.parseInt(scan.nextLine());
			//bagSize = Integer.parseInt(scan.nextLine());
			while(scan.hasNext()) {
				groceries.add(scan.nextLine());
			}
			scan.close();
			
			//Print data to verify how it is given.
			for(String thing : groceries){
				System.out.println(thing);
			}
			
			
			
		} catch(NumberFormatException e) {
			System.out.println("The first and second line of the input file must be integers.");
			System.exit(1);
		} catch(FileNotFoundException e) {
			System.out.println("The file \"" + args[0] + "\" could not be found.");
			System.exit(1);
		}catch (Exception e) {
			System.out.println("An exception occured that was not accounted for.");
			e.printStackTrace();
		}

	}

	/**
	 * Get and check the input of the user in respond to them giving too many arguments to main.
	 * 
	 * @return true if the user enters y/yes, false if invalid input, and close if users enters n/no
	 */
	private static boolean checkInput() {
		
		tempInput = tempScan.next();
		
		if(tempInput.toLowerCase().equals("y") | tempInput.toLowerCase().equals("yes")) {
			System.out.println("Resuming program.");
			tempScan.close();
			return true;
		} else if(tempInput.toLowerCase().equals("n") | tempInput.toLowerCase().equals("no")) {
			System.out.println("Reminder! Usage: groceryBagger <fileName>\nExiting program.");
			tempScan.close();
			System.exit(0);
		} 
		return false; //If input was not a yes or no variation.
	}
	
	/**
	 * Respond to the user based on how many times they have attempted to deal with giving
	 *  main too many arguments.
	 * 
	 * @param attempts The number of times we have prompted the user about how to deal with
	 * giving main too many arguments
	 */
	private static void respond(int attempts) {
		
		if(attempts == 0) {
			System.out.println("Warning! This program only takes 1 argument. Would " 
							+ "you like to continue anyway? (Y/N)\n");
		} else if(attempts == 1) {
			System.out.println("Reminder! If you would like to continue, type Y and press enter, or "
					+ "if you would like to exit, type N and press enter.\n");
		} else if(attempts == 2){
			System.out.println("One more time, type and enter either Y or N to continue or exit.\n");
		} else if(attempts > 2){
			System.out.println("READ!\n");
		}
		
	}

}
