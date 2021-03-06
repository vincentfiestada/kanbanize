package vcx.myapplication;
import android.app.FragmentManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REQ_INDEX = 0;
    public static final int DEV_INDEX = 1;
    public static final int TEST_INDEX = 2;
    public static final int DONE_INDEX = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Tasks
        new Tasks();

        // Initialize MQTT Client
        new MQTTClient(getApplicationContext(), getResources().getString(R.string.broker_uri), "bot".concat(new Date().toString()));

        /* --------------------------------------------
         * Get List Fragments and add tasks to adapters
         */
        FragmentManager fm = getFragmentManager();
        adapters = new ArrayList<>();
        // Column 1 (Requested)
        TaskListFragment col1 = (TaskListFragment) fm.findFragmentById(R.id.column1);
        adapters.add(REQ_INDEX, new TaskListAdapter(this, Tasks.getRequested(), Task.Status.REQUESTED));
        col1.setListAdapter(adapters.get(REQ_INDEX));
        col1.getListView().setOnItemLongClickListener(new LongItemClick());
        col1.getListView().setOnDragListener(new ItemDragToEmpty());
        // Column 2 (Development)
        TaskListFragment col2 = (TaskListFragment) fm.findFragmentById(R.id.column2);
        adapters.add(DEV_INDEX, new TaskListAdapter(this, Tasks.getDevelopment(), Task.Status.DEVELOPMENT));
        col2.setListAdapter(adapters.get(DEV_INDEX));
        col2.getListView().setOnItemLongClickListener(new LongItemClick());
        col2.getListView().setOnDragListener(new ItemDragToEmpty());
        // Column 3 (Testing)
        TaskListFragment col3 = (TaskListFragment) fm.findFragmentById(R.id.column3);
        adapters.add(TEST_INDEX, new TaskListAdapter(this, Tasks.getTesting(), Task.Status.TESTING));
        col3.setListAdapter(adapters.get(TEST_INDEX));
        col3.getListView().setOnItemLongClickListener(new LongItemClick());
        col3.getListView().setOnDragListener(new ItemDragToEmpty());
        // Column 4 (Done)
        TaskListFragment col4 = (TaskListFragment) fm.findFragmentById(R.id.column4);
        adapters.add(DONE_INDEX, new TaskListAdapter(this, Tasks.getDone(), Task.Status.DONE));
        col4.setListAdapter(adapters.get(DONE_INDEX));
        col4.getListView().setOnItemLongClickListener(new LongItemClick());
        col4.getListView().setOnDragListener(new ItemDragToEmpty());

    }

    public static ArrayList<TaskListAdapter> adapters;

    public static TaskListAdapter getAdapterByStatus(Task.Status status) {
        if (adapters != null) {
            switch(status) {
                case DEVELOPMENT:
                    return adapters.get(DEV_INDEX);
                case TESTING:
                    return adapters.get(TEST_INDEX);
                case DONE:
                    return adapters.get(DONE_INDEX);
                case REQUESTED:
                    return adapters.get(REQ_INDEX);
                default:
                    return null;
            }
        }
        else {
            return null;
        }
    }

    public static TaskListAdapter getAdapterByStatus(String status) {
        return getAdapterByStatus(Task.toStatus(status));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.board_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.new_task_button:
                newTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void newTask() {
        Intent i = new Intent(this, NewTaskActivity.class);
        startActivity(i);
    }
}
