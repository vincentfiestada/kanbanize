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

    public Task(long id, String name, User user)
    {
        this.id = id;
        this.name = name;
        this.assignedUser = user;
        this.status = Status.REQUESTED;
    }

    public Task(long id, String name, User user, Status status)
    {
        this.id = id;
        this.name = name;
        this.assignedUser = user;
        this.status = status;
    }

    private static long nextId = 0;
    private long id;
    private String name;
    private User assignedUser;
    private Status status;

    /**
     * ----------------------
     * Getters and Setters
     */

    public long getId() {
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

    public boolean equals(Task t) {
        return (id == t.getId());
    }
}
