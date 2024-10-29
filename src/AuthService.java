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
    public boolean register(String username, String password, String role) {
        List<User> users = xmlHandler.readUsers();
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return false; // User already exists
            }
        }
        users.add(new User(username, password, role));
        xmlHandler.writeUsers(users);
        return true;
    }

    /**
     * Logs in a user.
     * @param username The username of the user.
     * @param password The password of the user.
     * @return The logged-in user, or null if credentials are invalid.
     */
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

    /**
     * Logs out the current user.
     */
    public void logout() {
        currentUser = null;
    }

    /**
     * Gets the current logged-in user.
     * @return The current user.
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Displays the login screen.
     * @param frame The main application frame.
     */
    /*public void showLoginScreen(JFrame frame) {
        JPanel loginPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        emailField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        loginButton.setPreferredSize(new Dimension(100, 30));
        registerButton.setPreferredSize(new Dimension(100, 30));

        emailField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        loginButton.setBackground(Color.DARK_GRAY);
        loginButton.setForeground(Color.WHITE);
        registerButton.setBackground(Color.DARK_GRAY);
        registerButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        emailField.setFont(font);
        passwordField.setFont(font);
        loginButton.setFont(font);
        registerButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPane.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        loginPane.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPane.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        loginPane.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPane.add(loginButton, gbc);

        gbc.gridy = 3;
        loginPane.add(registerButton, gbc);

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
                    if (user instanceof Admin) {
                        new AdminForms(AuthService.this, new ProductService(xmlHandler, AuthService.this)).showAdminManagementScreen(frame);
                    } else {
                        new ProductForms(AuthService.this, new ProductService(xmlHandler, AuthService.this)).showProductManagementScreen(frame);
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new UserForms(AuthService.this, new ProductService(xmlHandler, AuthService.this)).showRegisterForm();
            }
        });
    }*/
}