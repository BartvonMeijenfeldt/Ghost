package bartvonmeijenfeldt.ghost;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


/**
 * Created by startklaar on 18-4-2015.
 */

public class Dictionary {
    private HashSet<String> dictionary = new HashSet<String>();
    public HashSet<String> dictionary_filtered = new HashSet<String>();

    public Dictionary() {

    }

    public Dictionary(String words) {

        String temp_dictionary[] = words.split("\n");
        int length = temp_dictionary.length;
        for (int i = 0; i < length; i++) {
            dictionary.add(temp_dictionary[i]);
        }

        dictionary_filtered = (HashSet<String>) dictionary.clone();

    }

    public void add(String word) {
        dictionary.add(word);
        dictionary_filtered.add(word);
    }

    public void filter(String word_start) {

        Iterator<String> iterator = dictionary_filtered.iterator();

        while ( iterator.hasNext()) {
            if (!(iterator.next().startsWith(word_start))) {
                iterator.remove();
            }
        }

       /*

            if (l.charAt(number_count) != letter)
                dictionary_filtered.remove(l);
        }

    */
    }

    public int count() {

        return dictionary_filtered.size();

    }

    //returns true if the chars entered form a word
    //returns false if the chars entered do not form a word
    public Boolean isWord(String word) {

        return dictionary_filtered.contains(word);
    }

    public String result() {

        Iterator<String> iterator = dictionary_filtered.iterator();
        return iterator.next();
    }


    public void reset() {

        dictionary_filtered = (HashSet<String>) dictionary.clone();

    }

    public String getRandomWord() {
        int words = dictionary_filtered.size();
        int word_chosen = (int) (Math.random() * words);

        Iterator<String> iterator = dictionary_filtered.iterator();
        for (int i = 0; i < word_chosen; i++) {
            iterator.next();
        }
        return iterator.next();
    }

}