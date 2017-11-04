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
    

    public String addItem(GroceryItem item) {
    	int id = item.getID();
    	String result = "";
    	if(constraintBits.get(id)) {
    		result = "Invalid";
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
