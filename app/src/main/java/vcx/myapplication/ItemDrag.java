package vcx.myapplication;

import android.view.DragEvent;
import android.view.View;
import android.widget.ListView;
import android.util.Log;

/**
 * Handles an item being dragged (see TaskListFragment and TaskListAdapter)
 */
class ItemDrag implements View.OnDragListener {
    @Override
    public boolean onDrag (View v, DragEvent event) {

        Log.d("Drag", Integer.toString(event.getAction()));

        if (event.getAction() == DragEvent.ACTION_DROP) {
            Log.d("DragListener", "Dropping");
            // Parse state of drag event
            DragArgs passed = (DragArgs) event.getLocalState();
            Task draggedTask = passed.task;
            ListView sourceView = (ListView) passed.sourceView.getParent();
            TaskListAdapter src = (TaskListAdapter) sourceView.getAdapter();
            // Get destination view and list
            ListView destView = (ListView) v.getParent();
            TaskListAdapter dest = (TaskListAdapter) destView.getAdapter();
            // Remove Task from source and add to destination
            if (src.getList().remove(draggedTask)) {
                draggedTask.setStatus(dest.getStatus());
                dest.getList().add(draggedTask);
            }

            // Publish to MQTT broker
            MQTTClient.publishStatusChange(draggedTask.getId(), draggedTask.getStatus());

            // Make sure data binding is updated
            src.notifyDataSetChanged();
            dest.notifyDataSetChanged();
            // Scroll to position of new item (last)
            destView.smoothScrollToPosition(dest.getCount() - 1);

        }

        return true;
    }
}