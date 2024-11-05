import javax.swing.*;
import java.awt.*;

public class AuthorManagementView {
    private JFrame frame;
    private DefaultListModel<String> authorListModel;
    private JList<String> authorList;
    private JButton addAuthorButton;
    private JButton editAuthorButton;
    private JButton deleteAuthorButton;

    public AuthorManagementView() {
        frame = new JFrame("Author Management");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JPanel panel = new JPanel(new BorderLayout());
        authorListModel = new DefaultListModel<>();
        authorList = new JList<>(authorListModel);
        panel.add(new JScrollPane(authorList), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        addAuthorButton = new JButton("Add Author");
        editAuthorButton = new JButton("Edit Author");
        deleteAuthorButton = new JButton("Delete Author");
        buttonPanel.add(addAuthorButton);
        buttonPanel.add(editAuthorButton);
        buttonPanel.add(deleteAuthorButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setVisible(true);
    }

    public JFrame getFrame() {
        return frame;
    }

    public DefaultListModel<String> getAuthorListModel() {
        return authorListModel;
    }

    public JList<String> getAuthorList() {
        return authorList;
    }

    public JButton getAddAuthorButton() {
        return addAuthorButton;
    }

    public JButton getEditAuthorButton() {
        return editAuthorButton;
    }

    public JButton getDeleteAuthorButton() {
        return deleteAuthorButton;
    }
}