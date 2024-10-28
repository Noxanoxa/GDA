import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ProductService {
    private XMLHandler xmlHandler;
    private AuthService authService;

    public ProductService(XMLHandler xmlHandler, AuthService authService) {
        this.xmlHandler = xmlHandler;
        this.authService = authService;
    }

    public void createProduct(Product product) {
        product.setId(generateUniqueId());
        product.setUserId(authService.getCurrentUser().getUsername());
        List<Product> products = xmlHandler.readProducts();
        products.add(product);
        xmlHandler.writeProducts(products);
    }

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

    public void deleteProduct(String productId) {
        List<Product> products = xmlHandler.readProducts();
        products.removeIf(p -> p.getId().equals(productId));
        xmlHandler.writeProducts(products);
    }

    public List<Product> getAllProducts() {
        return xmlHandler.readProducts().stream()
                .filter(p -> p.getUserId().equals(authService.getCurrentUser().getUsername()))
                .collect(Collectors.toList());
    }

    public List<Product> searchProducts(String query) {
        return xmlHandler.readProducts().stream()
                .filter(p -> p.getUserId().equals(authService.getCurrentUser().getUsername()) &&
                        (p.getName().toLowerCase().contains(query.toLowerCase()) ||
                                String.valueOf(p.getPrice()).contains(query)))
                .collect(Collectors.toList());
    }

    private String generateUniqueId() {
        return UUID.randomUUID().toString();
    }
}