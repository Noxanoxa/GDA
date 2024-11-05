import javax.swing.*;

public class LibraryManagementApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            XMLHandler xmlHandler = new XMLHandler();
            LibraryManagementView view = new LibraryManagementView();
            new LibraryManagementController(xmlHandler, view);
        });
    }
}