package vcx.myapplication;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

/**
 * List adapter meant to be used with TaskListFragment
 */
public class TaskListAdapter extends BaseAdapter {

    class TaskViewWrapper {
        TextView titleView;
        TextView userView;
        TextView idView;
    }

    TaskListAdapter(Context context, List<Task> l, Task.Status status) {
        this.context = context;
        this.tasks = l;
        this.status = status;
    }

    private Context context;
    private List<Task> tasks;
    private Task.Status status;

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem (int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId (int position) {
        return position;
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.task_list_fragment, parent, false);

            TaskViewWrapper taskView = new TaskViewWrapper();
            taskView.titleView = (TextView) v.findViewById (R.id.task_title_label);
            taskView.userView = (TextView) v.findViewById(R.id.task_user_label);
            taskView.idView = (TextView) v.findViewById(R.id.task_id_label);
            v.setTag(taskView);
        }

        TaskViewWrapper taskView = (TaskViewWrapper) v.getTag();
        taskView.titleView.setText(tasks.get (position).getName());
        taskView.userView.setText(tasks.get(position).getUser().getName());
        taskView.idView.setText(String.format(Locale.ENGLISH, "%1$d", tasks.get(position).getId()));

        final View x = v;

        (v.findViewById(R.id.move_task_button)).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View button) {
                Log.d("d", "Clicked");

                Task selectedTask = tasks.get(position);

                DragArgs state = new DragArgs(selectedTask, x);

                ClipData data = ClipData.newPlainText("taskName", selectedTask.getName());
                View.DragShadowBuilder shadow = new View.DragShadowBuilder(x);
                x.startDrag (data, shadow, state, 0);

                return true;
            }
        });

        (v.findViewById(R.id.remove_task_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Task t = tasks.remove(position);
                if (t != null) {
                    MQTTClient.publishRemoveTask(t);
                    notifyDataSetChanged();
                }
            }
        });
        (v.findViewById(R.id.edit_task_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                Intent i = new Intent(context.getApplicationContext(), EditTaskActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra(EditTaskActivity.ARG_TASK_ID, tasks.get(position).getId());
                context.getApplicationContext().startActivity(i);
            }
        });
        v.setOnDragListener(new ItemDrag());

        return v;
    }

    public Task removeById(int id) {
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getId() == id) {
                return tasks.remove(i);
            }
        }
        return null;
    }

    public List<Task> getList() {
        return tasks;
    }
    public Task.Status getStatus() {
        return this.status;
    }
}