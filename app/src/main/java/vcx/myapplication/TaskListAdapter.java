package vcx.myapplication;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * List adapter meant to be used with TaskListFragment
 */
public class TaskListAdapter extends BaseAdapter {

    class TaskViewWrapper {
        TextView titleView;
        TextView userView;
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
    public View getView (int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.task_list_fragment, null);

            TaskViewWrapper taskView = new TaskViewWrapper();
            taskView.titleView = (TextView) v.findViewById (R.id.task_title_label);
            taskView.userView = (TextView) v.findViewById(R.id.task_user_label);
            v.setTag(taskView);
        }

        TaskViewWrapper taskView = (TaskViewWrapper) v.getTag();
        taskView.titleView.setText(tasks.get (position).getName());
        taskView.userView.setText(tasks.get(position).getUser().getName());

        v.setOnDragListener(new ItemDrag());

        return v;
    }

    public List<Task> getList() {
        return tasks;
    }
    public Task.Status getStatus() {
        return this.status;
    }
}