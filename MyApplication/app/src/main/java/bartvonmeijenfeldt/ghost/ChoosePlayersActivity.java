package bartvonmeijenfeldt.ghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;


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

        //String previousActivity = getIntent().getExtras().getString("callingActivity");

        //if (previousActivity == "CreateNewAccount" || previousActivity == "ChooseExistingPlayer") {
        //    player = true;
        //}



        //TextView playerTextView = (TextView) findViewById(R.id.player_textView);

        //if (player) {
        //    playerTextView.append(" 2");
        //} else {
        //    playerTextView.append(" 1");
        //}


    }


    public void Start(View view) {

        Intent StartGame = new Intent(this,
                GameActivity.class);

        StartGame.putExtra("player1", String.valueOf(player1EditText.getText()));
        StartGame.putExtra("player2", String.valueOf(player2EditText.getText()));
        startActivity(StartGame);
    }
}
