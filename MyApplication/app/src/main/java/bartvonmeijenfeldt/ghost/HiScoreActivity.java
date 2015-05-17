package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by startklaar on 15-5-2015.
 */
public class HiScoreActivity extends ActionBarActivity {

    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiscores);



        Set<String> user_names;
        Set<String> error_user_names = new HashSet<String>();
        error_user_names.add("No Existing\n0");
        error_user_names.add("Players\n0");

        preferenceSettings = getSharedPreferences("user_names", Context.MODE_PRIVATE);
        user_names = preferenceSettings.getStringSet("names", error_user_names);
        String[] user_names_and_wins = new String[user_names.size()];
        user_names.toArray(user_names_and_wins);
        Integer[] wins_array = new Integer[user_names_and_wins.length];

        for (int i = 0; i < user_names_and_wins.length; i++)
        {
            wins_array[i] = Integer.parseInt((user_names_and_wins[i]).split("\n")[1]);
        }

        int switches = 0;
        do {
            int i = wins_array.length - 1;
            switches = 0;

            while(i > 0) {
                if(wins_array[i] > wins_array[i-1]) {
                    Integer tempInt = wins_array[i];
                    wins_array[i] = wins_array[i - 1];
                    wins_array[i - 1] = tempInt;
                    String tempString = user_names_and_wins[i];
                    user_names_and_wins[i] = user_names_and_wins[i - 1];
                    user_names_and_wins[i - 1] = tempString;

                    switches++;
                }
                i--;
            }

        } while(switches > 0);

        ListAdapter LA = new UsersAdapter(this, user_names_and_wins);
        ListView user_names_listView = (ListView) findViewById(R.id.hi_scores_listView);
        user_names_listView.setAdapter(LA);

    }

    public void activity_main(View view) {

            Intent main_menu = new Intent(this, MainActivity.class);
            startActivity(main_menu);
    }

    @Override
    public void onBackPressed() {
        String callingActivity = getIntent().getExtras().getString("callingActivity");
        if (callingActivity.equals("MainActivity")) {
            super.onBackPressed();
        }
    }

}
