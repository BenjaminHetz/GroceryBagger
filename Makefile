SRC=GroceryBagger.java GroceryItem.java GroceryBag.java
EXE=GroceryBagger

build: $(SRC)
	javac $(SRC)

time: build
	./timing.sh > output.txt

clean:
	rm *.class
