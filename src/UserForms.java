import javax.swing.*;
import java.awt.*;
import java.util.regex.Pattern;

public class UserForms {
    private AuthService authService;
    private ProductService productService;

    public UserForms(AuthService authService, ProductService productService) {
        this.authService = authService;
        this.productService = productService;
    }

    public void showLoginScreen(JFrame frame) {
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

        loginButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            User user = authService.login(email, password);
            if (user != null) {
                if (user instanceof Admin) {
                    new AdminForms(authService, productService).showAdminManagementScreen(frame);
                } else {
                    new ProductForms(authService, productService).showProductManagementScreen(frame);
                }
            } else {
                showAlert("Error", "Invalid credentials.");
            }
        });

        registerButton.addActionListener(e -> showRegisterForm());
    }

    public void showRegisterForm() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(400, 300);
        registerFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        usernameField.setPreferredSize(new Dimension(200, 25));
        emailField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        registerButton.setPreferredSize(new Dimension(100, 30));

        usernameField.setBackground(Color.LIGHT_GRAY);
        emailField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        registerButton.setBackground(Color.DARK_GRAY);
        registerButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(font);
        emailField.setFont(font);
        passwordField.setFont(font);
        registerButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerFrame.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        registerFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerFrame.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        registerFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        registerFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerFrame.add(registerButton, gbc);

        registerFrame.setVisible(true);

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());

            if (!isValidEmail(email)) {
                showAlert("Error", "Invalid email format.");
                return;
            }

            if (!authService.isUsernameUnique(username)) {
                showAlert("Error", "Username already exists.");
                return;
            }

            boolean success = authService.register(username, password,email,  "user");
            if (success) {
                showAlert("Success", "User registered successfully.");
                registerFrame.dispose();
            } else {
                showAlert("Error", "Registration failed.");
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}