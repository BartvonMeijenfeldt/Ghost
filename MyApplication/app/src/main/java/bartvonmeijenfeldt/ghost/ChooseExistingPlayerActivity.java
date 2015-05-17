package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ListAdapter;
import android.widget.ListView;

import android.widget.TextView;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Created by startklaar on 13-5-2015.
 */
public class ChooseExistingPlayerActivity extends ActionBarActivity {

    String player;
    TextView player_textView;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseexistingplayer);

        player_textView = (TextView) findViewById(R.id.player_textView);
        player = getIntent().getExtras().getString("player");
        player_textView.append(player);

        Set<String> user_names;
        Set<String> error_user_names = new HashSet<String>();
        error_user_names.add("No Existing\n0");
        error_user_names.add("Players\n0");

        preferenceSettings = getSharedPreferences("user_names",Context.MODE_PRIVATE);
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
        ListView user_names_listView = (ListView) findViewById(R.id.user_names_listView);
        user_names_listView.setAdapter(LA);

        user_names_listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                String username_picked = ((String.valueOf(parent.getItemAtPosition(position)).split("\n"))[0]);
                Intent goingBack = new Intent();
                goingBack.putExtra("player", player);
                goingBack.putExtra("username", username_picked);
                setResult(RESULT_OK, goingBack);
                finish();
            }
        });
    }
    public void ResetPlayers(View view) {
        preferenceEditor = preferenceSettings.edit();
        preferenceEditor.remove("names");
        preferenceEditor.commit();
    }
}
