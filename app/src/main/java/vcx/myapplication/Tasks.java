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
        if (!tasksLoaded) {

            new Users();
            // Get users
            User jeff = Users.get(0);
            User abed = Users.get(1);
            User annie = Users.get(2);
            User troy = Users.get(3);
            User britta = Users.get(4);
            User shirley = Users.get(5);
            User dean = Users.get(6);

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
            add(new Task("Buy Dalmatian poster", dean));
            add(new Task("Handle latest budget cuts", dean));
            add(new Task("Issue diplomas to 456 students", dean));
            add(new Task("Make pastries", shirley, Task.Status.DEVELOPMENT));
            add(new Task("Start a sandwich shop", shirley, Task.Status.DONE));
            add(new Task("Ruin things", britta, Task.Status.DONE));
            add(new Task("Celebrate Star Wars Day", abed, Task.Status.DEVELOPMENT));
            add(new Task("Repair the Dreamatorium", troy, Task.Status.TESTING));
            tasksLoaded = true; // Don't reload next time
        }
    }

    public static ArrayList<Task> tasks_req;
    public static ArrayList<Task> tasks_dev;
    public static ArrayList<Task> tasks_test;
    public static ArrayList<Task> tasks_done;
    private static boolean tasksLoaded = false;

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

    public static List<Task> getListByStatus(String status) {
        switch (status) {
            case "DEVELOPMENT":
                return tasks_dev;
            case "TESTING":
                return tasks_test;
            case "DONE":
                return tasks_done;
            default:
                return tasks_req;
        }
    }

    public static boolean add(Task task)
    {
        ensureTasks();
        boolean result = false;
        switch(task.getStatus()) {
            case REQUESTED:
                result = tasks_req.add(task);
            break;
            case DEVELOPMENT:
                result = tasks_dev.add(task);
            break;
            case TESTING:
                result = tasks_test.add(task);
            break;
            case DONE:
                result = tasks_done.add(task);
            break;
        }
        if (result) { // Notify about new task
            MQTTClient.publishNewTask(task);
        }
        return result;
    }

}
