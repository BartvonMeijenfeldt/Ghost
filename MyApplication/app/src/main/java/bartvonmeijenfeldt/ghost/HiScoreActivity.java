package bartvonmeijenfeldt.ghost;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.HashSet;
import java.util.Set;

public class HiScoreActivity extends ActionBarActivity {

    SharedPreferences preferenceSettings;
    String[] userNamesAndWins;
    Set<String> userNames;
    Integer[] winsArray;
    Set<String> errorUserNames = new HashSet<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiscores);

        getUserNames();
        orderUserNames();
        inflateMenu();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void getUserNames() {
        errorUserNames.add("No Existing\n0");
        errorUserNames.add("Players\n0");
        preferenceSettings = getSharedPreferences("userNames", Context.MODE_PRIVATE);
        userNames = preferenceSettings.getStringSet("names", errorUserNames);
        userNamesAndWins = new String[userNames.size()];
        userNames.toArray(userNamesAndWins);
        winsArray = new Integer[userNamesAndWins.length];

        for (int i = 0; i < userNamesAndWins.length; i++)
        {
            try {
                winsArray[i] = Integer.parseInt((userNamesAndWins[i]).split("\n")[1]);
            }   catch (NumberFormatException nfe) {
                winsArray[i] = 0;
            }
        }
    }

    private void orderUserNames() {
        // keeps moving up the array and switching userNames and their scores if a userName higher
        // up in the array has a lower score than the one below, stops if no userNames were switched
        // for a iteration.
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
    }

    private void inflateMenu() {
        ListAdapter LA = new UsersAdapter(this, userNamesAndWins);
        ListView userNamesListView = (ListView) findViewById(R.id.hi_scores_listView);
        userNamesListView.setAdapter(LA);
    }

    public void activityMain(View view) {
        onBackPressed();
    }


}
