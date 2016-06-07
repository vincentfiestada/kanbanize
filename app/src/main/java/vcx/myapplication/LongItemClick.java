package vcx.myapplication;

import android.content.ClipData;
import android.view.View;
import android.widget.AdapterView;

import android.util.Log;
import java.util.List;

/**
 * Listener for long item click in TaskListFragment
 */
public class LongItemClick implements AdapterView.OnItemLongClickListener {
    @Override
    public boolean onItemLongClick (AdapterView<?> parent, View view, int position, long id) {
        Task selectedTask = (Task) parent.getItemAtPosition (position);

        Log.d("LongItemClick", selectedTask.getName());

        DragArgs state = new DragArgs(selectedTask, view);

        ClipData data = ClipData.newPlainText("taskName", selectedTask.getName());
        View.DragShadowBuilder shadow = new View.DragShadowBuilder(view);
        view.startDrag (data, shadow, state, 0);

        return true;
    }
}