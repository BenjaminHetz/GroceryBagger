SRC=GroceryBagger.java GroceryItem.java GroceryBag.java
EXE=GroceryBagger

build: $(SRC)
	javac $(SRC)

clean:
	rm  -f *.class *.output *.time
