package vcx.myapplication;

import java.util.ArrayList;

/**
 * Represents the pool of known users
 */
public class Users {

    public Users() {
        if (!initialized) {
            users = new ArrayList<>();
            // Add dummy users
            // TODO: Add from a database or lazily add from Tasks
            add(new User(1, "Jeff Winger"));
            add(new User(2, "Abed Nadir"));
            add(new User(3, "Annie Edison"));
            add(new User(4, "Troy Barnes"));
            add(new User(5, "Britta Perry"));
            add(new User(6, "Shirley Bennett"));
            add(new User(7, "Dean Craig Pelton"));
            initialized = true;
        }
    }


    private static boolean initialized = false;

    private static void ensureInit() {
        if (!initialized) {
            new Users();
        }
    }

    public static ArrayList<User> users;

    public static void add(User user) {
        users.add(user);
    }

    public static ArrayList<User> getUsers() {
        ensureInit();
        return users;
    }

    public static User get(int index) {
        ensureInit();
        return users.get(index);
    }

    public static User getById(int id) {
        ensureInit();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                return users.get(i);
            }
        }
        return null;
    }

    public static User getByName(String username) {
        ensureInit();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getName() == username) {
                return users.get(i);
            }
        }
        return null;
    }
}
