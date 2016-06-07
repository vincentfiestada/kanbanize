package vcx.myapplication;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Static class which represents the collection of tasks
 */
public class Tasks {

    public Tasks()
    {
        if (tasks_req == null)
        {
            tasks_req = new ArrayList<>();
            tasks_dev = new ArrayList<>();
            tasks_test = new ArrayList<>();
            tasks_done = new ArrayList<>();
        }
        // TODO: initiate from resource
        User jeff = new User(1, "Jeff Winger");
        User abed = new User(2, "Abed Nadir");
        User annie = new User(3, "Annie Edison");
        User troy = new User(4, "Troy Barnes");
        User britta = new User(4, "Britta Perry");
        add(new Task("Wake up", jeff));
        add(new Task("Take a bath", jeff));
        add(new Task("Watch Die Hard", abed, Task.Status.DEVELOPMENT));
        add(new Task("Study for class", annie, Task.Status.DEVELOPMENT));
        add(new Task("Protest oil spill", britta, Task.Status.TESTING));
        add(new Task("Play video games", troy, Task.Status.TESTING));
        add(new Task("Buy new backpack", annie, Task.Status.DONE));
        add(new Task("Watch Inspector SpaceTime", abed, Task.Status.DONE));
        add(new Task("Watch Inspector SpaceTime", troy, Task.Status.DEVELOPMENT));
        add(new Task("Come up with quippy remarks", jeff, Task.Status.TESTING));
    }

    private static ArrayList<Task> tasks_req;
    private static ArrayList<Task> tasks_dev;
    private static ArrayList<Task> tasks_test;
    private static ArrayList<Task> tasks_done;

    /**
     * Make sure lists of tasks have been initialized
     */
    private static void ensureTasks()
    {
        if (tasks_req == null)
        {
            new Tasks();
        }
    }

    public static List<Task> getRequested() {
        ensureTasks();
        return tasks_req;
    }

    public static List<Task> getDevelopment() {
        ensureTasks();
        return tasks_dev;
    }

    public static List<Task> getTesting() {
        ensureTasks();
        return tasks_test;
    }

    public static List<Task> getDone() {
        ensureTasks();
        return tasks_done;
    }

    public static boolean add(Task task)
    {
        ensureTasks();
        switch(task.getStatus()) {
            case REQUESTED:
                return tasks_req.add(task);
            case DEVELOPMENT:
                return tasks_dev.add(task);
            case TESTING:
                return tasks_test.add(task);
            case DONE:
                return tasks_done.add(task);
            default:
                return false;
        }
    }

}
