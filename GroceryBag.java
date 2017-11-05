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
    		result = "Failed: constraints";
    	} else {
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
    
    public BitSet getConstraintBits() {
    	return constraintBits;
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
    
    //Once testing is done, needs updated to proper format.
    public String toString(){
        String returnString = "Bag " + bagID;
        
        if(empty()) {
        	returnString += " is empty.";
        } else {
        	returnString += " contains " + currItems + ":";
	        for(GroceryItem item: items) {
	        	returnString += "\n\t" + item.toString();
	        }
        }
        
        return returnString;
    }
}
