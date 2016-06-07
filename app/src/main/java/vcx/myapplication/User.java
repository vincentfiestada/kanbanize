package vcx.myapplication;

/**
 * Represents a user that a task can be assigned to
 */
public class User {
    public User(int id, String name)
    {
        this.id = id;
        this.name = name;
    }

    private int id;
    private String name;

    /** -------------------
     * Getters and setters
     */
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }
}
