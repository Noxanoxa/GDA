import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Service class to handle authentication logic.
 */
public class AuthService {
    private XMLHandler xmlHandler;
    private User currentUser;

    public AuthService(XMLHandler xmlHandler) {
        this.xmlHandler = xmlHandler;
    }

    /**
     * Registers a new user.
     * @param username The username of the new user.
     * @param password The password of the new user.
     * @param role The role of the new user.
     * @return true if registration is successful, false if the user already exists.
     */
    public boolean register(String username, String password, String email, String role) {
        List<User> users = xmlHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // User already exists
            }
        }
        users.add(new User(username, password, email, "user"));
        xmlHandler.writeUsers(users);
        return true;
    }


    public boolean isUsernameUnique(String username) {
        List<User> users = xmlHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false;
            }
        }
        return true;
    }


    public User login(String email, String password) {
        List<User> users = xmlHandler.readUsers();
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
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