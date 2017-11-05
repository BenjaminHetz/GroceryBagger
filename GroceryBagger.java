import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GroceryBagger {
	
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
			int idCounter = 0;
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
				groceries.add(new GroceryItem(itemName, plusConstraint, weight, constraints, idCounter++));

				lineScan.close();
			}
			scan.close();

			//Set bits now that we have each item.
			for(GroceryItem GI: groceries) {
				GI.setConstraintBits(idCounter, groceries);
			}
			
			// Print items to check proper creation.
//			for(GroceryItem GI: groceries) {
//				System.out.println();
//				System.out.println(GI.toString());
//			}
			
			//Initialize Bags
			int totalItems = idCounter;
			idCounter = 0;
			ArrayList<GroceryBag> bags = new ArrayList<>();
			for(int i = 0; i < maxBags; i++) {
				bags.add(new GroceryBag(bagSize, idCounter++, totalItems));
			}
			
			// Check bags for proper creation.
			for(GroceryBag bag: bags) {
				System.out.println(bag.toString());
			}
			
			ArrayList<GroceryItem> currList = MRV(groceries, bags, maxBags);
			GroceryItem currItem;
			if(currList.size() == 1) {
				currItem = currList.get(0);
			} else {
				currItem = LCV(currList);
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

	//Minimum Remaining Values...Minimum bags available.
	private static ArrayList<GroceryItem> MRV(ArrayList<GroceryItem> groceries, ArrayList<GroceryBag> bags, int maxBags) {
		int minValue = 10000;
		ArrayList<GroceryItem> minItems = new ArrayList<>();
		for(GroceryItem GI: groceries) {
			int currValue = 0;
			for(int i = 0; i < maxBags; i++) {
				if(bags.get(i).empty()) {
					currValue += maxBags - i;
				} else {
					if(bags.get(i).getConstraintBits().get(GI.getID())) {
						currValue++;
					}
				}

			}
			if(currValue < minValue) {
				minValue = currValue;
				minItems.removeAll(minItems);
				minItems.add(GI);
			}
			else if(currValue == minValue) {
				minItems.add(GI);
			}
		}
		return minItems;
	}
	
	//Least Constraining Value...most 1s in BitSet
	private static GroceryItem LCV(ArrayList<GroceryItem> groceries) {
		//Find LCVs
		ArrayList<GroceryItem> maxItems = new ArrayList<>();
		int maxValue = 0;
		for(GroceryItem GI: groceries) {
			int curValue = GI.getConstraintBits().cardinality();
			if(curValue > maxValue) {
				maxItems.removeAll(maxItems);
				maxValue = curValue;
				maxItems.add(GI);
			}
			else if(curValue == maxValue) {
				maxItems.add(GI);
			}
		}
		//Return random LCV
		if(maxItems.size() == 1) {
			return maxItems.get(0);
		} else {
			Random rand = new Random();
			int randItem = rand.nextInt(maxItems.size());
			return maxItems.get(randItem);
		}
	}

}
