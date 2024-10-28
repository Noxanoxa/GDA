import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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

    public void createAndShowGUI() {
        XMLHandler xmlHandler = new XMLHandler();
        authService = new AuthService(xmlHandler);
        productService = new ProductService(xmlHandler, authService);

        JFrame frame = new JFrame("Product Management");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        // Login screen
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

        frame.add(loginPane);
        frame.setVisible(true);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                User user = authService.login(email, password);
                if (user != null) {
                    showProductManagementScreen(frame);
                } else {
                    showAlert("Error", "Invalid credentials.");
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRegisterForm();
            }
        });
    }

    public void showRegisterForm() {
        JFrame registerFrame = new JFrame("Register");
        registerFrame.setSize(300, 200);
        registerFrame.setLayout(new GridLayout(3, 2));

        JTextField emailField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");

        registerFrame.add(new JLabel("Email:"));
        registerFrame.add(emailField);
        registerFrame.add(new JLabel("Password:"));
        registerFrame.add(passwordField);
        registerFrame.add(new JLabel(""));
        registerFrame.add(registerButton);

        registerFrame.setVisible(true);

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                boolean success = authService.register(email, password);
                if (success) {
                    showAlert("Success", "User registered successfully.");
                    registerFrame.dispose();
                } else {
                    showAlert("Error", "User already exists.");
                }
            }
        });
    }

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

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddForm();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = productList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedProduct = productListModel.getElementAt(selectedIndex);
                    showEditForm(selectedProduct);
                } else {
                    showAlert("Error", "No product selected.");
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedIndex = productList.getSelectedIndex();
                if (selectedIndex != -1) {
                    String selectedProduct = productListModel.getElementAt(selectedIndex);
                    String productId = selectedProduct.split(" - ")[0];
                    productService.deleteProduct(productId);
                    refreshProductList();
                } else {
                    showAlert("Error", "No product selected.");
                }
            }
        });

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String query = searchField.getText();
                searchProducts(query);
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProductList();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authService.logout();
                authService.showLoginScreen(frame);
            }
        });
    }

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

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                String userId = authService.getCurrentUser().getUsername();
                Product newProduct = new Product(null, name, price, userId);
                productService.createProduct(newProduct);
                showAlert("Success", "Product added successfully.");
                refreshProductList();
                addFrame.dispose();
            }
        });
    }

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

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String newName = nameField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                String userId = authService.getCurrentUser().getUsername();
                Product updatedProduct = new Product(productId, newName, newPrice, userId);
                productService.editProduct(updatedProduct);
                showAlert("Success", "Product updated successfully.");
                refreshProductList();
                editFrame.dispose();
            }
        });
    }

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

    private void showAlert(String title, String message) {
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
}