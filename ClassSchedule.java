/*--------------------------------------------------------------------------
GWU CSCI 1112 Spring 2025
author: Yusra Faheem

This class encapsulates the logic to model scheduling a set of classes for a university
--------------------------------------------------------------------------*/

public class ClassSchedule {
    /// Performs a deep copy of the input schedule and returns the deep copy.
    /// This operation might be used to make a permanent backup of the data
    /// as it would make a unique and unlinked copy of the data.
    /// @param schedule the schedule array to copy
    /// @return the deep copy of the schedule array that were copied
    public static String[][] clone(String[][] schedule) {
        if (schedule == null) {
            return null;
        }

        String[][] copy = new String[schedule.length][];
        for (int i = 0; i < schedule.length; i++) {
            if (schedule[i] == null) {
                copy[i] = null;
                continue;
            }
            copy[i] = new String[schedule[i].length];
            for (int j = 0; j < schedule[i].length; j++) {
                copy[i][j] = schedule[i][j];
            }
        }

        return copy;
    }

    /// A referential copy (shallow copy of each row) and not an
    /// element-wise copy (deep copy).  We are sorting elements with respect
    /// to the original data rather than generating a new set of data.
    /// @param schedule data containing the rows to reference
    /// @return an array containing a shallow copy of the input schedule
    ///         rows
    public static String[][] createView(String[][] schedule) {
        if (schedule == null) {
            return null;
        }

        // New outer array, but each row is a reference back to the
        // original row - a shallow, row-level copy
        String[][] view = new String[schedule.length][];
        for (int i = 0; i < schedule.length; i++) {
            view[i] = schedule[i];
        }

        return view;
    }

    //------------------------------------------------------------------
    /// Compute the differential between start time (index 3) and end time
    /// (index 4). The differential is not maintained in the data but is
    /// a virtual field derived by the calculation performed here
    /// @param classInfo a record from the scheduling data
    /// @return the difference in time between the end time and start time
    ///         in minutes
    public static int differential(String[] classInfo) {
        int start = duration(classInfo[3]);
        int end = duration(classInfo[4]);
        return end - start;
    }

    //------------------------------------------------------------------
    /// This utility function converts a time string from the "HH:mm:ss"
    /// format into a value representing minutes
    /// @param time a string representing a time in "HH"mm:ss" format
    /// @return an integer representing the time converted to minutes
    private static int duration(String time) {
        String[] tokens = time.split(":");
        int h = Integer.parseInt(tokens[0]);
        int m = Integer.parseInt(tokens[1]);
        int t = h * 60 + m;
        return t;
    }

    //------------------------------------------------------------------
    /// Performs a comparison between two classes that is equivalent to a
    /// less than operation so that a sort can use this function to order
    /// classes. The less than criteria is an evaluation between the
    /// differentials of two classes.
    /// @param class1 a class record that is used as the "left" operand for
    ///        a less than comparison
    /// @param class2 a class record that is used as the "right" operand for
    ///        a less than comparison
    /// @return returns true if the computed differential for class1 is less
    ///         than the computed differential for class2; otherwise,
    ///         returns false (false implies that differential for class1 is
    ///         greater than or equal to class2)
    public static boolean lessThan(String[] class1, String[] class2) {
        return differential(class1) < differential(class2);
    }
    //------------------------------------------------------------------
    /// Swaps references to classes.  Note that this is a "shallow" swap and
    /// not a "deep" swap meaning we swap at a reference level (between rows
    /// in view) and not at the value level
    /// @param view A shallow copy of a set of classes
    /// @param i the index of the first reference to swap
    /// @param j the index of the second reference to swap
    public static void swapClasses(String[][] view, int i, int j) {
        String[] temp = view[i];
        view[i] = view[j];
        view[j] = temp;
    }

    //------------------------------------------------------------------
    /// Sorts (shallow) a set of references to classes in descending order
    /// subject to the differential between ups and downs using selection
    /// sort
    /// @param view A shallow copy of a set of classes
    /// @return an array of profile information of 3 buckets with the
    ///         respective buckets containing a count of 0: allocations,
    ///         1:comparisons, and 2: swaps
    public static int[] sortSelection(String[][] view) {
        int comparisons = 0;
        int swaps = 0;
        int n = view.length;

        // Each pass finds the largest remaining differential and moves it
        // into place at the front of the unsorted region
        for (int i = 0; i < n - 1; i++) {
            int maxIndex = i;
            for (int j = i + 1; j < n; j++) {
                comparisons++;
                if (lessThan(view[maxIndex], view[j])) {
                    maxIndex = j;
                }
            }
            if (maxIndex != i) {
                swapClasses(view, i, maxIndex);
                swaps++;
            }
        }

        return new int[]{0, comparisons, swaps};
    }

    //------------------------------------------------------------------
    /// Sorts (shallow) a set of references to classes in descending order
    /// subject to the differential between ups and downs using bubble
    /// sort
    /// @param view A shallow copy of a set of classes
    /// @return an array of profile information of 3 buckets with the
    ///         respective buckets containing a count of 0: allocations,
    ///         1:comparisons, and 2: swaps
    public static int[] sortBubble(String[][] view) {
        int comparisons = 0;
        int swaps = 0;
        int n = view.length;

        // Each pass bubbles a larger differential leftward whenever it is
        // found to the right of a smaller one; stop early once a full pass
        // makes no swaps
        for (int i = 0; i < n - 1; i++) {
            boolean swappedThisPass = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparisons++;
                if (lessThan(view[j], view[j + 1])) {
                    swapClasses(view, j, j + 1);
                    swaps++;
                    swappedThisPass = true;
                }
            }
            if (!swappedThisPass) {
                break;
            }
        }

        return new int[]{0, comparisons, swaps};
    }

    //------------------------------------------------------------------
    /// Sorts (shallow) a set of references to classes in descending order
    /// subject to the differential between ups and downs using insertion
    /// sort
    /// @param view A shallow copy of a set of classes
    /// @return an array of profile information of 3 buckets with the
    ///         respective buckets containing a count of 0: allocations,
    ///         1:comparisons, and 2: swaps
    public static int[] sortInsertion(String[][] view) {
        int comparisons = 0;
        int swaps = 0;
        int n = view.length;

        // Each newly considered element is walked leftward, swapping with
        // its neighbor, for as long as that neighbor has a smaller
        // differential (i.e. belongs after it in descending order)
        for (int i = 1; i < n; i++) {
            int j = i;
            while (j > 0) {
                comparisons++;
                if (lessThan(view[j - 1], view[j])) {
                    swapClasses(view, j, j - 1);
                    swaps++;
                    j--;
                } else {
                    break;
                }
            }
        }

        return new int[]{0, comparisons, swaps};
    }

    //------------------------------------------------------------------
    /// Sorts (shallow) a set of references to classes in descending order
    /// subject to the differential between ups and downs using a quicksort.
    /// @param view A shallow copy of a set of classes
    /// @return an array of profile information of 3 buckets with the
    ///         respective buckets containing a count of 0: allocations,
    ///         1:comparisons, and 2: swaps
    public static int[] sortQuicksort(String[][] view) {
        int[] profile = new int[]{0, 0, 0};
        quicksortHelper(view, 0, view.length - 1, profile);
        return profile;
    }

    //------------------------------------------------------------------
    /// Recursive helper for sortQuicksort: sorts the sub-range
    /// [low, high] of view in place (descending by differential),
    /// accumulating comparisons and swaps into profile.
    /// @param view the shallow view being sorted
    /// @param low the low index of the sub-range to sort (inclusive)
    /// @param high the high index of the sub-range to sort (inclusive)
    /// @param profile the running profile counters to update
    private static void quicksortHelper(String[][] view, int low, int high, int[] profile) {
        if (low < high) {
            int p = partition(view, low, high, profile);
            quicksortHelper(view, low, p - 1, profile);
            quicksortHelper(view, p + 1, high, profile);
        }
    }

    //------------------------------------------------------------------
    /// Lomuto-style partition (descending): uses view[high] as the pivot
    /// and rearranges [low, high] so that every element with a larger
    /// differential than the pivot ends up to its left.
    /// @param view the shallow view being partitioned
    /// @param low the low index of the range to partition (inclusive)
    /// @param high the high index of the range to partition (inclusive);
    ///        also the index of the pivot element
    /// @param profile the running profile counters to update
    /// @return the final index of the pivot after partitioning
    private static int partition(String[][] view, int low, int high, int[] profile) {
        String[] pivot = view[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            profile[1]++;
            if (lessThan(pivot, view[j])) {
                i++;
                swapClasses(view, i, j);
                profile[2]++;
            }
        }

        swapClasses(view, i + 1, high);
        profile[2]++;

        return i + 1;
    }
}
