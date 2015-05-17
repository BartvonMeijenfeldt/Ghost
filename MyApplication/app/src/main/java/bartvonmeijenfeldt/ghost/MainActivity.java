package bartvonmeijenfeldt.ghost;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

/**
 * Created by startklaar on 30-4-2015.
 */
public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void Play(View view) {

        Intent getNameScreenIntent = new Intent(this,
                ChoosePlayersActivity.class);

        startActivity(getNameScreenIntent);
    }

    public void activity_hi_scores(View view) {
        Intent hiScores = new Intent(this, HiScoreActivity.class);
        hiScores.putExtra("callingActivity", "MainActivity");
        startActivity(hiScores);
    }

    @Override
    public void onBackPressed(){
    }
}
