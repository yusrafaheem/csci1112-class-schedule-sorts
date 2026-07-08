# CSCI 1112 Class Schedule Sorting

This project models a university class schedule and profiles four different sorting algorithms (selection sort, bubble sort, insertion sort, and quicksort) that order a set of classes in descending order by their time differential, the difference between a class's end time and start time. It was written for GWU CSCI 1112 in Spring 2025.

## What I wrote

I implemented ClassSchedule.java, which contains the core logic for this assignment. The clone method performs a deep copy of the schedule data. The createView method creates a shallow, row-level copy so sorting can rearrange references without disturbing the original data. The differential method computes the time difference for a class, and lessThan uses that differential to compare two classes. The swapClasses method swaps two references within a view. Finally, sortSelection, sortBubble, and sortInsertion implement three classic sorting algorithms in descending order by differential, while sortQuicksort implements a recursive quicksort using Lomuto partitioning. Each sort returns a profile of the number of comparisons and swaps it performed.

## Provided scaffolding (not my code)

ProfileSorts.java, ScheduleReader.java, UnitTests.java, and the classes.csv, classes1k.csv, and classes10k.csv data files were provided by the course as scaffolding and testing infrastructure. I did not write these files.

## Running it

ProfileSorts.main reads each CSV file with ScheduleReader.getSchedule, sorts a view of the data with each of the four algorithms, and prints the comparison and swap counts for each one so their performance can be compared across dataset sizes.
