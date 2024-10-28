import java.util.ArrayList;
import java.util.List;

public class User {
    private String username;
    private String password;
    private List<Product> products;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.products = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}