package vcx.myapplication;

import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Represents a Kanban task
 */
public class Task {

    enum Status {
        REQUESTED,
        DEVELOPMENT,
        TESTING,
        DONE
    }

    public Task(String name, User user)
    {
        this.id = nextId;
        nextId++;
        this.name = name;
        this.assignedUser = user;
        this.status = Status.REQUESTED;
    }

    public Task(String name, User user, Status status)
    {
        this.id = nextId;
        nextId++;
        this.name = name;
        this.assignedUser = user;
        this.status = status;
    }

    public Task(int id, String name, User user)
    {
        this.id = id;
        this.name = name;
        this.assignedUser = user;
        this.status = Status.REQUESTED;
        nextId = id + 1;
    }

    public Task(int id, String name, User user, Status status)
    {
        this.id = id;
        this.name = name;
        this.assignedUser = user;
        this.status = status;
        nextId = id + 1;
    }

    private static int nextId = 0;
    private int id;
    private String name;
    private User assignedUser;
    private Status status;

    /**
     * ----------------------
     * Getters and Setters
     */

    public int getId() {
        return this.id;
    }

    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public User getUser()
    {
        return this.assignedUser;
    }

    public void setUser(User user) {
        this.assignedUser = user;
    }

    public Status getStatus() {
        return this.status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setStatus(String status) {
        switch(status) {
            case "REQUESTED":
                this.status = Status.REQUESTED;
            break;
            case "DEVELOPMENT":
                this.status = Status.DEVELOPMENT;
            break;
            case "TESTING":
                this.status = Status.TESTING;
            break;
            case "DONE":
                this.status = Status.DONE;
            break;
        }
    }

    public static Status toStatus(String status) {
        switch(status) {
            case "DEVELOPMENT":
                return Status.DEVELOPMENT;
            case "TESTING":
                return Status.TESTING;
            case "DONE":
                return Status.DONE;
            default:
                return Status.REQUESTED;
        }
    }

    public boolean equals(Task t) {
        return (id == t.getId());
    }

}
