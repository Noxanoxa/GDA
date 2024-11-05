import javax.swing.*;
import java.awt.*;

public class UserManagementView {
    private JFrame frame;
    private DefaultListModel<String> bookListModel;
    private JList<String> bookList;
    private DefaultListModel<String> borrowedListModel;
    private JList<String> borrowedList;
    private JButton borrowBookButton;
    private JButton returnBookButton;
    private JButton switchButton;

    public UserManagementView() {
        frame = new JFrame("User Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        panel.add(new JScrollPane(bookList), BorderLayout.CENTER);

        JPanel borrowedPanel = new JPanel(new BorderLayout());
        borrowedListModel = new DefaultListModel<>();
        borrowedList = new JList<>(borrowedListModel);
        borrowedPanel.add(new JScrollPane(borrowedList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        borrowBookButton = new JButton("Borrow Book");
        returnBookButton = new JButton("Return Book");
        switchButton = new JButton("Switch to Admin Interface");
        buttonPanel.add(borrowBookButton);
        buttonPanel.add(returnBookButton);
        buttonPanel.add(switchButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel, BorderLayout.CENTER);
        frame.add(borrowedPanel, BorderLayout.EAST);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public DefaultListModel<String> getBookListModel() {
        return bookListModel;
    }

    public JList<String> getBookList() {
        return bookList;
    }

    public DefaultListModel<String> getBorrowedListModel() {
        return borrowedListModel;
    }

    public JList<String> getBorrowedList() {
        return borrowedList;
    }

    public JButton getBorrowBookButton() {
        return borrowBookButton;
    }

    public JButton getReturnBookButton() {
        return returnBookButton;
    }

    public JButton getSwitchButton() {
        return switchButton;
    }
}