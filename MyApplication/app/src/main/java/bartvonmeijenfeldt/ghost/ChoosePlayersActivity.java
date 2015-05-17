package bartvonmeijenfeldt.ghost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by startklaar on 30-4-2015.
 */
public class ChoosePlayersActivity extends ActionBarActivity {

    EditText player1EditText;
    EditText player2EditText;
    RadioButton english_radioButton;
    RadioButton dutch_radioButton;
    RadioButton zero_radioButton;
    RadioButton one_radioButton;
    RadioButton two_radioButton;
    RadioButton three_radioButton;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseplayers);
        player1EditText = (EditText) findViewById(R.id.player1_editText);
        player2EditText = (EditText) findViewById(R.id.player2_editText);
        english_radioButton = (RadioButton) findViewById(R.id.english_radioButton);
        dutch_radioButton = (RadioButton) findViewById(R.id.dutch_radioButton);
        zero_radioButton = (RadioButton) findViewById(R.id.zero_radioButton);
        one_radioButton = (RadioButton) findViewById(R.id.one_radioButton);
        two_radioButton = (RadioButton) findViewById(R.id.two_radioButton);
        three_radioButton = (RadioButton) findViewById(R.id.three_radioButton);

        preferenceSettings = getSharedPreferences("user_names", Context.MODE_PRIVATE);
        preferenceEditor = preferenceSettings.edit();

        Boolean dutch_chosen = preferenceSettings.getBoolean("dutch", false);
        if(dutch_chosen) {
            dutch_radioButton.setChecked(true);
        } else {
            english_radioButton.setChecked(true);
        }

        Integer letters = preferenceSettings.getInt("letters", 0);
        switch (letters) {
            case 0:
                zero_radioButton.setChecked(true);
                break;
            case 1:
                one_radioButton.setChecked(true);
                break;
            case 2:
                two_radioButton.setChecked(true);
                break;
            case 3:
                three_radioButton.setChecked(true);
                break;
        }

    }


    public void Start(View view) {

        Intent StartGame = new Intent(this,
                GameActivity.class);

        String user_name_1 = String.valueOf(player1EditText.getText());
        String user_name_2 = String.valueOf(player2EditText.getText());

        if (user_name_1.equals("") ) {
            Toast.makeText(this, "Player 1 needs a name", Toast.LENGTH_SHORT).show();
            return;
        } else if (user_name_1.length() > 10) {
            Toast.makeText(this, "Player 1's name is too long, max length is 10 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user_name_2.equals("")) {
            Toast.makeText(this, "Player 2 needs a name", Toast.LENGTH_SHORT).show();
            return;
        }   else if (user_name_2.length() > 10) {
            Toast.makeText(this, "Player 2's name is too long, max length is 10 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user_name_1.equals(user_name_2)) {
            Toast.makeText(this, "Player 1 and Player 2 need different names", Toast.LENGTH_SHORT).show();
            return;
        }
        StartGame.putExtra("player1", String.valueOf(player1EditText.getText()));
        StartGame.putExtra("player2", String.valueOf(player2EditText.getText()));
        startActivity(StartGame);
    }



    public void ExistingPlayers(View view) {

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

    public void englishClick(View view) {
        dutch_radioButton.setChecked(false);
        preferenceEditor.putBoolean("dutch", false);
        preferenceEditor.commit();
    }

    public void dutchClick(View view) {
        english_radioButton.setChecked(false);
        preferenceEditor.putBoolean("dutch", true);
        preferenceEditor.commit();
    }



    public void zeroClick(View view) {
        one_radioButton.setChecked(false);
        two_radioButton.setChecked(false);
        three_radioButton.setChecked(false);
        preferenceEditor.putInt("letters", 0);
        preferenceEditor.commit();

    }

    public void oneClick(View view) {
        zero_radioButton.setChecked(false);
        two_radioButton.setChecked(false);
        three_radioButton.setChecked(false);
        preferenceEditor.putInt("letters", 1);
        preferenceEditor.commit();
    }

    public void twoClick(View view) {
        zero_radioButton.setChecked(false);
        one_radioButton.setChecked(false);
        three_radioButton.setChecked(false);
        preferenceEditor.putInt("letters", 2);
        preferenceEditor.commit();
    }

    public void threeClick(View view) {
        zero_radioButton.setChecked(false);
        one_radioButton.setChecked(false);
        two_radioButton.setChecked(false);
        preferenceEditor.putInt("letters", 3);
        preferenceEditor.commit();
    }
}
