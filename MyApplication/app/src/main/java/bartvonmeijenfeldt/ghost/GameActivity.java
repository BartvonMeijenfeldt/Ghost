package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import android.widget.Toast;


public class GameActivity extends ActionBarActivity {

    String name_player1;
    String name_player2;
    private TextView outputTextView;
    private TextView dictionaryTextView;
    private TextView turnTextView;
    private EditText letterEditText;
    Dictionary dict;
    Game game;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;
    Boolean dutch;
    Set<String> user_names;
    Set<String> user_names_returning;
    String[] user_names_array;
    Integer[] wins_array;
    String winner;
    String loser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        preferenceSettings = getSharedPreferences("userNames",Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        outputTextView = (TextView) findViewById(R.id.word_textView);
        dictionaryTextView = (TextView) findViewById(R.id.dictionary_textView);
        turnTextView = (TextView) findViewById(R.id.turn_textView);
        letterEditText = (EditText) findViewById(R.id.letter_editText);

        readDictionaryAndCreateGame();
        getPlayerNames();
        String word = preferenceSettings.getString("word", null);
        if (word != null) {
            recreateSavedGame(word);
        }
        pickRandomLetters();
        setTextViewsOnCreate();
    }

    private void recreateSavedGame(String word) {
        for(int i = 0; i < word.length(); i++) {
            game.guess(word.charAt(i));
        }
        preferenceEditor.remove("word");
        preferenceEditor.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.change_language) {
            dutch = !dutch;
            preferenceEditor.putBoolean("dutch", dutch);
            preferenceEditor.commit();
        }

        Intent restart_game_activity = new Intent(this, GameActivity.class);
        restart_game_activity.putExtra("player1", name_player1);
        restart_game_activity.putExtra("player2", name_player2);
        startActivity(restart_game_activity);
        finish();
        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void click(View view) {

        String input = letterEditText.getText().toString().toLowerCase();
        if(errorCheckingInput(input)){
            return;
        }

        updateGame(input.charAt(0));

        if (game.ended()) {
            getUserNames();
            setWinnerAndLoser();
            if(user_names.size() == 0) {
                updateEmptyUserNames();
            }   else {
                updateExistingUserNames();
            }
            updateScoreWinner();
            storeUserNames();
            createGameEndedActivity();
        }
        else {
            updateViews();
        }
    }

    private void updateViews() {

        outputTextView.setText(capitalizeFirstLetterWord(game.word));
        letterEditText.setText("");

        if (!game.turn()) {
            turnTextView.setText(name_player1 + ", choose a letter!");
        } else {
            turnTextView.setText(name_player2 + ", choose a letter!");
        }
    }

    private void createGameEndedActivity() {
        Intent GameEndedActivity = new Intent(this, GameEndedActivity.class);
        GameEndedActivity.putExtra("winner", winner);
        GameEndedActivity.putExtra("loser", loser);
        GameEndedActivity.putExtra("word", capitalizeFirstLetterWord(game.word));
        GameEndedActivity.putExtra("isWord", dict.isWord(game.word));
        startActivity(GameEndedActivity);
        finish();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void storeUserNames() {
        for (int i = 0; i < user_names_array.length; i++) {
            user_names_returning.add(user_names_array[i] + "\n" + String.valueOf(wins_array[i]));
        }
        preferenceEditor.putStringSet("names", user_names_returning);
        preferenceEditor.commit();
    }

    private void updateScoreWinner() {
        for (int i = 0; i < user_names_array.length; i++) {
            if (winner.equals(user_names_array[i])) {
                wins_array[i]++;
                break;
            }
        }
    }

    private void updateEmptyUserNames() {
        user_names_array = new String[2];
        wins_array = new Integer[2];
        user_names_array[0] = name_player1;
        user_names_array[1] = name_player2;
        wins_array[0] = 0;
        wins_array[1] = 0;
    }

    private void updateExistingUserNames() {
        user_names_returning = new HashSet<>();
        user_names_array = new String[user_names.size()];
        wins_array = new Integer[user_names.size()];
        Iterator user_names_iterator = user_names.iterator();

        boolean exist_player1 = false;
        boolean exist_player2 = false;
        int iterator = 0;

        while(user_names_iterator.hasNext()) {
            String[] temp = ((String) user_names_iterator.next()).split("\n");
            user_names_array[iterator] = temp[0];
            try {
                wins_array[iterator] = Integer.parseInt(temp[1]);
            } catch (NumberFormatException nfe) {
                wins_array[iterator] = 0;
            }

            if(user_names_array[iterator].equals(name_player1)) {
                exist_player1 = true;
            }   else if(user_names_array[iterator].equals(name_player2)) {
                exist_player2 = true;
            }
            iterator++;
        }
        //userNames.clear();

        if (!exist_player1) {
            if (name_player1.equals(winner)) {
                user_names_returning.add(name_player1 +"\n1");
            } else {
                user_names_returning.add(name_player1 + "\n0");
            }

        }

        if(!exist_player2) {
            if (name_player2.equals(winner)) {
                user_names_returning.add(name_player2 +"\n1");
            } else {
                user_names_returning.add(name_player2 + "\n0");
            }
        }
    }

    private void setWinnerAndLoser() {
        if (!game.winner()) {
            winner = name_player1;
            loser = name_player2;

        } else {
            winner = name_player2;
            loser = name_player1;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void getUserNames() {
        user_names = new HashSet<>();
        user_names = preferenceSettings.getStringSet("names", user_names);
    }

    private void setTextViewsOnCreate() {
        if (!game.turn()) {
            turnTextView.setText(name_player1 + ", choose a letter!");
        } else {
            turnTextView.setText(name_player2 + ", choose a letter!");
        }
        String word = game.word;
        if (!word.equals("")) {
            outputTextView.setText(capitalizeFirstLetterWord(word));
        }
    }

    private String capitalizeFirstLetterWord(String word){
        char[] stringArray = word.toCharArray();
        stringArray[0] = Character.toUpperCase(stringArray[0]);
        return new String(stringArray);
    }

    private void pickRandomLetters() {
        int numberLetters = preferenceSettings.getInt("letters", 0);
        if(numberLetters > 0) {
            game.updateRandomLetters(numberLetters);
        }
    }

    private void readDictionaryAndCreateGame() {
        BufferedReader reader;
        dutch = preferenceSettings.getBoolean("dutch", false);
        if (dutch) {
            dictionaryTextView.setText("Dutch dictionary in use");
            reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.dutch)));
        }   else {
            reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.english)));
        }
        String line;
        dict = new Dictionary();
        try {
            while ((line = reader.readLine()) != null) {
                dict.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        game = new Game(dict);
    }

    private void updateGame(char letter) {
        game.guess(letter);
    }

    private boolean errorCheckingInput(String input) {
        if (input.length() != 1) {
            Toast.makeText(this, "You did not enter a letter", Toast.LENGTH_SHORT).show();
            return true;
        }

        char letter = input.charAt(0);

        if (!Character.isLetter(letter)) {
            Toast.makeText(this, "You did not enter a letter", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        preferenceEditor.putString("word", game.word);
        preferenceEditor.putString("player1", name_player1);
        preferenceEditor.putString("player2", name_player2);
        preferenceEditor.commit();
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
    }

    public void getPlayerNames() {
        Intent players = getIntent();
        name_player1 = players.getExtras().getString("player1");
        name_player2 = players.getExtras().getString("player2");

    }
}


