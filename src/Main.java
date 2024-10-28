import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
            try {
                new Main().createAndShowGUI();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        frame.setSize(300, 200);

        showLoginScreen(frame);
        frame.setVisible(true);
    }

    /**
     * Displays the login screen.
     * @param frame The main application frame.
     */
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
                JOptionPane.showMessageDialog(frame, "Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerButton.addActionListener(e -> showRegisterForm());
    }

    /**
     * Displays the registration form.
     */
    public void showRegisterForm() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(300, 200);
        registerFrame.setLayout(new GridLayout(4, 2));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[]{"user", "admin"});
        JButton registerButton = new JButton("Register");

        registerFrame.add(new JLabel("Email:"));
        registerFrame.add(emailField);
        registerFrame.add(new JLabel("Password:"));
        registerFrame.add(passwordField);
        registerFrame.add(new JLabel("Role:"));
        registerFrame.add(roleComboBox);
        registerFrame.add(new JLabel(""));
        registerFrame.add(registerButton);

        registerFrame.setVisible(true);

        registerButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();
            boolean success = authService.register(email, password, role);
            if (success) {
                JOptionPane.showMessageDialog(registerFrame, "Registration successful.", "Success", JOptionPane.INFORMATION_MESSAGE);
                registerFrame.dispose();
            } else {
                JOptionPane.showMessageDialog(registerFrame, "User already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    /**
     * Displays the admin management screen.
     * @param frame The main application frame.
     */
    public void showAdminManagementScreen(JFrame frame) {
        JPanel adminPane = new JPanel();
        adminPane.setLayout(new BoxLayout(adminPane, BoxLayout.Y_AXIS));
        JButton showAllProductsButton = new JButton("Show All Products");
        JButton showAllUsersButton = new JButton("Show All Users");
        JButton logoutButton = new JButton("Logout");

        adminPane.add(showAllProductsButton);
        adminPane.add(showAllUsersButton);
        adminPane.add(logoutButton);

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
        JPanel productPane = new JPanel();
        productPane.setLayout(new BoxLayout(productPane, BoxLayout.Y_AXIS));
        JButton addButton = new JButton("Add Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JTextField searchField = new JTextField();
        JButton searchButton = new JButton("Search");
        JButton backButton = new JButton("Back");
        JButton logoutButton = new JButton("Logout");

        productListModel = new DefaultListModel<>();
        productList = new JList<>(productListModel);
        JScrollPane productScrollPane = new JScrollPane(productList);

        productPane.add(productScrollPane);
        productPane.add(new JLabel("Search:"));
        productPane.add(searchField);
        productPane.add(searchButton);
        productPane.add(addButton);
        productPane.add(editButton);
        productPane.add(deleteButton);
        productPane.add(backButton);
        productPane.add(logoutButton);

        frame.getContentPane().removeAll();
        frame.add(productPane);
        frame.revalidate();
        frame.repaint();

        refreshProductList();

        addButton.addActionListener(e -> showAddForm());
        editButton.addActionListener(e -> {
            int selectedIndex = productList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedProduct = productListModel.getElementAt(selectedIndex);
                showEditForm(selectedProduct);
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        deleteButton.addActionListener(e -> {
            int selectedIndex = productList.getSelectedIndex();
            if (selectedIndex != -1) {
                String selectedProduct = productListModel.getElementAt(selectedIndex);
                String[] productDetails = selectedProduct.split(" - ");
                String productId = productDetails[0];
                productService.deleteProduct(productId);
                refreshProductList();
            } else {
                JOptionPane.showMessageDialog(frame, "Please select a product to delete.", "Error", JOptionPane.ERROR_MESSAGE);
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
        addFrame.setSize(300, 200);
        addFrame.setLayout(new GridLayout(3, 2));

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton saveButton = new JButton("Save");

        addFrame.add(new JLabel("Name:"));
        addFrame.add(nameField);
        addFrame.add(new JLabel("Price:"));
        addFrame.add(priceField);
        addFrame.add(new JLabel(""));
        addFrame.add(saveButton);

        addFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            String userId = authService.getCurrentUser().getUsername();
            Product newProduct = new Product(null, name, price, userId);
            productService.createProduct(newProduct);
            showAlert("Success", "Product added successfully.");
            refreshProductList();
            addFrame.dispose();
        });
    }

    /**
     * Displays the form to edit an existing product.
     * @param selectedProduct The selected product to edit.
     */
    private void showEditForm(String selectedProduct) {
        JFrame editFrame = new JFrame("Edit Product");
        editFrame.setSize(300, 200);
        editFrame.setLayout(new GridLayout(4, 2));

        String[] productDetails = selectedProduct.split(" - ");
        String productId = productDetails[0];
        String productName = productDetails[1];
        String productPrice = productDetails[2].substring(1);

        JTextField nameField = new JTextField(productName);
        JTextField priceField = new JTextField(productPrice);
        JButton saveButton = new JButton("Save");

        editFrame.add(new JLabel("Name:"));
        editFrame.add(nameField);
        editFrame.add(new JLabel("Price:"));
        editFrame.add(priceField);
        editFrame.add(new JLabel(""));
        editFrame.add(saveButton);

        editFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            String newName = nameField.getText();
            double newPrice = Double.parseDouble(priceField.getText());
            String userId = authService.getCurrentUser().getUsername();
            Product updatedProduct = new Product(productId, newName, newPrice, userId);
            productService.editProduct(updatedProduct);
            showAlert("Success", "Product updated successfully.");
            refreshProductList();
            editFrame.dispose();
        });
    }

    /**
     * Refreshes the product list displayed in the GUI.
     */
    private void refreshProductList() {
        productListModel.clear();
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            productListModel.addElement("No products added.");
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