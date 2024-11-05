import javax.swing.*;

public class AuthorManagementApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            XMLHandler xmlHandler = new XMLHandler();
            AuthorManagementView view = new AuthorManagementView();
            new AuthorManagementController(xmlHandler, view);
        });
    }
}