import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AuthService {
    private XMLHandler xmlHandler;
    private User currentUser;

    public AuthService(XMLHandler xmlHandler) {
        this.xmlHandler = xmlHandler;
    }

    public boolean register(String username, String password) {
        List<User> users = xmlHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // User already exists
            }
        }
        users.add(new User(username, password));
        xmlHandler.writeUsers(users);
        return true;
    }

    public User login(String username, String password) {
        List<User> users = xmlHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return user;
            }
        }
        return null; // Invalid credentials
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void showLoginScreen(JFrame frame) {
        JPanel loginPane = new JPanel(new GridLayout(4, 2));
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginPane.add(new JLabel("Email:"));
        loginPane.add(emailField);
        loginPane.add(new JLabel("Password:"));
        loginPane.add(passwordField);
        loginPane.add(new JLabel(""));
        loginPane.add(loginButton);
        loginPane.add(new JLabel(""));
        loginPane.add(registerButton);

        frame.getContentPane().removeAll();
        frame.add(loginPane);
        frame.revalidate();
        frame.repaint();

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                User user = login(email, password);
                if (user != null) {
                    new Main().showProductManagementScreen(frame);
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Main().showRegisterForm();
            }
        });
    }
}