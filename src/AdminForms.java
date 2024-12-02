import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AdminForms {
    private AuthService authService;
    private ProductService productService;
    private DefaultListModel<String> productListModel;
    private JList<String> productList;

    public AdminForms(AuthService authService, ProductService productService) {
        this.authService = authService;
        this.productService = productService;
    }

    public void showAdminManagementScreen(JFrame frame) {
        JPanel adminPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton showAllProductsButton = new JButton("Show All Products");
        JButton showAllUsersButton = new JButton("Show All Users");
        JButton createUserButton = new JButton("Create User");
        JButton editUserButton = new JButton("Edit User");
        JButton deleteUserButton = new JButton("Delete User");
        JButton logoutButton = new JButton("Logout");

        showAllProductsButton.setPreferredSize(new Dimension(200, 30));
        showAllUsersButton.setPreferredSize(new Dimension(200, 30));
        createUserButton.setPreferredSize(new Dimension(200, 30));
        editUserButton.setPreferredSize(new Dimension(200, 30));
        deleteUserButton.setPreferredSize(new Dimension(200, 30));
        logoutButton.setPreferredSize(new Dimension(200, 30));

        showAllProductsButton.setBackground(Color.DARK_GRAY);
        showAllProductsButton.setForeground(Color.WHITE);
        showAllUsersButton.setBackground(Color.DARK_GRAY);
        showAllUsersButton.setForeground(Color.WHITE);
        createUserButton.setBackground(Color.DARK_GRAY);
        createUserButton.setForeground(Color.WHITE);
        editUserButton.setBackground(Color.DARK_GRAY);
        editUserButton.setForeground(Color.WHITE);
        deleteUserButton.setBackground(Color.DARK_GRAY);
        deleteUserButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        showAllProductsButton.setFont(font);
        showAllUsersButton.setFont(font);
        createUserButton.setFont(font);
        editUserButton.setFont(font);
        deleteUserButton.setFont(font);
        logoutButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        adminPane.add(showAllProductsButton, gbc);

        gbc.gridy = 1;
        adminPane.add(showAllUsersButton, gbc);

        gbc.gridy = 2;
        adminPane.add(createUserButton, gbc);

        gbc.gridy = 3;
        adminPane.add(editUserButton, gbc);

        gbc.gridy = 4;
        adminPane.add(deleteUserButton, gbc);

        gbc.gridy = 5;
        adminPane.add(logoutButton, gbc);

        frame.getContentPane().removeAll();
        frame.add(adminPane);
        frame.revalidate();
        frame.repaint();

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);

        showAllProductsButton.addActionListener(e -> showAllProducts());
        showAllUsersButton.addActionListener(e -> showAllUsers());
        createUserButton.addActionListener(e -> showCreateUserForm());
        editUserButton.addActionListener(e -> showEditUserForm());
        deleteUserButton.addActionListener(e -> showDeleteUserForm());
        logoutButton.addActionListener(e -> {
            authService.logout();
            new UserForms(authService, productService).showLoginScreen(frame);
        });
    }

    private void showAllUsers() {
        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new JsonHandler());
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        for (User user : users) {
            userListModel.addElement(user.getUsername() + " - " + user.getRole());
        }
        JOptionPane.showMessageDialog(null, new JScrollPane(userList), "All Users", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCreateUserForm() {
        JFrame createUserFrame = new JFrame("Create User");
        createUserFrame.setSize(400, 300);
        createUserFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        JButton createButton = new JButton("Create");

        usernameField.setPreferredSize(new Dimension(200, 25));
        emailField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        createButton.setPreferredSize(new Dimension(100, 30));

        usernameField.setBackground(Color.LIGHT_GRAY);
        emailField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        createButton.setBackground(Color.DARK_GRAY);
        createButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(font);
        emailField.setFont(font);
        passwordField.setFont(font);
        createButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        createUserFrame.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createUserFrame.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        createUserFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        createUserFrame.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        createUserFrame.add(createButton, gbc);

        createUserFrame.setVisible(true);

        createButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            ((Admin) authService.getCurrentUser()).createUser(new JsonHandler(), new User(username, password,email,  role));
            createUserFrame.dispose();
        });
    }

    private void showEditUserForm() {
        JFrame editUserFrame = new JFrame("Edit User");
        editUserFrame.setSize(400, 300);
        editUserFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new JsonHandler());
        JComboBox<String> userComboBox = new JComboBox<>();
        for (User user : users) {
            userComboBox.addItem(user.getUsername());
        }

        JTextField usernameField = new JTextField();
        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        JButton editButton = new JButton("Edit");

        usernameField.setPreferredSize(new Dimension(200, 25));
        emailField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        editButton.setPreferredSize(new Dimension(100, 30));

        usernameField.setBackground(Color.LIGHT_GRAY);
        emailField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        editButton.setBackground(Color.DARK_GRAY);
        editButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(font);
        emailField.setFont(font);
        passwordField.setFont(font);
        editButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        editUserFrame.add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(userComboBox, gbc);


        gbc.gridx = 0;
        gbc.gridy = 1;
        editUserFrame.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editUserFrame.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editUserFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        editUserFrame.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editUserFrame.add(editButton, gbc);

        editUserFrame.setVisible(true);

        userComboBox.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            for (User user : users) {
                if (user.getUsername().equals(selectedUser)) {
                    usernameField.setText(user.getUsername());
                    emailField.setText(user.getEmail());
                    passwordField.setText(user.getPassword());
                    roleComboBox.setSelectedItem(user.getRole());
                    break;
                }
            }
        });

        editButton.addActionListener(e -> {
            String username = usernameField.getText();
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            ((Admin) authService.getCurrentUser()).editUser(new JsonHandler(), new User(username, password, email, role));
            showAlert("Success", "User edited successfully.");
            editUserFrame.dispose();
        });
    }

    private void showDeleteUserForm() {
        JFrame deleteUserFrame = new JFrame("Delete User");
        deleteUserFrame.setSize(400, 300);
        deleteUserFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new JsonHandler());
        JComboBox<String> userComboBox = new JComboBox<>();
        for (User user : users) {
            userComboBox.addItem(user.getUsername());
        }

        JButton deleteButton = new JButton("Delete");

        userComboBox.setPreferredSize(new Dimension(200, 25));
        deleteButton.setPreferredSize(new Dimension(100, 30));

        userComboBox.setBackground(Color.LIGHT_GRAY);
        deleteButton.setBackground(Color.DARK_GRAY);
        deleteButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        userComboBox.setFont(font);
        deleteButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteUserFrame.add(new JLabel("Select User:"), gbc);
        gbc.gridx = 1;
        deleteUserFrame.add(userComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deleteUserFrame.add(deleteButton, gbc);

        deleteUserFrame.setVisible(true);

        deleteButton.addActionListener(e -> {
            String username = (String) userComboBox.getSelectedItem();
            ((Admin) authService.getCurrentUser()).deleteUser(new JsonHandler(), username);
            showAlert("Success", "User deleted successfully.");
            deleteUserFrame.dispose();
        });
    }

    private void showAllProducts() {
        List<Product> products = ((Admin) authService.getCurrentUser()).showAllProducts(new JsonHandler());
        productListModel.clear();
        for (Product product : products) {
            productListModel.addElement(product.getId() + " - " + product.getName() + " - $" + product.getPrice() + " - " + product.getUserId());
        }
        JOptionPane.showMessageDialog(null, new JScrollPane(productList), "All Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}