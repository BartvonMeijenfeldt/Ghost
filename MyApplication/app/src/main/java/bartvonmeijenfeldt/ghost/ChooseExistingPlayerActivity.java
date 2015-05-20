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
import java.util.Set;

public class ChooseExistingPlayerActivity extends ActionBarActivity {

    String player;
    TextView playerTextView;
    SharedPreferences preferenceSettings;
    SharedPreferences.Editor preferenceEditor;



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseexistingplayer);

        playerTextView = (TextView) findViewById(R.id.player_textView);
        player = getIntent().getExtras().getString("player");
        playerTextView.append(player);

        Set<String> userNames;
        Set<String> errorUserNames = new HashSet<>();
        errorUserNames.add("No Existing\n0");
        errorUserNames.add("Players\n0");

        preferenceSettings = getSharedPreferences("userNames", Context.MODE_PRIVATE);
        userNames = preferenceSettings.getStringSet("names", errorUserNames);
        String[] userNamesAndWins = new String[userNames.size()];
        userNames.toArray(userNamesAndWins);
        Integer[] winsArray = new Integer[userNamesAndWins.length];

        for (int i = 0; i < userNamesAndWins.length; i++)
        {
            try {
                winsArray[i] = Integer.parseInt((userNamesAndWins[i]).split("\n")[1]);
            }   catch (NumberFormatException nfe) {
                winsArray[i] = 0;
            }
        }

        int switches;
        do {
            int i = winsArray.length - 1;
            switches = 0;

            while(i > 0) {
                if(winsArray[i] > winsArray[i-1]) {
                    Integer tempInt = winsArray[i];
                    winsArray[i] = winsArray[i - 1];
                    winsArray[i - 1] = tempInt;
                    String tempString = userNamesAndWins[i];
                    userNamesAndWins[i] = userNamesAndWins[i - 1];
                    userNamesAndWins[i - 1] = tempString;

                    switches++;
                }
                i--;
            }

        } while(switches > 0);

        ListAdapter LA = new UsersAdapter(this, userNamesAndWins);
        ListView userNamesListView = (ListView) findViewById(R.id.user_names_listView);
        userNamesListView.setAdapter(LA);

        userNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                String usernamePicked = ((String.valueOf(parent.getItemAtPosition(position)).split("\n"))[0]);
                Intent goingBack = new Intent();
                goingBack.putExtra("player", player);
                goingBack.putExtra("username", usernamePicked);
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
