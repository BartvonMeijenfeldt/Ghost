package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
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
    boolean firstTurn = true;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;
    Boolean dutch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        preferenceSettings = getSharedPreferences("user_names",Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();
        outputTextView = (TextView) findViewById(R.id.word_textView);
        dictionaryTextView = (TextView) findViewById(R.id.dictionary_textView);
        turnTextView = (TextView) findViewById(R.id.turn_textView);
        letterEditText = (EditText) findViewById(R.id.letter_editText);

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
        Intent players = getIntent();
        name_player1 = players.getExtras().getString("player1");
        name_player2 = players.getExtras().getString("player2");
        int letters = preferenceSettings.getInt("letters", 0);


        String word = "";
        if(letters > 0) {
            firstTurn = false;
            word = game.updateRandomLetters(letters);
        }
        if (!game.turn()) {
            turnTextView.setText(name_player1 + ", choose a letter!");
        } else {
            turnTextView.setText(name_player2 + ", choose a letter!");
        }

        if (!word.equals("")) {
            outputTextView.setText(word);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        /* waarom werkt dit niet?? hij returned blijkbaar nx bij findViewById, maar hoezo dan?
        MenuItem change_language = (MenuItem) findViewById(R.id.change_language);
        if (dutch) {
            change_language.setTitle("Change language to English");
        }   else {
            change_language.setTitle("Change language to Dutch");
        }
        */
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_language) {
            dutch = !dutch;
            preferenceEditor.putBoolean("dutch", dutch);
            preferenceEditor.commit();
        }

        //restarten zou logisch zijn via onCreate, maar lukt me niet
        Intent restart_game_activity = new Intent(this, GameActivity.class);
        restart_game_activity.putExtra("player1", name_player1);
        restart_game_activity.putExtra("player2", name_player2);
        startActivity(restart_game_activity);


        return super.onOptionsItemSelected(item);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void click(View view) {

        // conditions for input
        String input = letterEditText.getText().toString().toLowerCase();
        if (input.length() != 1) {
            Toast.makeText(this, "You did not enter a letter", Toast.LENGTH_SHORT).show();
            return;
        }

        char letter = input.charAt(0);

        if (!Character.isLetter(letter)) {
            Toast.makeText(this, "You did not enter a letter", Toast.LENGTH_SHORT).show();
            return;
        }

        //update game
        game.guess(letter);

        if (firstTurn) {
            outputTextView.setText(String.valueOf(letter).toUpperCase());
            firstTurn = false;
        } else{
            outputTextView.append(String.valueOf(letter));
        }

        letterEditText.setText("");

        if (game.ended()) {




            Set<String> user_names = new HashSet<String>();
            user_names = preferenceSettings.getStringSet("names", user_names);
            String[] user_names_array; ;
            Integer[] wins_array;

            String winner;
            String loser;

            if (!game.winner()) {
                winner = name_player1;
                loser = name_player2;

            } else {
                winner = name_player2;
                loser = name_player1;
            }

            // add players to user_names_array and wins_array
            if(user_names.size() == 0) {
                user_names_array = new String[2];
                wins_array = new Integer[2];
                user_names_array[0] = name_player1;
                user_names_array[1] = name_player2;
                wins_array[0] = 0;
                wins_array[1] = 0;

            }   else {
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
                user_names.clear();

                if (!exist_player1) {
                    if (name_player1.equals(winner)) {
                        user_names.add(name_player1 +"\n1");
                    } else {
                        user_names.add(name_player1 + "\n0");
                    }

                }

                if(!exist_player2) {
                    if (name_player2.equals(winner)) {
                        user_names.add(name_player2 +"\n1");
                    } else {
                        user_names.add(name_player2 + "\n0");
                    }
                }
            }

            //update score winner
            for (int i = 0; i < user_names_array.length; i++) {
                if (winner.equals(user_names_array[i])) {
                    wins_array[i]++;
                    break;
                }
            }

            //rewrite user_names and store
            for (int i = 0; i < user_names_array.length; i++) {
                user_names.add(user_names_array[i] + "\n" + String.valueOf(wins_array[i]));
            }
            preferenceEditor.putStringSet("names", user_names);
            preferenceEditor.commit();

            Intent GameEndedActivity = new Intent(this, GameEndedActivity.class);
            GameEndedActivity.putExtra("winner", winner);
            GameEndedActivity.putExtra("loser", loser);
            GameEndedActivity.putExtra("word", String.valueOf(outputTextView.getText()));
            GameEndedActivity.putExtra("isWord", dict.isWord(game.word));
            startActivity(GameEndedActivity);

        }

        else if (!game.turn()) {
            turnTextView.setText(name_player1 + ", choose a letter!");
        } else {
            turnTextView.setText(name_player2 + ", choose a letter!");
        }
    }

    @Override
    public void onBackPressed() {
    }
}


