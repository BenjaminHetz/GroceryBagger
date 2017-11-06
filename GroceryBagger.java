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
			
			depthFirstSearch(groceries, bags, totalItems, maxBags);
			
			
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

	private static String depthFirstSearch(ArrayList<GroceryItem> groceries, ArrayList<GroceryBag> bags, int totalItems, int maxBags) {
		
		//Create Ord ArrayList of items.
		ArrayList<GroceryItem> currList = LCV(groceries);
		currList = MRV(groceries, bags, maxBags);

		//While newBag, test bag.
		//If success, return, else continue looping.
		//If out of loops, fail.
		/*for(int i = 0; i < totalItems; i++) {
			//Find best bag and item to it.
			int idealBag = idealBag(currItem, bags, maxBags);
			if(idealBag == -1) {
				System.out.println("Something is wrong with the heuristics");
				for(GroceryItem GI: groceries) {
					System.out.println(GI.toString());
				}
				for(GroceryBag GB: bags) {
					System.out.println(GB.toString());
				}
				System.exit(1);
			} else {
				String bagResult = bags.get(idealBag).addItem(currItem);
				if(bagResult.contains("Failed")) {
					System.out.println(bagResult);
					System.out.println("Something is wrong with addItem.");
				} else {
					//Update objects and Recursively search(DFS) for full solution
					int currItemID = currItem.getID();
					groceries.remove(currItem);
					totalItems--;
					String searchResult = depthFirstSearch(groceries, bags, totalItems, maxBags);
					if(searchResult.contains("Failed")) {
						groceries.add(currItemID, currItem);
						totalItems++;
						continue;
					} else {
						
					}
					
				}
			}
		}
		*/
		return "";
	}

	/**
	 * Finds the best bag to insert the given item
	 * 
	 * @param currItem
	 * @param bags
	 * @param maxBags
	 * @return
	 */
	/* No longer needed?
	private static int idealBag(GroceryItem currItem, ArrayList<GroceryBag> bags, int maxBags) {
		GroceryBag currBag;
		for(int i = 0; i < maxBags; i++) {
			if(bags.get(i).empty()) {
				return i;
			} else {
				BitSet currBitSet = bags.get(i).getConstraintBits();
				if(currBitSet.get(currItem.getID())) {
					return i;
				}
			}
		}
		return -1;
	}
	*/
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
			for(int i = 0; i < ordItems.size(); i++) {
				if(ordItems.size() == 0) {
					ordItems.add(GI);
					break;
				}
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
				}
			} //End OrdItems loop.			
		} //End GI loop
		return ordItems;
	}

	/**
	 * Uses MRV to return an further ordered ArrayList of groceries.
	 * MRV uses the relative order from the LCV to return a list 
	 * ordered by both heuristics.
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
			//Compare item to current ordered list and add.
			for(int i = 0; i < ordItems.size(); i++) {
				if(ordItems.size() == 0) {
					ordItems.add(GI);
					break;
				}
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
				if(currGIValue > currOrdValue) {
					ordItems.add(i, GI);
					break;
				}

			} //End order values loop.
		} //End groceries loop.
		return ordItems;
	}
//	private static Map<GroceryItem, GroceryBag> minConflicts(ArrayList<GroceryBag> bags, ArrayList<GroceryItem> items, int maxSteps){
//		Map<GroceryItem, GroceryBag> solution = new HashMap<>();
//		Random r = new Random();
//		//generate a random assignment of all items
//		for (GroceryItem GI: items){
//			solution.put(GI, bags.get(r.nextInt(bags.size())));
//		}
//		for (int i = 0; i < maxSteps; i++){
//			ArrayList<GroceryItem> failedItems = isSolution(solution);
//			if (failedItems.isEmpty()){
//				//add the items as they appear in this map and that is solution
//				for (GroceryItem GI: solution.keySet()){
//					solution.get(GI).addItem(GI);
//				}
//				return solution;
//			} else {
//				//pick a random failed item
//				GroceryItem itemToChange = failedItems.get(r.nextInt(failedItems.size()));
//				GroceryBag bestBag = null;
//
//				int minConflicts = Integer.MAX_VALUE;
//				for (GroceryBag GB: bags){
//					int numConflicts = 0;
//					BitSet conflictingItemBits = GB.getConstraintBits();
//					for (GroceryItem GI: GB.getItems()){
//						if (conflictingItemBits.get(GI.getID())){
//							//Not a conflict since that item can be bagged
//						} else {
//							numConflicts++;
//						}
//					}
//					if (numConflicts < minConflicts){
//						minConflicts = numConflicts;
//						bestBag = GB;
//					}
//				}
//				solution.replace(itemToChange, bestBag);
//			}
//		}
//	}
	private static ArrayList<GroceryItem> isSolution(Map<GroceryItem, GroceryBag> possibleSolution){
		ArrayList<GroceryItem> failedItems = new ArrayList<>();
		for (GroceryItem GI: possibleSolution.keySet()){
			String result = possibleSolution.get(GI).addItem(GI);
			if (result.equals("Failed: weight") || result.equals("Failed: constraints")){
				failedItems.add(GI);
			}
		}
		return failedItems;
	}
}
