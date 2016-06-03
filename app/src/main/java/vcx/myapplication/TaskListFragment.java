package vcx.myapplication;

import android.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.util.Log;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskListFragment extends ListFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ArrayList<HashMap<String, String>> planets = new ArrayList<>();
        HashMap<String, String> jupiter = new HashMap<>();
        jupiter.put("name", "Jupiter");
        HashMap<String, String> saturn = new HashMap<>();
        saturn.put("name", "Saturn");
        HashMap<String, String> neptune = new HashMap<>();
        neptune.put("name", "Neptune");
        planets.add(jupiter);
        planets.add(saturn);
        planets.add(neptune);
        Log.d("v", planets.toString());
        String[] columns = new String[]{"name"};
        int[] columnViews = new int[]{R.id.task_title_label};
        this.setListAdapter(new SimpleAdapter(inflater.getContext(), planets, R.layout.task_list_fragment, columns, columnViews));
        // Let super class handle the grunt work
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
