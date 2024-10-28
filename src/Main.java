import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    private AuthService authService;
    private ProductService productService;
    private DefaultListModel<String> productListModel;
    private JList<String> productList;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main main = new Main();
            main.createAndShowGUI();
        });
    }

    public void createAndShowGUI() {
        XMLHandler xmlHandler = new XMLHandler();
        authService = new AuthService(xmlHandler);
        productService = new ProductService(xmlHandler, authService);

        JFrame frame = new JFrame("Product Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);

        showLoginScreen(frame);
        frame.setVisible(true);
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
                    showAdminManagementScreen(frame);
                } else {
                    showProductManagementScreen(frame);
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

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        JButton registerButton = new JButton("Register");

        emailField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        registerButton.setPreferredSize(new Dimension(100, 30));

        emailField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        registerButton.setBackground(Color.DARK_GRAY);
        registerButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        emailField.setFont(font);
        passwordField.setFont(font);
        registerButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        registerFrame.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        registerFrame.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        registerFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        registerFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        registerFrame.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        registerFrame.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        registerFrame.add(registerButton, gbc);

        registerFrame.setVisible(true);

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            if (authService.register(email, password, role)) {
                showAlert("Success", "Registration successful.");
                registerFrame.dispose();
            } else {
                showAlert("Error", "User already exists.");
            }
        });
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
            showLoginScreen(frame);
        });
    }

    private void showAllProducts() {
        List<Product> products = ((Admin) authService.getCurrentUser()).showAllProducts(new XMLHandler());
        productListModel.clear();
        for (Product product : products) {
            productListModel.addElement(product.getId() + " - " + product.getName() + " - $" + product.getPrice());
        }
        JOptionPane.showMessageDialog(null, new JScrollPane(productList), "All Products", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showAllUsers() {
        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new XMLHandler());
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
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        JButton createButton = new JButton("Create");

        usernameField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        createButton.setPreferredSize(new Dimension(100, 30));

        usernameField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        createButton.setBackground(Color.DARK_GRAY);
        createButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(font);
        passwordField.setFont(font);
        createButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        createUserFrame.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createUserFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        createUserFrame.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        createUserFrame.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        createUserFrame.add(createButton, gbc);

        createUserFrame.setVisible(true);

        createButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            ((Admin) authService.getCurrentUser()).createUser(new XMLHandler(), new User(username, password, role));
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

        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new XMLHandler());
        JComboBox<String> userComboBox = new JComboBox<>();
        for (User user : users) {
            userComboBox.addItem(user.getUsername());
        }

        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        JButton editButton = new JButton("Edit");

        usernameField.setPreferredSize(new Dimension(200, 25));
        passwordField.setPreferredSize(new Dimension(200, 25));
        editButton.setPreferredSize(new Dimension(100, 30));

        usernameField.setBackground(Color.LIGHT_GRAY);
        passwordField.setBackground(Color.LIGHT_GRAY);
        editButton.setBackground(Color.DARK_GRAY);
        editButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        usernameField.setFont(font);
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
        editUserFrame.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editUserFrame.add(new JLabel("Role:"), gbc);
        gbc.gridx = 1;
        editUserFrame.add(roleComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editUserFrame.add(editButton, gbc);

        editUserFrame.setVisible(true);

        userComboBox.addActionListener(e -> {
            String selectedUser = (String) userComboBox.getSelectedItem();
            for (User user : users) {
                if (user.getUsername().equals(selectedUser)) {
                    usernameField.setText(user.getUsername());
                    passwordField.setText(user.getPassword());
                    roleComboBox.setSelectedItem(user.getRole());
                    break;
                }
            }
        });

        editButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            ((Admin) authService.getCurrentUser()).editUser(new XMLHandler(), new User(username, password, role));
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

        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new XMLHandler());
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
            ((Admin) authService.getCurrentUser()).deleteUser(new XMLHandler(), username);
            showAlert("Success", "User deleted successfully.");
            deleteUserFrame.dispose();
        });
    }

    private void showAddForm() {
        JFrame addProductFrame = new JFrame("Add Product");
        addProductFrame.setSize(400, 300);
        addProductFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton addButton = new JButton("Add");

        nameField.setPreferredSize(new Dimension(200, 25));
        priceField.setPreferredSize(new Dimension(200, 25));
        addButton.setPreferredSize(new Dimension(100, 30));

        nameField.setBackground(Color.LIGHT_GRAY);
        priceField.setBackground(Color.LIGHT_GRAY);
        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(font);
        priceField.setFont(font);
        addButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addProductFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        addProductFrame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addProductFrame.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        addProductFrame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addProductFrame.add(addButton, gbc);

        addProductFrame.setVisible(true);

        addButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            Product product = new Product(null, name, price, authService.getCurrentUser().getUsername());
            productService.createProduct(product);
            addProductFrame.dispose();
            showProductManagementScreen(addProductFrame);
        });
    }

    private void showEditProductForm() {
        JFrame editProductFrame = new JFrame("Edit Product");
        editProductFrame.setSize(400, 300);
        editProductFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton editButton = new JButton("Edit");

        idField.setPreferredSize(new Dimension(200, 25));
        nameField.setPreferredSize(new Dimension(200, 25));
        priceField.setPreferredSize(new Dimension(200, 25));
        editButton.setPreferredSize(new Dimension(100, 30));

        idField.setBackground(Color.LIGHT_GRAY);
        nameField.setBackground(Color.LIGHT_GRAY);
        priceField.setBackground(Color.LIGHT_GRAY);
        editButton.setBackground(Color.DARK_GRAY);
        editButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        idField.setFont(font);
        nameField.setFont(font);
        priceField.setFont(font);
        editButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        editProductFrame.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        editProductFrame.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editProductFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        editProductFrame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editProductFrame.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        editProductFrame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editProductFrame.add(editButton, gbc);

        editProductFrame.setVisible(true);

        editButton.addActionListener(e -> {
            String id = idField.getText();
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            Product product = new Product(id, name, price, authService.getCurrentUser().getUsername());
            productService.editProduct(product);
            editProductFrame.dispose();
            showProductManagementScreen(editProductFrame);
        });
    }

    private void showDeleteProductForm() {
        JFrame deleteProductFrame = new JFrame("Delete Product");
        deleteProductFrame.setSize(400, 300);
        deleteProductFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField idField = new JTextField();
        JButton deleteButton = new JButton("Delete");

        idField.setPreferredSize(new Dimension(200, 25));
        deleteButton.setPreferredSize(new Dimension(100, 30));

        idField.setBackground(Color.LIGHT_GRAY);
        deleteButton.setBackground(Color.DARK_GRAY);
        deleteButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        idField.setFont(font);
        deleteButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteProductFrame.add(new JLabel("Product ID:"), gbc);
        gbc.gridx = 1;
        deleteProductFrame.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deleteProductFrame.add(deleteButton, gbc);

        deleteProductFrame.setVisible(true);

        deleteButton.addActionListener(e -> {
            String id = idField.getText();
            productService.deleteProduct(id);
            deleteProductFrame.dispose();
            showProductManagementScreen(deleteProductFrame);
        });
    }
    public void showProductManagementScreen(JFrame frame) {
        JPanel productPane = new JPanel(new BorderLayout());
        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        JScrollPane scrollPane = new JScrollPane(productList);

        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton logoutButton = new JButton("Logout");

        JPanel buttonPane = new JPanel();
        buttonPane.add(addButton);
        buttonPane.add(editButton);
        buttonPane.add(deleteButton);
        buttonPane.add(logoutButton);

        productPane.add(scrollPane, BorderLayout.CENTER);
        productPane.add(buttonPane, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(productPane);
        frame.revalidate();
        frame.repaint();

        addButton.addActionListener(e -> showAddForm());
        editButton.addActionListener(e -> showEditProductForm());
        deleteButton.addActionListener(e -> showDeleteProductForm());
        logoutButton.addActionListener(e -> {
            authService.logout();
            showLoginScreen(frame);
        });

        // Load products for the current user
        List<Product> products = productService.getAllProducts();
        productListModel.clear();
        for (Product product : products) {
            productListModel.addElement(product.getName() + " - $" + product.getPrice());
        }
    }
    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}