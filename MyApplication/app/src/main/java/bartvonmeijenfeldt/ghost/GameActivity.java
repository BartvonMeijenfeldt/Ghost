package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import android.widget.Toast;


public class GameActivity extends ActionBarActivity {

    private TextView outputTextView;
    private TextView dictionaryTextView;
    private TextView turnTextView;
    private EditText letterEditText;
    private Game game;
    private SharedPreferences preferenceSettings;
    private SharedPreferences.Editor preferenceEditor;

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
            boolean dutch = !preferenceSettings.getBoolean("dutch", false);
            preferenceEditor.putBoolean("dutch", dutch);
            preferenceEditor.commit();
        }

        Intent restartGameActivity = new Intent(this, GameActivity.class);
        restartGameActivity.putExtra("player1", player1());
        restartGameActivity.putExtra("player2", player2());
        startActivity(restartGameActivity);
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
            gameEnded();
        }
        else {
            updateViews();
        }
    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(!game.ended()) {
            preferenceEditor.putString("word", game.word);
            preferenceEditor.putString("player1", player1());
            preferenceEditor.putString("player2", player2());
            preferenceEditor.commit();
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && !game.ended()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Exit game")
                    .setMessage("Do you want to quit the current game?")
                    .setNegativeButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            GameActivity.this.finish();
                        }
                    })
                    .setPositiveButton("no", null)
                    .show();
            return true;
        }
        else {
            return super.onKeyDown(keyCode, event);
        }
    }


    public void activityHiScores(View view) {
        Intent hiScores = new Intent(this, HiScoreActivity.class);
        startActivity(hiScores);
        finish();
    }

    public void rematch(View view) {
        setContentView(R.layout.activity_game);

        outputTextView = (TextView) findViewById(R.id.word_textView);
        dictionaryTextView = (TextView) findViewById(R.id.dictionary_textView);
        turnTextView = (TextView) findViewById(R.id.turn_textView);
        letterEditText = (EditText) findViewById(R.id.letter_editText);

        game.resetGame();
        pickRandomLetters();
        setTextViewsOnCreate();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(letterEditText, InputMethodManager.SHOW_IMPLICIT);
    }

    private void gameEnded() {
        updateScoreWinner();
        setGameEndedView();
    }

    public String player1() {
        Intent players = getIntent();
        return players.getExtras().getString("player1");
    }

    public String player2() {
        Intent players = getIntent();
        return players.getExtras().getString("player2");
    }

    private void recreateSavedGame(String word) {
        for(int i = 0; i < word.length(); i++) {
            game.guess(word.charAt(i));
        }
        preferenceEditor.remove("word");
        preferenceEditor.commit();
    }

    private void updateViews() {

        outputTextView.setText(capitalizeFirstLetterWord(game.word));
        letterEditText.setText("");

        if (!game.turn()) {
            turnTextView.setText(player1() + ", choose a letter!");
        } else {
            turnTextView.setText(player2() + ", choose a letter!");
        }
    }

    private void setGameEndedView() {
        setContentView(R.layout.activity_gameended);
        hideKeyBoard();
        setTextViewsOnGameEnded();
    }

    private void setTextViewsOnGameEnded() {
        TextView winnerTextView = (TextView) findViewById(R.id.winner_textView);
        TextView reasonTextView = (TextView) findViewById(R.id.reason_textView);
        TextView wordTextView = (TextView)findViewById(R.id.word_textView);

        winnerTextView.setText(winner() + " won!");
        if (game.lossByMadeWord()) {
            reasonTextView.setText(loser() + " made an existing word:");
        }   else {
            reasonTextView.setText(loser() + " added a letter which could never form an existing word:");
        }
        wordTextView.setText(capitalizeFirstLetterWord(game.word));
    }

    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(letterEditText.getWindowToken(), 0);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateScoreWinner() {

        List<Player> players = PlayerFunctions.players(userNames());
        for(Player player : players) {
            if(winner().equals(player.getName())) {
                player.incrementScoreOne();
                break;
            }
        }

        Set<String> userNamesReturning = PlayerFunctions.playersToHashSet(players);
        preferenceEditor.putStringSet("names", userNamesReturning);
        preferenceEditor.commit();
    }

    private String winner() {
        if (!game.winner()) {
            return player1();
        } else {
            return player2();
        }
    }

    private String loser() {
        if (game.winner()) {
            return player1();
        } else {
            return player2();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Set userNames() {
        Set<String> user_names = new HashSet<>();
        return preferenceSettings.getStringSet("names", user_names);
    }

    private void setTextViewsOnCreate() {
        if (!game.turn()) {
            turnTextView.setText(player1() + ", choose a letter!");
        } else {
            turnTextView.setText(player2() + ", choose a letter!");
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
        Dictionary dict;
        if(getIntent().hasExtra("dictionary")) {
            dict = (Dictionary) getIntent().getSerializableExtra("dictionary");
        } else {
            BufferedReader reader;
            boolean dutch = preferenceSettings.getBoolean("dutch", false);
            if (dutch) {
                dictionaryTextView.setText("Dutch dictionary in use");
                reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.dutch)));
            } else {
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

}


