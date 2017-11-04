import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class groceryBagger {
	
	public static void main(String[] args) {
		
		//Command Line Argument Handling.
		if(args.length < 1) { //Not enough arguments
			System.out.println("Usage: groceryBagger <fileName>");
			System.exit(1);
		}
		
		boolean arcConsistency = true;
		boolean localSearch = false;
		if(args.length > 1) {
			if(args[1].equals("-slow")) {
				arcConsistency = false;
			}
			else if(args[1].equals("-local")) {
				localSearch = true;
			}
			if(args.length > 2) {
				if(args[2].equals("-slow")) {
					arcConsistency = false;
				}
				else if(args[2].equals("-local")) {
					localSearch = true;
				}
			}
		}
		
		//Parse file and get grocery info.
		File groceryProblem = new File(args[0]);
		try {
			int maxBags;
			int bagSize;
			ArrayList<GroceryItem> groceries = new ArrayList<>();
			//Initialize Scanner.
			Scanner scan = new Scanner(groceryProblem);
			
			//Get data from scanner and close it.
			maxBags = Integer.parseInt(scan.nextLine());
			bagSize = Integer.parseInt(scan.nextLine());
			System.out.println("Max bags: " + maxBags + "\nMax Capacity per Bag: " + bagSize);
			int totalItems = 1;
			while(scan.hasNextLine()) {
				ArrayList<String> constraints = new ArrayList<>();
				String line = scan.nextLine();
				Scanner lineScan = new Scanner(line);

				String itemName = lineScan.next();

				int weight = lineScan.nextInt();
				boolean plusConstraint = false;
				if (lineScan.hasNext()){
					if (lineScan.next().equals("+")) {
						plusConstraint = true;
					}
					while (lineScan.hasNext()){
						constraints.add(lineScan.next());
					}
				}
				groceries.add(new GroceryItem(itemName, plusConstraint, weight, constraints, totalItems++));

				lineScan.close();
			}
			scan.close();

			for(GroceryItem item: groceries) {
				item.setConstraintBits(totalItems - 1);
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


}
