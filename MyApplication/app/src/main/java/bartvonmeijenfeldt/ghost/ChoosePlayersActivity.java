package bartvonmeijenfeldt.ghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

/**
 * Created by startklaar on 30-4-2015.
 */
public class ChoosePlayersActivity extends ActionBarActivity {

    //players 1 for false
    boolean player = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseplayers);

        String previousActivity = getIntent().getExtras().getString("callingActivity");

        if (previousActivity == "CreateNewAccount" || previousActivity == "ChooseExistingPlayer") {
            player = true;
        }



        TextView playerTextView = (TextView) findViewById(R.id.player_textView);

        if (player) {
            playerTextView.append(" 2");
        } else {
            playerTextView.append(" 1");
        }


    }


}
