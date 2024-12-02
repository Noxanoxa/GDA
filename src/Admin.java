import java.util.List;

public class Admin extends User {
    public Admin(String username, String password, String email) {
        super(username, password, email,"admin");
    }


    public List<Product> showAllProducts(JsonHandler jsonHandler) {
        return jsonHandler.readProducts();
    }

    public List<User> showAllUsers(JsonHandler jsonHandler) {
        return jsonHandler.readUsers();
    }


    public void createUser(JsonHandler jsonHandler, User user) {
        List<User> users = jsonHandler.readUsers();
        users.add(user);
        jsonHandler.writeUsers(users);
    }


    public void editUser(JsonHandler jsonHandler, User user) {
        List<User> users = jsonHandler.readUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setPassword(user.getPassword());
                u.setRole(user.getRole());
                break;
            }
        }
        jsonHandler.writeUsers(users);
    }

    public void deleteUser(JsonHandler jsonHandler, String username) {
        List<User> users = jsonHandler.readUsers();
        users.removeIf(u -> u.getUsername().equals(username));
        jsonHandler.writeUsers(users);

        List<Product> products = jsonHandler.readProducts();
        products.removeIf(p -> p.getUserId().equals(username));
        jsonHandler.writeProducts(products);
    }
}