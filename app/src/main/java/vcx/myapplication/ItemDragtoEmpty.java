package vcx.myapplication;

import android.view.View;
import android.widget.ListView;

/**
 * Handles an item being dragged to an empty part of the ListView (see TaskListFragment and TaskListAdapter)
 * Should be set as the OnDragListener of a ListView
 */
class ItemDragToEmpty extends ItemDrag {
    @Override
    protected ListView getDestView(View v) {
        return (ListView) v;
    }
}