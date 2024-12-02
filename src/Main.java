import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
//            XMLHandler xmlHandler = new XMLHandler();
            JsonHandler jsonHandler = new JsonHandler();
            AuthService authService = new AuthService(jsonHandler);
            ProductService productService = new ProductService(jsonHandler, authService);
            JFrame frame = new JFrame("Product Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            new UserForms(authService, productService).showLoginScreen(frame);
            frame.setVisible(true);
        });
    }
}