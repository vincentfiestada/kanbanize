package vcx.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class NewTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        Spinner userSelector = (Spinner) findViewById(R.id.task_user_select);
        if (userSelector != null) {
            userSelector.setAdapter(new UserListAdapter(this, Users.getUsers()));
        }
    }

    public void save(View v) {
        // Get parameters

        Log.d("save", "Saving");

        EditText taskNameEdit = (EditText) findViewById(R.id.task_name_edit);
        Spinner userSelector = (Spinner) findViewById(R.id.task_user_select);
        if (taskNameEdit != null && userSelector != null) {
            String taskName = taskNameEdit.getText().toString();
            User assignedUser = (User) userSelector.getSelectedItem();

            Tasks.add(new Task(taskName, assignedUser));
            TaskListAdapter req = MainActivity.getAdapterByStatus("REQUESTED");
            if (req != null) {
                req.notifyDataSetChanged();
            }

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }
    }
}
