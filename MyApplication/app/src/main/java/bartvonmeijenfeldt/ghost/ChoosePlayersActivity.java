package bartvonmeijenfeldt.ghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by startklaar on 30-4-2015.
 */
public class ChoosePlayersActivity extends ActionBarActivity {

    EditText player1EditText;
    EditText player2EditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseplayers);
        player1EditText = (EditText) findViewById(R.id.player1_editText);
        player2EditText = (EditText) findViewById(R.id.player2_editText);
    }


    public void Start(View view) {

        Intent StartGame = new Intent(this,
                GameActivity.class);

        String user_name_1 = String.valueOf(player1EditText.getText());
        String user_name_2 = String.valueOf(player2EditText.getText());

        if (user_name_1.equals("") ) {
            Toast.makeText(this, "Player 1 needs a name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user_name_2.equals("")) {
            Toast.makeText(this, "Player 2 needs a name", Toast.LENGTH_SHORT).show();
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
}
