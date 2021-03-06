package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChoosePlayersActivity extends ActionBarActivity {

    EditText player1EditText;
    EditText player2EditText;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseplayers);
        player1EditText = (EditText) findViewById(R.id.player1_editText);
        player2EditText = (EditText) findViewById(R.id.player2_editText);
        preferenceSettings = getSharedPreferences("userNames", Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        Boolean dutchChosen = preferenceSettings.getBoolean("dutch", false);
        if(dutchChosen) {
            setRadioButtonsLanguages(1);
        } else {
            setRadioButtonsLanguages(0);
        }

        Integer numberOfLetters = preferenceSettings.getInt("numberOfLetters", 0);
        setRadioButtonsLetters(numberOfLetters);
    }

    public void start(View view) {
        String userName1 = String.valueOf(player1EditText.getText());
        String userName2 = String.valueOf(player2EditText.getText());

        if(!(userNameLengthCheck(userName1, "Player 1")) || !(userNameLengthCheck(userName2, "Player 2"))) {
            return;
        }
        if (!userNamesDiffer(userName1, userName2)) {
            return;
        }

        Set<String> userNamesAndScores = new HashSet<>();
        userNamesAndScores = getUserNames(userNamesAndScores);
        String namePlayer1 = String.valueOf(player1EditText.getText());
        String namePlayer2 = String.valueOf(player2EditText.getText());

        if(userNamesAndScores.size() == 0) {
            updateEmptyUserNames(namePlayer1, namePlayer2);
        }   else {
            updateExistingUserNames(userNamesAndScores, namePlayer1, namePlayer2);
        }
        startGame(userName1, userName2);
    }

    private void startGame(String userName1, String userName2) {
        Intent StartGame = new Intent(this,
                GameActivity.class);
        StartGame.putExtra("player1", userName1);
        StartGame.putExtra("player2", userName2);
        startActivity(StartGame);
        finish();
    }

    private boolean userNamesDiffer(String userName1, String userName2) {
        if (userName1.equals(userName2)) {
            Toast.makeText(this, "Player 1 and Player 2 need different names", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private boolean userNameLengthCheck(String userName, String player) {
        if (userName.equals("") ) {
            Toast.makeText(this, player + " needs a name", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Set getUserNames(Set<String> userNames) {
        return preferenceSettings.getStringSet("names", userNames);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateEmptyUserNames(String namePlayer1, String namePlayer2) {

        Set<String> userNamesReturning = new HashSet<>();
        userNamesReturning.add(namePlayer1 + "\n0");
        userNamesReturning.add(namePlayer2 + "\n0");
        preferenceEditor.putStringSet("names", userNamesReturning);
        preferenceEditor.commit();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void updateExistingUserNames(Set userNamesAndScores, String namePlayer1, String namePlayer2) {
        List<Player> players = PlayerFunctions.players(userNamesAndScores);
        List<String> userNames = PlayerFunctions.getUserNamesOfPlayers(players);

        if(!userNames.contains(namePlayer1)){
            players.add(new Player(namePlayer1, 0));
        }
        if(!userNames.contains(namePlayer2)) {
            players.add(new Player(namePlayer2, 0));
        }

        preferenceEditor.putStringSet("names", PlayerFunctions.playersToHashSet(players));
        preferenceEditor.commit();
    }


    public void existingPlayers(View view) {
        if(!existingUserNames()) {
            Toast.makeText(this, "No existing players yet", Toast.LENGTH_SHORT).show();
            return;
        }
        createChooseExistingPlayersActivity(view);
    }

    private void createChooseExistingPlayersActivity(View view) {
        Intent getExistingNameScreenIntent = new Intent(this,
                ChooseExistingPlayerActivity.class);

        if (view.getId() == R.id.existing_player1_button) {
            getExistingNameScreenIntent.putExtra("player", "1");
        } else {
            getExistingNameScreenIntent.putExtra("player", "2");
        }
        final int result = 1;
        startActivityForResult(getExistingNameScreenIntent, result);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }

        String player = data.getStringExtra("player");
        String username = data.getStringExtra("username");

        if (player.equals("1")) {
            player1EditText.setText(username);
        }   else {
            player2EditText.setText(username);
        }

    }

    public void clickForLanguage(View view){
        int clicked = Integer.parseInt(view.getTag().toString());
        setRadioButtonsLanguages(clicked);
        if (clicked == 1) {
        preferenceEditor.putBoolean("dutch", true);
        } else {
            preferenceEditor.putBoolean("dutch", false);
        }
        preferenceEditor.commit();
    }

    private void setRadioButtonsLanguages(int clicked) {
        RadioButton[] radioButtons = new RadioButton[2];
        radioButtons[0] = (RadioButton) findViewById(R.id.english_radioButton);
        radioButtons[1] = (RadioButton) findViewById(R.id.dutch_radioButton);

        for (int i = 0; i < radioButtons.length; i++){
            if (i == clicked) {
                radioButtons[i].setChecked(true);
            }   else {
                radioButtons[i].setChecked(false);
            }
        }
    }

    public void clickForLetter(View view) {
        int clicked = Integer.parseInt(view.getTag().toString());
        setRadioButtonsLetters(clicked);
        preferenceEditor.putInt("numberOfLetters", clicked);
        preferenceEditor.commit();
    }

    private void setRadioButtonsLetters(int clicked) {

        RadioButton[] radioButtons = new RadioButton[4];
        radioButtons[0] = (RadioButton) findViewById(R.id.zero_radioButton);
        radioButtons[1] = (RadioButton) findViewById(R.id.one_radioButton);
        radioButtons[2] = (RadioButton) findViewById(R.id.two_radioButton);
        radioButtons[3] = (RadioButton) findViewById(R.id.three_radioButton);

        for (int i = 0; i < radioButtons.length; i++){
            if (i == clicked) {
                radioButtons[i].setChecked(true);
                }   else {
                radioButtons[i].setChecked(false);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private boolean existingUserNames() {
        SharedPreferences preferenceSettings = getSharedPreferences("userNames",Context.MODE_PRIVATE);
        Set<String> userNames = new HashSet<>();
        userNames = preferenceSettings.getStringSet("names", userNames);
        return userNames.size() != 0;
    }

}
