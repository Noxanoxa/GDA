import javax.swing.*;
import java.awt.*;

public class LibraryManagementView {
    private JFrame frame;
    private DefaultListModel<String> bookListModel;
    private JList<String> bookList;
    private DefaultComboBoxModel<String> authorComboBoxModel;
    private JButton addBookButton;
    private JButton editBookButton;
    private JButton deleteBookButton;
    private JButton manageAuthorsButton;

    public LibraryManagementView() {
        frame = new JFrame("Library Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel panel = new JPanel(new BorderLayout());
        bookListModel = new DefaultListModel<>();
        bookList = new JList<>(bookListModel);
        panel.add(new JScrollPane(bookList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addBookButton = new JButton("Add Book");
        editBookButton = new JButton("Edit Book");
        deleteBookButton = new JButton("Delete Book");
        manageAuthorsButton = new JButton("Manage Authors");
        buttonPanel.add(addBookButton);
        buttonPanel.add(editBookButton);
        buttonPanel.add(deleteBookButton);
        buttonPanel.add(manageAuthorsButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
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

    public DefaultComboBoxModel<String> getAuthorComboBoxModel() {
        return authorComboBoxModel;
    }

    public void setAuthorComboBoxModel(DefaultComboBoxModel<String> authorComboBoxModel) {
        this.authorComboBoxModel = authorComboBoxModel;
    }

    public JButton getAddBookButton() {
        return addBookButton;
    }

    public JButton getEditBookButton() {
        return editBookButton;
    }

    public JButton getDeleteBookButton() {
        return deleteBookButton;
    }

    public JButton getManageAuthorsButton() {
        return manageAuthorsButton;
    }
}