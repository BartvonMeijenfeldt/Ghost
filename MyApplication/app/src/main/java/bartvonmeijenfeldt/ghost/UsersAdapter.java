package bartvonmeijenfeldt.ghost;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


class UsersAdapter extends ArrayAdapter<String> {
    public UsersAdapter(Context context,  String[] user_scores){
        super(context, R.layout.adapter_users, user_scores);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater theInflater = LayoutInflater.from(getContext());
        View theView = theInflater.inflate(R.layout.adapter_users, parent, false);
        String[] user_score = (getItem(position)).split("\n");
        TextView user_textView = (TextView) theView.findViewById(R.id.user_textView);
        user_textView.setText(user_score[0]);
        TextView score_textView = (TextView) theView.findViewById(R.id.score_textView);
        score_textView.setText(user_score[1]);
        return theView;

    }
}
