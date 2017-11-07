import java.util.ArrayList;
import java.util.BitSet;

/**
 * 
 * @author tyleregan, benhetz
 */
public class GroceryBag {
    private ArrayList<GroceryItem> items;
    private BitSet constraintBits;
    private int bagID;
    private int currItems;
    private int currWeight;
    private int maxWeight;
    private int totalItems;
    


    /**
     * @param maxWeight the maximum weight the bag can hold.
     * @param totalItems the total items to bag.
     */
    public GroceryBag(int maxWeight, int bagID, int totalItems){
        this.items = new ArrayList<GroceryItem>();
        this.constraintBits = new BitSet(totalItems);
        this.constraintBits.flip(0, totalItems);

        this.bagID = bagID;
        this.currItems = 0;
        this.currWeight = 0;
        this.maxWeight = maxWeight;
        this.totalItems = totalItems;
    }
    

    /**
     * Try to add the specified item to the bag.
     * 
     * @param item The item to add to the bag.
     * @return True if added successfully, false otherwise.
     */
    public String addItem(GroceryItem item) {
    	int id = item.getID();
    	String result;
    	if(item.getWeight() + currWeight > maxWeight) {
    		result = "Failed: weight";
    	} else if(!constraintBits.get(id)) {
    		result = "Failed: Bag can't take item";
    	} else {
    		for(GroceryItem GI: items) {
    			if(!item.getConstraintBits().get(GI.getID())) {
    				result = "Failed: Item can't go in bag";
    				return result;
    			}
    		}
    		items.add(item);
    		currItems++;
    		currWeight += item.getWeight();
    		constraintBits.and(item.getConstraintBits());
    		result = "Success";
    	}
    	return result;
    }
    
    /**
     * Checks if the bag is empty.
     * 
     * @return True if empty, false otherwise.
     */
    public boolean empty() {
    	return currItems == 0;
    }
    
    /**
     * Getter for the constraint BitSet
     * 
     * @return The constraint BitSet
     */
    public BitSet getConstraintBits() {
    	return constraintBits;
    }
    
    /**
     * Getter for the ArrayList of the bag's current items.
     * 
     * @return ArrayList containing the current items in the bag.
     */
    public ArrayList<GroceryItem> getItems(){
    	return items;
    }
    
    /**
     * Removes the specified item from the bag if the
     * item is in the bag.
     * 
     * @param item The item to remove from the bag
     * @return "Success" if removed, "Failed: <reason>" otherwise.
     */
    public String removeItem(GroceryItem item) {
    	int id = item.getID();
    	String result;
    	if(empty()) { //Item is not in the bag.
    		result = "Failed: empty Bag";
    	}
    	else if(!constraintBits.get(id)) {
    		result = "Failed: not in bag";
    	}
    	else {
    		items.remove(item);
    		currItems--;
    		currWeight -= item.getWeight();
    		constraintBits.clear(0, totalItems);
    		constraintBits.flip(0, totalItems);
    		for(GroceryItem GI: items) {
    			constraintBits.and(GI.getConstraintBits());
    		}
    		result = "Success";
    	}
    	return result;
    }
    public void clearBag(){
    	for (int i = 0; i < this.items.size(); i++){
    		this.removeItem(this.items.get(i));
		}
	}
    
    
    // toString to properly print for the assignment.
    public String toString(){
        String returnString = "";
        for(GroceryItem GI: items) {
			returnString += GI.getItemName() + "\t";
		}
        return returnString;
    }
    
  
    /**
     * Additional toString method for programming purposes.
     * This toString will print out additional information so someone
     * programming with this class can better understand what their program
     * is doing.
     * 
     * @param debug "quick" or "detailed"
     * @return A string of additional information about the bag.
     */
    public String toString(String debug){
    	String returnString = "Bag " + bagID;
    	if(empty()) {
        	returnString += " is empty.";
    	} else {
    		if(debug.equals("quick")) {
    			returnString += ":\n";
    			for(GroceryItem GI: items) {
    				returnString += GI.getItemName() + "\t";
    			}
    		}
    		else if(debug.equals("detailed")) {
    			returnString += " contains " + currItems + ":";
    	        for(GroceryItem item: items) {
    	        	returnString += "\n\t" + item.toString();
    	        }
    		}
    	}
        return returnString;
    }
}
