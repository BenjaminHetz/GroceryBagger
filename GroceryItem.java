import java.util.ArrayList;
import java.util.BitSet;

/**
 * 
 * @author benhetz, tyleregan
 */
public class GroceryItem {
    private String itemName;
    private boolean plusConstraint;
    private ArrayList<String> constraints;
    private int weight;
    private int id;
    private BitSet constraintBits;


    /**
     * @param name The name of this item
     * @param plusConstraint Items this one can be bagged with.
     * @param weight The weight of the item.
     * @param constraints Items this one can't be bagged with.
     * @param id Identifier for which item this is.
     */
    public GroceryItem(String name, boolean plusConstraint, int weight, ArrayList<String> constraints, int id){
        this.constraints = new ArrayList<>();

        this.itemName = name;
        this.plusConstraint = plusConstraint;
        this.weight = weight;
        this.id = id;
        
        this.constraints.addAll(constraints);
    }
    
    /**
     * Getter for the item's constraints.
     * 
     * @return This item's constraints.
     */
    public BitSet getConstraintBits() {
    	return this.constraintBits;
    }
    
    /**
     * Getter for the item's ID.
     * 
     * @return This item's ID.
     */
    public int getID() {
    	return this.id;
    }
    
    /**
     * Getter for the item's name.
     * 
     * @return This item's name.
     */
    public String getItemName() {
    	return this.itemName;
    }
    
    /**
     * Getter for the item's constraint type.
     * 
     * @return True if can be bagged with, false if can't be bagged with.
     */
    public boolean getPlusContraint() {
    	return this.plusConstraint;
    }
    
    /**
     * Getter for the item's weight.
     * 
     * @return This item's weight.
     */
    public int getWeight() {
    	return this.weight;
    }
    
    public void setConstraintBits(int totalItems) {
    	constraintBits = new BitSet(totalItems);
    	//TODO figure out how to set the constraints.
    }
    
    public String toString(){
        String returnString = "";
        returnString += "Name: " + this.itemName + "\n";
        returnString += "Weight: " + this.weight + "\n";
        if (this.constraints.size() != 0) {
            returnString += "Constraint type: ";
            if (this.plusConstraint) {
                returnString += "Plus Constraint\n";
            } else {
                returnString += "Minus Constraint\n";
            }

            returnString += "Constraining Items: \n";
            for (String s : this.constraints) {
                returnString += "\t" + s + "\n";
            }
        } else {
            returnString += "No Constraints\n";
        }
        return returnString;
    }
}
