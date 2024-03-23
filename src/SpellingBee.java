import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [ADD YOUR NAME HERE]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // TODO: generate all possible substrings and permutations of the letters.
    //  Store them all in the ArrayList words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void generate() {
        // Call a recursive function with the correct starting parameters which generates
        // all the possible combinations of letters from the "letters" input
        recGenerate("", letters);
    }

    // Recursive function to generate all word combinations
    public void recGenerate(String str1, String str2){
        // Base case
        if (str2.isEmpty()){
            words.add(str1);
            return;
        }
        // Add the new word to the ArrayList words
        words.add(str1);
        // Recursive cases
        for(int i = 0; i < str2.length(); i++){
            recGenerate(str1 + str2.substring(i, i + 1), str2.substring(0, i) + str2.substring(i + 1));
        }
    }

    // TODO: Apply mergesort to sort all words. Do this by calling ANOTHER method
    //  that will find the substrings recursively.
    public void sort() {
        // Create an array which will hold the list of generated words
        String[] wordArr = new String[words.size()];
        // Transcribe the words ArrayList into this new array
        for(int i = 0; i < words.size(); i++){
            wordArr[i] = words.get(i);
        }
        // Call the recursive mergeSort function with the correct
        wordArr = mergeSort(wordArr, 0, wordArr.length - 1);
        for(int i = 0; i < wordArr.length; i++){
            words.set(i, wordArr[i]);
        }
    }

    // Recursive mergesort method to sort all the possible words by alphabetical order
    public String[] mergeSort(String[] arr, int left, int right){
        // Base case — if the section in question is only one value long
        if(left - right == 0){
            String[] newArr = new String[1];
            newArr[0] = arr[left];
            return newArr;
        }
        // Find the middle of the section
        int mid = (left + right)/2;
        // Recursive step: call mergeSort on the left and right of the middle of the section
        String[] arr1 = mergeSort(arr, left, mid);
        String[] arr2 = mergeSort(arr, mid + 1, right);
        // Merge all the sections back together at the end with the helper function merge
        return merge(arr1, arr2);
    }

    // Helper function which handles merging all the sections from the mergeSort method
    public String[] merge(String[] arr1, String[] arr2){
        // Create a new array with the length both arrays combined
        String[] sol = new String[arr1.length + arr2.length];
        // Create counters for position in each array and total terms added
        int index1 = 0, index2 = 0, count = 0;
        // While there are still numbers in both arrays
        while(index1 < arr1.length && index2 < arr2.length) {
            // If the string at index1 in arr1 should come before the string at index2 in arr2:
            if(arr1[index1].compareTo(arr2[index2]) <= 0) {
                sol[count++] = arr1[index1++];
            }
            else{
                sol[count++] = arr2[index2++];
            }
        }
        // Add the remaining strings from whichever array did not run out
        while(index1 < arr1.length){
            sol[count++] = arr1[index1++];
        }
        while(index2 < arr2.length){
            sol[count++] = arr2[index2++];
        }
        // Return the merged array
        return sol;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // TODO: For each word in words, use binary search to see if it is in the dictionary.
    //  If it is not in the dictionary, remove it from words.
    public void checkWords() {
        // Cycle through the whole array of generated words to check if they exist in the dictionary
        for(int i = 0; i < words.size(); i++){
            // If not in the dictionary (determined with helper function), remove from the array of possible words
            if(!isInDictionary(words.get(i), 0, DICTIONARY_SIZE - 1)){
                words.remove(i--);
            }
        }
    }

    // Recursive binary sort function which searches for a certain word in the dictionary
    public boolean isInDictionary(String word, int start, int end){
        // Base case: if the section of the dictionary being searched is one entry long
        if(start == end){
            // Return whether that single entry is the word being searched for or not
            return DICTIONARY[start].equals(word);
        }
        // Find the midpoint of the section being searched
        int mid = (end + start) / 2;
        // If that midpoint is the word being searched for, return true.
        if(DICTIONARY[mid].equals(word)){
            return true;
        }
        // Recursive step: if the midpoint word comes before the word being searched for, run isInDictionary on the second half
        if(DICTIONARY[mid].compareTo(word) < 0){
            return isInDictionary(word, mid + 1, end);
        }
        // Otherwise run it on the first half
        return isInDictionary(word, start, mid);
    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
