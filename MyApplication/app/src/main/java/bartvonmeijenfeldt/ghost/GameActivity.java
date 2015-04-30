package bartvonmeijenfeldt.ghost;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.String;
import android.widget.Toast;


public class GameActivity extends ActionBarActivity {

    String name_player1;
    String name_player2;
    private TextView outputTextView;
    private TextView nextLetterTextView;
    private TextView turnTextView;
    private EditText letterEditText;
    private Button okButton;
    Dictionary dict;
    Game game;
    boolean firstTurn = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        outputTextView = (TextView) findViewById(R.id.word_textView);
        nextLetterTextView = (TextView) findViewById(R.id.next_letter_textView);
        turnTextView = (TextView) findViewById(R.id.turn_textView);
        letterEditText = (EditText) findViewById(R.id.letter_editText);
        okButton = (Button) findViewById(R.id.ok_button);


        BufferedReader reader = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.dutch)));
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


        Intent names = getIntent();
        name_player1 = names.getExtras().getString("player1");
        name_player2 = names.getExtras().getString("player2");
        turnTextView.setText(name_player1 + ", choose a letter!");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);

    }

    public void click(View view) {



        if(!game.ended()) {

            // conditions for input
            String input = letterEditText.getText().toString().toLowerCase();
            if (input.length() != 1) {
                Toast.makeText(this, "You did not enter one letter", Toast.LENGTH_SHORT).show();
                return;
            }

            char letter = input.charAt(0);

            if (!Character.isLetter(letter)) {
                Toast.makeText(this, "You did not enter one letter", Toast.LENGTH_SHORT).show();
                return;
            }

            //update game
            game.guess(letter);

            if (game.ended()) {
                okButton.setText("GG, RE?");
                String word = game.word;
                if (game.winner()) {
                    turnTextView.setText(name_player2 +" won");
                } else {
                    turnTextView.setText(name_player1 + " won");
                }
                if (dict.isWord(word)) {
                    turnTextView.append(" (word)");
                }

            }

            else if (!game.turn()) {
                turnTextView.setText(name_player1 + ", choose a letter!");
            } else {
                turnTextView.setText(name_player2 + ", choose a letter!");
            }

            nextLetterTextView.setText("Next letter:");
            if (firstTurn) {
                outputTextView.append(String.valueOf(letter).toUpperCase());
                firstTurn = false;
            } else{
                outputTextView.append(String.valueOf(letter));
            }

        }

        // if game hasn't ended
        else {
            okButton.setText("OK");
            dict.reset();
            game = new Game(dict);
            outputTextView.setText("");
            turnTextView.setText(name_player1 + ", choose a letter!");
            nextLetterTextView.setText("Choose first letter:");
            firstTurn = true;
        }

        letterEditText.setText("");

    }

}


