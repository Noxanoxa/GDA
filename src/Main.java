import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Main class to handle the GUI and main application logic.
 */
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

    /**
     * Initializes and displays the main GUI.
     */
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

    /**
     * Displays the login screen.
     * @param frame The main application frame.
     */
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

    /**
     * Displays the registration form.
     */
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

    /**
     * Displays the admin management screen.
     * @param frame The main application frame.
     */
    public void showAdminManagementScreen(JFrame frame) {
        JPanel adminPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton showAllProductsButton = new JButton("Show All Products");
        JButton showAllUsersButton = new JButton("Show All Users");
        JButton logoutButton = new JButton("Logout");

        showAllProductsButton.setPreferredSize(new Dimension(200, 30));
        showAllUsersButton.setPreferredSize(new Dimension(200, 30));
        logoutButton.setPreferredSize(new Dimension(200, 30));

        showAllProductsButton.setBackground(Color.DARK_GRAY);
        showAllProductsButton.setForeground(Color.WHITE);
        showAllUsersButton.setBackground(Color.DARK_GRAY);
        showAllUsersButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        showAllProductsButton.setFont(font);
        showAllUsersButton.setFont(font);
        logoutButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        adminPane.add(showAllProductsButton, gbc);

        gbc.gridy = 1;
        adminPane.add(showAllUsersButton, gbc);

        gbc.gridy = 2;
        adminPane.add(logoutButton, gbc);

        frame.getContentPane().removeAll();
        frame.add(adminPane);
        frame.revalidate();
        frame.repaint();

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);

        showAllProductsButton.addActionListener(e -> showAllProducts());
        showAllUsersButton.addActionListener(e -> showAllUsers());
        logoutButton.addActionListener(e -> {
            authService.logout();
            showLoginScreen(frame);
        });
    }

    /**
     * Displays all products for the admin.
     */
    private void showAllProducts() {
        List<Product> products = ((Admin) authService.getCurrentUser()).showAllProducts(new XMLHandler());
        productListModel.clear();
        for (Product product : products) {
            productListModel.addElement(product.getId() + " - " + product.getName() + " - $" + product.getPrice());
        }
        JOptionPane.showMessageDialog(null, new JScrollPane(productList), "All Products", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays all users for the admin.
     */
    private void showAllUsers() {
        List<User> users = ((Admin) authService.getCurrentUser()).showAllUsers(new XMLHandler());
        DefaultListModel<String> userListModel = new DefaultListModel<>();
        JList<String> userList = new JList<>(userListModel);
        for (User user : users) {
            userListModel.addElement(user.getUsername() + " - " + user.getRole());
        }
        JOptionPane.showMessageDialog(null, new JScrollPane(userList), "All Users", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Displays the product management screen.
     * @param frame The main application frame.
     */
    public void showProductManagementScreen(JFrame frame) {
        JPanel productPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        JButton logoutButton = new JButton("Logout");

        addButton.setPreferredSize(new Dimension(150, 30));
        editButton.setPreferredSize(new Dimension(150, 30));
        deleteButton.setPreferredSize(new Dimension(150, 30));
        searchField.setPreferredSize(new Dimension(200, 25));
        searchButton.setPreferredSize(new Dimension(100, 30));
        backButton.setPreferredSize(new Dimension(100, 30));
        logoutButton.setPreferredSize(new Dimension(100, 30));

        addButton.setBackground(Color.DARK_GRAY);
        addButton.setForeground(Color.WHITE);
        editButton.setBackground(Color.DARK_GRAY);
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.DARK_GRAY);
        deleteButton.setForeground(Color.WHITE);
        searchField.setBackground(Color.LIGHT_GRAY);
        searchButton.setBackground(Color.DARK_GRAY);
        searchButton.setForeground(Color.WHITE);
        backButton.setBackground(Color.DARK_GRAY);
        backButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        addButton.setFont(font);
        editButton.setFont(font);
        deleteButton.setFont(font);
        searchField.setFont(font);
        searchButton.setFont(font);
        backButton.setFont(font);
        logoutButton.setFont(font);

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        JScrollPane productScrollPane = new JScrollPane(productList);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        productPane.add(productScrollPane, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        productPane.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        productPane.add(searchField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        productPane.add(searchButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        productPane.add(addButton, gbc);
        gbc.gridx = 1;
        productPane.add(editButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        productPane.add(deleteButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        productPane.add(backButton, gbc);
        gbc.gridx = 1;
        productPane.add(logoutButton, gbc);

        frame.getContentPane().removeAll();
        frame.add(productPane);
        frame.revalidate();
        frame.repaint();

        refreshProductList();

        addButton.addActionListener(e -> showAddForm());
        editButton.addActionListener(e -> {
            String selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null) {
                showEditForm(selectedProduct);
            } else {
                showAlert("Error", "No product selected.");
            }
        });
        deleteButton.addActionListener(e -> {
            String selectedProduct = productList.getSelectedValue();
            if (selectedProduct != null) {
                productService.deleteProduct(selectedProduct.split(" - ")[0]);
                refreshProductList();
            } else {
                showAlert("Error", "No product selected.");
            }
        });
        searchButton.addActionListener(e -> searchProducts(searchField.getText()));
        backButton.addActionListener(e -> refreshProductList());
        logoutButton.addActionListener(e -> {
            authService.logout();
            showLoginScreen(frame);
        });
    }

    /**
     * Displays the form to add a new product.
     */
    private void showAddForm() {
        JFrame addFrame = new JFrame("Add Product");
        addFrame.setSize(400, 300);
        addFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton saveButton = new JButton("Save");

        nameField.setPreferredSize(new Dimension(200, 25));
        priceField.setPreferredSize(new Dimension(200, 25));
        saveButton.setPreferredSize(new Dimension(100, 30));

        nameField.setBackground(Color.LIGHT_GRAY);
        priceField.setBackground(Color.LIGHT_GRAY);
        saveButton.setBackground(Color.DARK_GRAY);
        saveButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(font);
        priceField.setFont(font);
        saveButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        addFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        addFrame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addFrame.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        addFrame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addFrame.add(saveButton, gbc);

        addFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            productService.createProduct(new Product(null, name, price, authService.getCurrentUser().getUsername()));
            addFrame.dispose();
            refreshProductList();
        });
    }

    /**
     * Displays the form to edit an existing product.
     * @param selectedProduct The selected product to edit.
     */
    private void showEditForm(String selectedProduct) {
        JFrame editFrame = new JFrame("Edit Product");
        editFrame.setSize(400, 300);
        editFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        String[] productDetails = selectedProduct.split(" - ");
        String productId = productDetails[0];
        String productName = productDetails[1];
        String productPrice = productDetails[2].substring(1);

        JTextField nameField = new JTextField(productName);
        JTextField priceField = new JTextField(productPrice);
        JButton saveButton = new JButton("Save");

        nameField.setPreferredSize(new Dimension(200, 25));
        priceField.setPreferredSize(new Dimension(200, 25));
        saveButton.setPreferredSize(new Dimension(100, 30));

        nameField.setBackground(Color.LIGHT_GRAY);
        priceField.setBackground(Color.LIGHT_GRAY);
        saveButton.setBackground(Color.DARK_GRAY);
        saveButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(font);
        priceField.setFont(font);
        saveButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        editFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        editFrame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editFrame.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        editFrame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editFrame.add(saveButton, gbc);

        editFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            productService.editProduct(new Product(productId, name, price, authService.getCurrentUser().getUsername()));
            editFrame.dispose();
            refreshProductList();
        });
    }

    /**
     * Refreshes the product list displayed in the GUI.
     */
    private void refreshProductList() {
        productListModel.clear();
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            productListModel.addElement("No products found.");
        } else {
            for (Product product : products) {
                productListModel.addElement(product.getId() + " - " + product.getName() + " - $" + product.getPrice());
            }
        }
    }

    /**
     * Searches for products based on the query and updates the product list.
     * @param query The search query.
     */
    private void searchProducts(String query) {
        productListModel.clear();
        List<Product> products = productService.searchProducts(query);
        if (products.isEmpty()) {
            productListModel.addElement("No products found.");
        } else {
            for (Product product : products) {
                productListModel.addElement(product.getId() + " - " + product.getName() + " - $" + product.getPrice());
            }
        }
    }

    /**
     * Displays an alert dialog with the specified title and message.
     * @param title The title of the alert.
     * @param message The message of the alert.
     */
    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}