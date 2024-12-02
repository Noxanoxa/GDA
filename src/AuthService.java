import java.util.List;


public class AuthService {
    private JsonHandler JsonHandler;
    private static final String USERS_FILE = "users.json";

    private User currentUser;

    public AuthService(JsonHandler JsonHandler) {
        this.JsonHandler = JsonHandler;
    }


    public boolean register(String username, String password, String email, String role) {
        List<User> users = JsonHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        users.add(new User(username, password, email, "user"));
        JsonHandler.writeUsers(users);
        return true;
    }


    public boolean isUsernameUnique(String username) {
        List<User> users = JsonHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }


    public User login(String email, String password) {
        List<User> users = JsonHandler.readUsers();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                if ("admin".equals(user.getRole())) {
                    currentUser = new Admin(user.getUsername(), user.getPassword(), user.getEmail());
                } else {
                    currentUser = user;
                }
                return currentUser;
            }
        }
        return null;
    }


    public void logout() {
        currentUser = null;
    }


    public User getCurrentUser() {
        return currentUser;
    }

}