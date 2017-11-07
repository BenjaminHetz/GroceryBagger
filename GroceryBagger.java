import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

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
//			System.out.println("Max bags: " + maxBags + "\nMax Capacity per Bag: " + bagSize);
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
			
			// Print bags to check proper creation.
//			for(GroceryBag bag: bags) {
//				System.out.println(bag.toString());
//			}
			
			String result = depthFirstSearch(groceries, bags, totalItems, maxBags);
			if(result.equals("Success")){
				System.out.println(result);
				for(GroceryBag bag: bags) {
					System.out.println(bag.toString());
				}
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
	 * Perform a depth first search to determine how to bag the provided groceries with the provided parameters.
	 * 
	 * @param groceries An ArrayList of groceries to bag.
	 * @param bags An ArrayList of bags to put groceries in.
	 * @param totalItems The total number of items to bag.
	 * @param maxBags The total number of bags to use.
	 * @return "Success" if no problems, "Failed" otherwise.
	 */
	private static String depthFirstSearch(ArrayList<GroceryItem> groceries, ArrayList<GroceryBag> bags, int totalItems, int maxBags) {
		//Base case, return true.
		if(groceries.size() == 0) {
			return "Success";
		}
		//Get an ordered ArrayList of items.
		ArrayList<GroceryItem> ordList = LCV(groceries);
		ordList = MRV(ordList, bags, maxBags);
		//Test all items.
		for(int i = 0; i < totalItems; i++) {
			//Test all bags. If empty, break. TODO If time, continue on identical (constraints and weight).
			for(int b = 0; b < maxBags; b++) {
				String addResult = bags.get(b).addItem(ordList.get(i)); //Add item.
				//If failed to add, check next bag.
				if(addResult.contains("Fail")) {
					continue;
				} else { //If success, then update objects and call depthFirstSearch again.
					GroceryItem currItem = ordList.get(i);
					int currItemID = groceries.indexOf(currItem);
					groceries.remove(currItemID);
					totalItems--;
					String searchResult = depthFirstSearch(groceries, bags, totalItems, maxBags);
					//If failed, continue on to next bag/item. If success, return.
					if(searchResult.contains("Failed")) {
						groceries.add(currItemID, currItem);
						totalItems++;
						if(bags.get(b).empty()) {
							break;
						}
						continue;
					} else {
						return "Success";
					}
				}
			} //End items loop.
		} //End bags loop.
		//For Debugging.
		System.out.println("DFS Failed!");
		System.out.println("Total items: " + totalItems);
		for(GroceryItem GI: groceries) {
			System.out.println();
			System.out.println(GI.toString());
		}
		System.out.println("Total bags: " + maxBags);
		for(GroceryBag bag: bags) {
			System.out.println(bag.toString());
		}
		return "Failed";
	}

	/**
	 * Uses LCV to return an ordered ArrayList of groceries
	 * that came from the provided ArrayList of groceries.
	 * 
	 * @param groceries The ArrayList to sort.
	 * @return ArrayList of ordered groceries.
	 */
	private static ArrayList<GroceryItem> LCV(ArrayList<GroceryItem> groceries) {
		//Find LCVs
		ArrayList<GroceryItem> ordItems = new ArrayList<>();
		//Get and place every grocery.
		for(GroceryItem GI: groceries) {
			int currGIValue = GI.getConstraintBits().cardinality();
			//Compare each grocery to those already in the list.
			if(ordItems.size() == 0) {
				ordItems.add(GI);
				continue;
			}
			int max = ordItems.size();
			for(int i = 0; i < max; i++) {
				int currOrdValue = ordItems.get(i).getConstraintBits().cardinality();
				if(currGIValue > currOrdValue){
					ordItems.add(i, GI);
					break;
				} else if(currGIValue == currOrdValue) {
					int currOrdWeight = ordItems.get(i).getWeight();
					int currGIWeight = GI.getWeight();
					if(currGIWeight >= currOrdWeight) {
						ordItems.add(i, GI);
						break;
					}
				} else {
					if(i == (max - 1)) {
						ordItems.add(GI);
					}
				}
			} //End OrdItems loop.
			
		} //End GI loop
		return ordItems;
	}

	/**
	 * Uses MRV to return an further ordered ArrayList of groceries. MRV uses the relative order from the LCV
	 *  to return a list ordered by both heuristics.
	 * 
	 * @param groceries The LCV ordered list of groceries
	 * @param bags The bag ArrayList
	 * @param maxBags Total number of bags available.
	 * @return ArrayList ordered by MRV and LCV.
	 */
	private static ArrayList<GroceryItem> MRV(ArrayList<GroceryItem> groceries, ArrayList<GroceryBag> bags, int maxBags) {
		ArrayList<GroceryItem> ordItems = new ArrayList<>();
		//Add each item.
		for(GroceryItem GI: groceries) {
			int currGIValue = 0;
			for(int i = 0; i < maxBags; i++) {
				if(bags.get(i).empty()) {
					currGIValue += maxBags - i;
				} else {
					if(bags.get(i).getConstraintBits().get(GI.getID())) {
						currGIValue++;
					}
				}
			}
			//If first ordered item, just add.
			if(ordItems.size() == 0) {
				ordItems.add(GI);
				continue;
			}
			
			//Compare item to current ordered list and add.
			int max = ordItems.size();
			for(int i = 0; i < max; i++) {
				int currOrdValue = 0;
				//Get current ordered value.
				for(int j = 0; j < maxBags; j++) {
					if(bags.get(j).empty()) {
						currOrdValue += maxBags - j;
					} else {
						if(bags.get(j).getConstraintBits().get(GI.getID())) {
							currOrdValue++;
						}
					}
				} //End bags loop
				if(currGIValue < currOrdValue) {
					ordItems.add(i, GI);
					break;
				} else {
					if(i == (max - 1)) {
						ordItems.add(GI);
						break;
					}
				}
			} //End order values loop.
		} //End groceries loop.
		return ordItems;
	}
	
	/**
	 * TODO
	 * 
	 * @param bags
	 * @param items
	 * @param maxSteps
	 * @return
	 */
	private static Map<GroceryItem, GroceryBag> minConflicts(ArrayList<GroceryBag> bags, ArrayList<GroceryItem> items, int maxSteps){
		Map<GroceryItem, GroceryBag> solution = new HashMap<>();
		Random r = new Random();
		//generate a random assignment of all items
		for (GroceryItem GI: items){
			solution.put(GI, bags.get(r.nextInt(bags.size())));
		}
		for (int i = 0; i < maxSteps; i++){
			ArrayList<GroceryItem> failedItems = isSolution(solution);
			if (failedItems.isEmpty()){
				//add the items as they appear in this map and that is solution
				for (GroceryItem GI: solution.keySet()){
					solution.get(GI).addItem(GI);
				}
				return solution;
			} else {
				//pick a random failed item
				//save original mapping for that item so we can restore it if need be
				GroceryItem itemToChange = failedItems.get(r.nextInt(failedItems.size()));
				GroceryBag bestBag = null;

				int minConflicts = Integer.MAX_VALUE;
				for (GroceryBag GB: bags){
//					solution.replace(itemToChange, GB);
//					ArrayList<GroceryItem> trying = isSolution(solution);
//					if (trying.size() < minConflicts){
//						minConflicts = trying.size();
//						bestBag = GB;
//					} else{
//						solution.replace(itemToChange, bestBag);
//					}
					int numConflicts = 0;
					BitSet conflictingItemBits = itemToChange.getConstraintBits();
					for (GroceryItem GI: GB.getItems()){
						if (conflictingItemBits.get(GI.getID())){
							//Not a conflict since that item can be bagged with the other one
						} else {
							numConflicts++;
						}
					}
					if (numConflicts < minConflicts){
						minConflicts = numConflicts;
						bestBag = GB;
					}
				}
				solution.replace(itemToChange, bestBag);
			}
		}
		return solution;
	}
	
	/**
	 * TODO
	 * 
	 * @param possibleSolution
	 * @return
	 */
	private static ArrayList<GroceryItem> isSolution(Map<GroceryItem, GroceryBag> possibleSolution){
		ArrayList<GroceryItem> failedItems = new ArrayList<>();
		for (GroceryItem GI: possibleSolution.keySet()){
			String result = possibleSolution.get(GI).addItem(GI);
			if (result.contains("Fail")){
				failedItems.add(GI);
			}
		}
		return failedItems;
	}
}
