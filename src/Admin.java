import java.util.List;

public class Admin extends User {
    public Admin(String username, String password, String email) {
        super(username, password, email,"admin");
    }


    public List<Product> showAllProducts(XMLHandler xmlHandler) {
        return xmlHandler.readProducts();
    }

    public List<User> showAllUsers(XMLHandler xmlHandler) {
        return xmlHandler.readUsers();
    }


    public void createUser(XMLHandler xmlHandler, User user) {
        List<User> users = xmlHandler.readUsers();
        users.add(user);
        xmlHandler.writeUsers(users);
    }


    public void editUser(XMLHandler xmlHandler, User user) {
        List<User> users = xmlHandler.readUsers();
        for (User u : users) {
            if (u.getUsername().equals(user.getUsername())) {
                u.setPassword(user.getPassword());
                u.setRole(user.getRole());
                break;
            }
        }
        xmlHandler.writeUsers(users);
    }

    public void deleteUser(XMLHandler xmlHandler, String username) {
        List<User> users = xmlHandler.readUsers();
        users.removeIf(u -> u.getUsername().equals(username));
        xmlHandler.writeUsers(users);

        List<Product> products = xmlHandler.readProducts();
        products.removeIf(p -> p.getUserId().equals(username));
        xmlHandler.writeProducts(products);
    }
}