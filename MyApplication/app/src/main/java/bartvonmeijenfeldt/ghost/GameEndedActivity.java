package bartvonmeijenfeldt.ghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by startklaar on 14-5-2015.
 */
public class GameEndedActivity extends ActionBarActivity{

    String winner;
    String loser;
    String word;
    Boolean isWord;
    TextView winner_textView;
    TextView reason_textView;
    TextView word_textView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameended);

        Intent names = getIntent();
        winner = names.getExtras().getString("winner");
        loser = names.getExtras().getString("loser");
        word = names.getExtras().getString("word");
        isWord = names.getExtras().getBoolean("isWord");

        winner_textView = (TextView) findViewById(R.id.winner_textView);
        reason_textView = (TextView) findViewById(R.id.reason_textView);
        word_textView = (TextView)findViewById(R.id.word_textView);

        winner_textView.setText(winner + " won!");
        if(isWord) {
            reason_textView.setText(loser + " made an existing word:");
        }   else {
            reason_textView.setText(loser + " added a letter which could never form an existing word:");
        }

        word_textView.setText(word);
    }

    public void activity_hi_scores(View view) {
        Intent hiScores = new Intent(this, HiScoreActivity.class);
        hiScores.putExtra("callingActivity", "GameEndedActivity");
        startActivity(hiScores);
    }

    public void activity_rematch(View view) {
        Intent rematch = new Intent(this, GameActivity.class);

        // should change this depending on who wins the most, player 1 or 2
        // I suspect beginning is better, therefore this order
        rematch.putExtra("player1", loser);
        rematch.putExtra("player2", winner);
        startActivity(rematch);

    }

    @Override
    public void onBackPressed() {
    }
}
