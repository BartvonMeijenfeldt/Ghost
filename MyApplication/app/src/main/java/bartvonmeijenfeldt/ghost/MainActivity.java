package bartvonmeijenfeldt.ghost;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferenceSettings = getSharedPreferences("userNames", Context.MODE_PRIVATE);
        String word = preferenceSettings.getString("word", null);
        if(word != null) {
            Intent GameActivity = new Intent(this, GameActivity.class);
            GameActivity.putExtra("word", preferenceSettings.getString("word", null));
            GameActivity.putExtra("player1", preferenceSettings.getString("player1", null));
            GameActivity.putExtra("player2", preferenceSettings.getString("player2", null));
            startActivity(GameActivity);
        }
    }

    public void Play(View view) {
        Intent getNameScreenIntent = new Intent(this,
                ChoosePlayersActivity.class);
        startActivity(getNameScreenIntent);
    }

    public void activityHiScores(View view) {
        Intent hiScores = new Intent(this, HiScoreActivity.class);
        hiScores.putExtra("callingActivity", "MainActivity");
        startActivity(hiScores);
    }
}
