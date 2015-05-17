package bartvonmeijenfeldt.ghost;

import java.util.List;

/**
 * Created by startklaar on 18-4-2015.
 */
public class Game {


    private Dictionary dict;
    boolean player = false;
    boolean ended = false;
    String word = "";


    Game(Dictionary dictionary) {

        dict = dictionary;


    }

    void guess(char letter) {

        word = word + letter;

        dict.filter(word);

        player = !player;

        if (dict.count() == 0) {
            ended = true;
            return;
        }

        if (word.length() > 3) {
            ended = dict.isWord(word);
        }
    }
    // false stands for player 1
    // true stands for player 2
    boolean turn() {

        return player;
    }

    // false stands for still going on
    // true stands for ended

    boolean ended() {

        return ended;
    }


    // false stands for player 1
    // true stands for player 2

    boolean winner() {

        return player;
    }

    String word_random;
    String updateRandomLetters(Integer letters) {
        do {
            word_random = dict.getRandomWord();
        } while (word_random.length() < letters + 1);
        String returnWord = "";
        for(int i = 0; i < letters; i++) {
            this.guess(word_random.charAt(i));

            if (i == 0) {
                returnWord = (returnWord + word_random.charAt(i)).toUpperCase();
            } else {
                returnWord = returnWord + word_random.charAt(i);
            }
        }
        return returnWord;
    }

}