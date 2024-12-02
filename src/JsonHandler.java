import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.List;

public class JsonHandler {
    private ObjectMapper objectMapper;
    private static final String USERS_FILE = "users.json";
    private static final String PRODUCTS_FILE = "products.json";

    public JsonHandler() {
        objectMapper = new ObjectMapper();
    }

    public List<User> readUsers() {
        try {
            return objectMapper.readValue(new File(USERS_FILE), objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void writeUsers(List<User> users) {
        try {
            objectMapper.writeValue(new File(USERS_FILE), users);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Product> readProducts() {
        try {
            ProductsWrapper productsWrapper = objectMapper.readValue(new File(PRODUCTS_FILE), ProductsWrapper.class);
            return productsWrapper.getProducts();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void writeProducts(List<Product> products) {
        try {
            ProductsWrapper productsWrapper = new ProductsWrapper();
            productsWrapper.setProducts(products);
            objectMapper.writeValue(new File(PRODUCTS_FILE), productsWrapper);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}