package vcx.myapplication;
import android.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Tasks
        new Tasks();

        // Initialize MQTT Client
        new MQTTClient(getApplicationContext(), getResources().getString(R.string.broker_uri), "bot");

        /* --------------------------------------------
         * Get List Fragments and add tasks to adapters
         */
        FragmentManager fm = getFragmentManager();
        //
        // Column 1 (Requested)
        TaskListFragment col1 = (TaskListFragment) fm.findFragmentById(R.id.column1);
        col1.setListAdapter(new TaskListAdapter(this, Tasks.getRequested(), Task.Status.REQUESTED));
        col1.getListView().setOnItemLongClickListener(new LongItemClick());
        // Column 2 (Development)
        TaskListFragment col2 = (TaskListFragment) fm.findFragmentById(R.id.column2);
        col2.setListAdapter(new TaskListAdapter(this, Tasks.getDevelopment(), Task.Status.DEVELOPMENT));
        col2.getListView().setOnItemLongClickListener(new LongItemClick());
        // Column 3 (Testing)
        TaskListFragment col3 = (TaskListFragment) fm.findFragmentById(R.id.column3);
        col3.setListAdapter(new TaskListAdapter(this, Tasks.getTesting(), Task.Status.TESTING));
        col3.getListView().setOnItemLongClickListener(new LongItemClick());
        // Column 4 (Done)
        TaskListFragment col4 = (TaskListFragment) fm.findFragmentById(R.id.column4);
        col4.setListAdapter(new TaskListAdapter(this, Tasks.getDone(), Task.Status.DONE));
        col4.getListView().setOnItemLongClickListener(new LongItemClick());

    }

    public static ArrayList<TaskListAdapter> adapters;
}
