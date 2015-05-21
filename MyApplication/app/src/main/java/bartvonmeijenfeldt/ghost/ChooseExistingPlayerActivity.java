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
import android.widget.Toast;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ChooseExistingPlayerActivity extends ActionBarActivity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chooseexistingplayer);

        TextView playerTextView = (TextView) findViewById(R.id.player_textView);
        playerTextView.append(player());

        List<Player> players = PlayerFunctions.players(userNames());
        Collections.sort(players);
        Collections.reverse(players);
        inflateMenu(players);
    }

    private void inflateMenu( List<Player> players) {
        ListAdapter LA = new UsersAdapter(this, PlayerFunctions.userNamesAndWins(players));
        ListView userNamesListView = (ListView) findViewById(R.id.user_names_listView);
        userNamesListView.setAdapter(LA);

        userNamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                String usernamePicked = ((String.valueOf(parent.getItemAtPosition(position)).split("\n"))[0]);
                Intent goingBack = new Intent();
                goingBack.putExtra("player", player());
                goingBack.putExtra("username", usernamePicked);
                setResult(RESULT_OK, goingBack);
                finish();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private Set<String> userNames() {
        SharedPreferences preferenceSettings = getSharedPreferences("userNames", Context.MODE_PRIVATE);
        return preferenceSettings.getStringSet("names", PlayerFunctions.errorUserNames());
    }

    private String player() {
        return getIntent().getExtras().getString("player");
    }

    public void deletePlayers(View view) {
        SharedPreferences.Editor preferenceEditor = getSharedPreferences("userNames", Context.MODE_PRIVATE).edit();
        preferenceEditor.remove("names");
        preferenceEditor.commit();
        Toast.makeText(this, "Player names deleted", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }
}
