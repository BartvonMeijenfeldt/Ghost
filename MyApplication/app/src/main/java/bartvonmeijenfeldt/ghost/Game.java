package bartvonmeijenfeldt.ghost;

public class Game {

    public Dictionary dict;
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

    boolean ended() {
        return ended;
    }

    // false stands for player 1
    // true stands for player 2
    boolean winner() {
        return player;
    }

    String updateRandomLetters(Integer numberOfLetters) {
        String wordRandom;

        //gets a random word from dictionary and checks if it has enough letters
        //repeats until a long enough word is found
        do {
            wordRandom = dict.getRandomWord();
        } while (wordRandom.length() < numberOfLetters + 1);
        String returnWord = "";
        for(int i = 0; i < numberOfLetters; i++) {
            this.guess(wordRandom.charAt(i));

            if (i == 0) {
                returnWord = (returnWord + wordRandom.charAt(i)).toUpperCase();
            } else {
                returnWord = returnWord + wordRandom.charAt(i);
            }
        }
        return returnWord;
    }

    Boolean lossByMadeWord(){
        return dict.isWord(this.word);
    }

    void resetGame() {
        dict.reset();
        player = false;
        ended = false;
        word = "";
    }





}