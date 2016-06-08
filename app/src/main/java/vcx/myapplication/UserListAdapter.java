package vcx.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * Created by vincent.fiestada on 6/8/2016.
 */
public class UserListAdapter extends BaseAdapter {

    class UserViewWrapper {
        TextView nameView;
        TextView idView;
    }

    public UserListAdapter(Context context, List<User> l) {
        this.context = context;
        this.users = l;
    }

    private Context context;
    private List<User> users;

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem (int position) {
        return users.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.user_list_fragment, parent, false);

            UserViewWrapper userView = new UserViewWrapper();
            userView.nameView = (TextView) v.findViewById(R.id.user_name_label);
            userView.idView = (TextView) v.findViewById(R.id.user_id_label);
            v.setTag(userView);
        }

        UserViewWrapper userView = (UserViewWrapper) v.getTag();
        userView.nameView.setText(users.get (position).getName());
        userView.idView.setText(String.format(Locale.ENGLISH, context.getString(R.string.user_id_label), users.get(position).getId()));

        return v;
    }
}
