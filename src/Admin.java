import java.util.List;

public class Admin extends User {
    public Admin(String username, String password) {
        super(username, password, "admin");
    }

    // Method to show all products
    public List<Product> showAllProducts(XMLHandler xmlHandler) {
        return xmlHandler.readProducts();
    }

    // Method to show all users
    public List<User> showAllUsers(XMLHandler xmlHandler) {
        return xmlHandler.readUsers();
    }

    // Additional admin-specific methods can be added here
}