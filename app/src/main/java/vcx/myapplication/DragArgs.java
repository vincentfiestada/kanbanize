package vcx.myapplication;

import java.util.List;
import android.view.View;

/**
 * Holds state information about a Task being dragged
 */
public class DragArgs {

    public DragArgs(Task task, View srcView)
    {
        this.task = task;
        this.sourceView = srcView;
    }

    public View sourceView;
    public Task task;
}
