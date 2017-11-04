import java.util.ArrayList;
public class GroceryItem {
    private String itemName;
    private boolean plusConstraint;
    private ArrayList<String> constraints;
    private int weight;


    public GroceryItem(String name, boolean plusConstraint, int weight, ArrayList<String> constraints){
        this.constraints = new ArrayList<>();

        this.itemName = name;
        this.plusConstraint = plusConstraint;
        this.weight = weight;

        this.constraints.addAll(constraints);
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
