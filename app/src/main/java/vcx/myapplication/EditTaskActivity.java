package vcx.myapplication;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class EditTaskActivity extends NewTaskActivity {

    public static final String ARG_TASK_ID = "greendale.edit.args.task.id";

    private Task target;

    @Override
    protected String getInitName() {

        if (target != null) {
            return target.getName();
        }

        Intent i = getIntent();
        int id = i.getIntExtra(ARG_TASK_ID, -1);
        if (id >= 0) {
            Task t = Tasks.getById(id);
            if (t != null) {
                target = t;
                return t.getName();
            }
        }
        return "";
    }

    @Override
    protected User getInitUser() {

        if (target != null) {
            return target.getUser();
        }

        Intent i = getIntent();
        int id = i.getIntExtra(ARG_TASK_ID, -1);
        if (id >= 0) {
            Task t = Tasks.getById(id);
            if (t != null) {
                target = t;
                return t.getUser();
            }
        }
        return Users.get(0);
    }

    @Override
    public void save(View v) {
        // Get parameters

        Log.d("save", "Saving");

        EditText taskNameEdit = (EditText) findViewById(R.id.task_name_edit);
        Spinner userSelector = (Spinner) findViewById(R.id.task_user_select);
        if (taskNameEdit != null && userSelector != null) {
            if (target != null) {
                String taskName = taskNameEdit.getText().toString();
                User assignedUser = (User) userSelector.getSelectedItem();

                target.setName(taskName);
                target.setUser(assignedUser);
                TaskListAdapter req = MainActivity.getAdapterByStatus(target.getStatus());
                if (req != null) {
                    MQTTClient.publishChanges(target);
                    req.notifyDataSetChanged();
                }

                finish();
            }
            else {
                super.save(v);
            }
        }
    }
}
