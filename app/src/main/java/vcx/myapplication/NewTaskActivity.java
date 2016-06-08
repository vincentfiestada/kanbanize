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

        EditText taskNameEdit = (EditText) findViewById(R.id.task_name_edit);
        if (taskNameEdit != null) {
            taskNameEdit.setText(getInitName());
        }

        Spinner userSelector = (Spinner) findViewById(R.id.task_user_select);
        if (userSelector != null) {
            userSelector.setAdapter(new UserListAdapter(this, Users.getUsers()));
            // >>>>>>> Assume that UserListAdapter lists users in the same order
            // as the ids are assigned
            userSelector.setSelection(getInitUser().getId() - 1);
        }
    }

    protected String getInitName() {
        return "";
    }

    protected User getInitUser() {
        return Users.get(0);
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

            finish();
        }
    }
}
