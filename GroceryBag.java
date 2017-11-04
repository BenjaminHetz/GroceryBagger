import java.util.ArrayList;
import java.util.BitSet;

/**
 * 
 * @author tyleregan, benhetz
 */
public class GroceryBag {
    private ArrayList<GroceryItem> items;
    private BitSet constraintBits;
    private int currWeight;
    private int maxWeight;
    


    /**
     * @param maxWeight the maximum weight the bag can hold.
     * @param totalItems the total items to bag.
     */
    public GroceryBag(int maxWeight, int totalItems){
        this.items = new ArrayList<GroceryItem>();

        this.currWeight = 0;
        this.maxWeight = maxWeight;
        this.constraintBits = new BitSet(totalItems);
    }
    

    /**
     * Try to add the passed in item to this bag.
     * 
     * @param item The item to add to the bag.
     * @return True if added successfully, false otherwise.
     */
    public boolean addItem(GroceryItem item) {
    	int id = item.getID();
    	boolean result;
    	if(item.getWeight() + currWeight > maxWeight) {
    		result = false;
    	} else if(!constraintBits.get(id)) {
    		result = false;
    	} else {
    		currWeight += item.getWeight();
    		constraintBits.and(item.getConstraintBits());
    		result = true;
    	}
    	return result;
    }
    
    public String toString(){
        String returnString = "";
        
        for(GroceryItem item: items) {
        	returnString += item.toString() + "\n";
        }
        
        return returnString;
    }
}
