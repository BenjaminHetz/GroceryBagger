#!/bin/sh

#This shell script will perform timing for various problems
#in the grocery problem
make

for file in $(ls | grep "GroceryTest")
do
	for  i in `seq 1 10`;
	do
		echo "Testing file" $file "run number" $i
		#echo "Normal" > $file.normal.time
		{ time java GroceryBagger $file >> $file.normal.output; } 2>> $file.normal.time
		#echo "Local" > $file.local.time
		{ time java GroceryBagger $file -local >> $file.local.output ; } 2>> $file.local.time
		#echo "Slow" > $file.slow.time
		{ time java GroceryBagger $file -slow >> $file.slow.output; } 2>> $file.slow.time
	done
done
#make clean
