import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class to handle product-related logic.
 */
public class ProductService {
    private XMLHandler xmlHandler;
    private AuthService authService;

    public ProductService(XMLHandler xmlHandler, AuthService authService) {
        this.xmlHandler = xmlHandler;
        this.authService = authService;
    }

    /**
     * Creates a new product.
     * @param product The product to create.
     */
    public void createProduct(Product product) {
        product.setId(generateUniqueId());
        product.setUserId(authService.getCurrentUser().getUsername());
        List<Product> products = xmlHandler.readProducts();
        products.add(product);
        xmlHandler.writeProducts(products);
    }

    /**
     * Edits an existing product.
     * @param product The product to edit.
     */
    public void editProduct(Product product) {
        List<Product> products = xmlHandler.readProducts();
        for (Product p : products) {
            if (p.getId().equals(product.getId())) {
                p.setName(product.getName());
                p.setPrice(product.getPrice());
                break;
            }
        }
        xmlHandler.writeProducts(products);
    }

    /**
     * Deletes a product by its ID.
     * @param productId The ID of the product to delete.
     */
    public void deleteProduct(String productId) {
        List<Product> products = xmlHandler.readProducts();
        products.removeIf(p -> p.getId().equals(productId));
        xmlHandler.writeProducts(products);
    }

    /**
     * Gets all products for the current user.
     * @return A list of products.
     */
    public List<Product> getAllProducts() {
        return xmlHandler.readProducts().stream()
                .filter(p -> p.getUserId().equals(authService.getCurrentUser().getUsername()))
                .collect(Collectors.toList());
    }

    /**
     * Searches for products based on a query.
     * @param query The search query.
     * @return A list of products matching the query.
     */
    public List<Product> searchProducts(String query) {
        return xmlHandler.readProducts().stream()
                .filter(p -> p.getUserId().equals(authService.getCurrentUser().getUsername()) &&
                        (p.getName().toLowerCase().contains(query.toLowerCase()) ||
                                String.valueOf(p.getPrice()).contains(query)))
                .collect(Collectors.toList());
    }



    /**
     * Generates a unique ID for a product.
     * @return A unique ID.
     */
    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}