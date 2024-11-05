import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ProductForms {
    private AuthService authService;
    private ProductService productService;
    private JList<String> productList; // Declare productList


    public ProductForms(AuthService authService, ProductService productService) {
        this.authService = authService;
        this.productService = productService;
        this.productList = new JList<>(); // Initialize productList

    }

    public void showProductManagementScreen(JFrame frame) {
        JPanel productPane = new JPanel(new BorderLayout(10, 10));
        productPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a panel for the buttons
        JPanel buttonPane = new JPanel(new GridLayout(1, 5, 10, 0));

        JButton createButton = new JButton("Create Product");
        JButton editButton = new JButton("Edit Product");
        JButton deleteButton = new JButton("Delete Product");
        JButton logoutButton = new JButton("Logout");
        JButton refreshButton = new JButton("Refresh");
        JButton searchButton = new JButton("Search"); // Add search button


        createButton.setBackground(Color.DARK_GRAY);
        createButton.setForeground(Color.WHITE);
        editButton.setBackground(Color.DARK_GRAY);
        editButton.setForeground(Color.WHITE);
        deleteButton.setBackground(Color.DARK_GRAY);
        deleteButton.setForeground(Color.WHITE);
        logoutButton.setBackground(Color.DARK_GRAY);
        logoutButton.setForeground(Color.WHITE);
        refreshButton.setBackground(Color.DARK_GRAY);
        refreshButton.setForeground(Color.WHITE);
        searchButton.setBackground(Color.DARK_GRAY); // Set search button background
        searchButton.setForeground(Color.WHITE); // Set search button foreground


        Font font = new Font("Arial", Font.PLAIN, 14);
        createButton.setFont(font);
        editButton.setFont(font);
        deleteButton.setFont(font);
        logoutButton.setFont(font);
        refreshButton.setFont(font);
        searchButton.setFont(font); // Set search button font


        buttonPane.add(createButton);
        buttonPane.add(editButton);
        buttonPane.add(deleteButton);
        buttonPane.add(logoutButton);
        buttonPane.add(refreshButton);
        buttonPane.add(searchButton); // Add search button to panel


        // Add the product list to a scroll pane
        JScrollPane scrollPane = new JScrollPane(productList);
        scrollPane.setPreferredSize(new Dimension(400, 300));



        // Add components to the main panel
//        productPane.add(searchPane, BorderLayout.NORTH);
        productPane.add(scrollPane, BorderLayout.CENTER);
        productPane.add(buttonPane, BorderLayout.SOUTH);

        frame.getContentPane().removeAll();
        frame.add(productPane);
        frame.revalidate();
        frame.repaint();

        createButton.addActionListener(e -> showCreateProductForm());
        editButton.addActionListener(e -> showEditProductForm());
        deleteButton.addActionListener(e -> {
            showDeleteProductForm();
            loadProducts();
        });
        logoutButton.addActionListener(e -> {
            authService.logout();
            new UserForms(authService, productService).showLoginScreen(frame);
        });
        refreshButton.addActionListener(e -> loadProducts());
        searchButton.addActionListener(e -> showSearchForm());

        loadProducts();
    }

    private void showSearchForm() {
        JFrame searchFrame = new JFrame("Search Product");
        searchFrame.setSize(400, 200);
        searchFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JButton searchButton = new JButton("Search");

        nameField.setPreferredSize(new Dimension(200, 25));
        searchButton.setPreferredSize(new Dimension(100, 30));

        nameField.setBackground(Color.LIGHT_GRAY);
        searchButton.setBackground(Color.DARK_GRAY);
        searchButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(font);
        searchButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        searchFrame.add(new JLabel("Product Name:"), gbc);
        gbc.gridx = 1;
        searchFrame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        searchFrame.add(searchButton, gbc);

        searchFrame.setVisible(true);

        searchButton.addActionListener(e -> {
            String name = nameField.getText();
            List<Product> products = productService.searchProducts(name);
            DefaultListModel<String> productListModel = new DefaultListModel<>();
            for (Product product : products) {
                productListModel.addElement(product.getName() + " - $" + product.getPrice());
            }
            productList.setModel(productListModel);
            searchFrame.dispose();
        });
    }


    private void showCreateProductForm() {
        JFrame createProductFrame = new JFrame("Create Product");
        createProductFrame.setSize(400, 300);
        createProductFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton createButton = new JButton("Create");

        nameField.setPreferredSize(new Dimension(200, 25));
        priceField.setPreferredSize(new Dimension(200, 25));
        createButton.setPreferredSize(new Dimension(100, 30));

        nameField.setBackground(Color.LIGHT_GRAY);
        priceField.setBackground(Color.LIGHT_GRAY);
        createButton.setBackground(Color.DARK_GRAY);
        createButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(font);
        priceField.setFont(font);
        createButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        createProductFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        createProductFrame.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        createProductFrame.add(new JLabel("Price:"), gbc);
        gbc.gridx = 1;
        createProductFrame.add(priceField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        createProductFrame.add(createButton, gbc);

        createProductFrame.setVisible(true);

        createButton.addActionListener(e -> {
            String name = nameField.getText();
            double price = Double.parseDouble(priceField.getText());
            productService.createProduct(new Product(null, name, price, null));
            createProductFrame.dispose();
            loadProducts();
        });
    }

    private void showEditProductForm() {
        JFrame editProductFrame = new JFrame("Edit Product");
        editProductFrame.setSize(400, 300);
        editProductFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Product> products = productService.getAllProducts();
        JComboBox<String> productComboBox = new JComboBox<>();
        for (Product product : products) {
            productComboBox.addItem(product.getName());
        }

        JTextField nameField = new JTextField();
        JTextField priceField = new JTextField();
        JButton editButton = new JButton("Edit");

        nameField.setPreferredSize(new Dimension(200, 25));
        priceField.setPreferredSize(new Dimension(200, 25));
        editButton.setPreferredSize(new Dimension(100, 30));

        nameField.setBackground(Color.LIGHT_GRAY);
        priceField.setBackground(Color.LIGHT_GRAY);
        editButton.setBackground(Color.DARK_GRAY);
        editButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        nameField.setFont(font);
        priceField.setFont(font);
        editButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        editProductFrame.add(new JLabel("Select Product:"), gbc);
        gbc.gridx = 1;
        editProductFrame.add(productComboBox, gbc);

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

        productComboBox.addActionListener(e -> {
            String selectedProductName = (String) productComboBox.getSelectedItem();
            for (Product product : products) {
                if (product.getName().equals(selectedProductName)) {
                    nameField.setText(product.getName());
                    priceField.setText(String.valueOf(product.getPrice()));
                    break;
                }
            }
        });

        editButton.addActionListener(e -> {
            String selectedProductName = (String) productComboBox.getSelectedItem();
            Product selectedProduct = null;
            for (Product product : products) {
                if (product.getName().equals(selectedProductName)) {
                    selectedProduct = product;
                    break;
                }
            }
            if (selectedProduct != null) {
                String name = nameField.getText();
                double price = Double.parseDouble(priceField.getText());
                selectedProduct.setName(name);
                selectedProduct.setPrice(price);
                productService.editProduct(selectedProduct);
                editProductFrame.dispose();
                loadProducts();
            }
        });
    }

    private void showDeleteProductForm() {
        JFrame deleteProductFrame = new JFrame("Delete Product");
        deleteProductFrame.setSize(400, 300);
        deleteProductFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        List<Product> userProducts = productService.getAllProducts();
        JComboBox<String> productComboBox = new JComboBox<>();
        for (Product product : userProducts) {
            productComboBox.addItem(product.getName() + " - " + product.getId());
        }

        JButton deleteButton = new JButton("Delete");

        productComboBox.setPreferredSize(new Dimension(200, 25));
        deleteButton.setPreferredSize(new Dimension(100, 30));

        productComboBox.setBackground(Color.LIGHT_GRAY);
        deleteButton.setBackground(Color.DARK_GRAY);
        deleteButton.setForeground(Color.WHITE);

        Font font = new Font("Arial", Font.PLAIN, 14);
        productComboBox.setFont(font);
        deleteButton.setFont(font);

        gbc.gridx = 0;
        gbc.gridy = 0;
        deleteProductFrame.add(new JLabel("Select Product:"), gbc);
        gbc.gridx = 1;
        deleteProductFrame.add(productComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        deleteProductFrame.add(deleteButton, gbc);

        deleteProductFrame.setVisible(true);

        deleteButton.addActionListener(e -> {
            String selectedProduct = (String) productComboBox.getSelectedItem();
            if (selectedProduct != null) {
                String productId = selectedProduct.split(" - ")[1];
                productService.deleteProduct(productId);
                deleteProductFrame.dispose();
                loadProducts();
            }
        });
    }

    private void loadProducts() {
        loadProducts(null); // Call overloaded method with null query
    }

    private void loadProducts(String query) {
        List<Product> products = (query == null || query.isEmpty()) ? productService.getAllProducts() : productService.searchProducts(query);
        DefaultListModel<String> productListModel = new DefaultListModel<>();
        for (Product product : products) {
            productListModel.addElement(product.getName() + " - $" + product.getPrice());
        }
        productList.setModel(productListModel);
    }
}