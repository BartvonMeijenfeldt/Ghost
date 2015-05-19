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
    Game game;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;
    Boolean dutch;
    Set<String> user_names = new HashSet<>();
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
        } else {
            pickRandomLetters();
        }
        setTextViewsOnCreate();
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


    public void ok(View view) {

        String input = letterEditText.getText().toString().toLowerCase();
        if(errorCheckingInput(input)){
            return;
        }

        updateGame(input.charAt(0));

        if (game.ended()) {
            getUserNames();
            setWinnerAndLoser();
            updateScoreWinner();
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
        GameEndedActivity.putExtra("isWord", game.lossByMadeWord());
        startActivity(GameEndedActivity);
        finish();
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateScoreWinner() {

        Set<String> user_names_returning = new HashSet<>();
        Iterator user_names_iterator = user_names.iterator();

        while(user_names_iterator.hasNext()) {
            String tempEntry = (String) user_names_iterator.next();

            String tempUserName = (tempEntry).split("\n")[0];
            if(tempUserName.equals(winner)) {
                Integer tempScore = Integer.parseInt(tempEntry.split("\n")[1]) + 1;
                user_names_returning.add(tempUserName +"\n" + tempScore);
            }   else {
                user_names_returning.add(tempEntry);
            }
        }

        preferenceEditor.putStringSet("names", user_names_returning);
        preferenceEditor.commit();
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
        int numberOfLetters = preferenceSettings.getInt("numberOfLetters", 0);
        if(numberOfLetters > 0) {
            game.updateRandomLetters(numberOfLetters);
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
        Dictionary dict = new Dictionary();
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

    private void recreateSavedGame(String word) {
        for(int i = 0; i < word.length(); i++) {
            game.guess(word.charAt(i));
        }
        preferenceEditor.remove("word");
        preferenceEditor.commit();
    }
}


