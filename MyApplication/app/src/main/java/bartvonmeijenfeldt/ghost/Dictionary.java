package bartvonmeijenfeldt.ghost;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;

public class Dictionary implements Serializable {
    private HashSet<String> dictionary = new HashSet<>();
    public HashSet<String> dictionaryFiltered = new HashSet<>();

    public Dictionary() {
    }

    public void add(String word) {
        dictionary.add(word);
        dictionaryFiltered.add(word);
    }

    public void filter(String wordStart) {

        Iterator<String> iterator = dictionaryFiltered.iterator();

        while ( iterator.hasNext()) {
            if (!(iterator.next().startsWith(wordStart))) {
                iterator.remove();
            }
        }
    }

    public int count() {
        return dictionaryFiltered.size();
    }

    public Boolean isWord(String word) {
        return dictionaryFiltered.contains(word);
    }

    public void reset() {
        dictionaryFiltered = (HashSet<String>) dictionary.clone();
    }

    public String getRandomWord() {
        int words = dictionaryFiltered.size();
        int wordChosen = (int) (Math.random() * words);

        Iterator<String> iterator = dictionaryFiltered.iterator();
        for (int i = 0; i < wordChosen; i++) {
            iterator.next();
        }
        return iterator.next();
    }

}