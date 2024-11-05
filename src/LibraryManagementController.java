import javax.swing.*;
import java.awt.*;
import java.util.List;

public class LibraryManagementController {
    private XMLHandler xmlHandler;
    private LibraryManagementView view;

    public LibraryManagementController(XMLHandler xmlHandler, LibraryManagementView view) {
        this.xmlHandler = xmlHandler;
        this.view = view;

        loadBooks();
        loadAuthors();

        view.getAddBookButton().addActionListener(e -> showAddBookForm());
        view.getEditBookButton().addActionListener(e -> showEditBookForm());
        view.getDeleteBookButton().addActionListener(e -> showDeleteBookForm());
        view.getManageAuthorsButton().addActionListener(e -> showAuthorManagementForm());
    }

    private void loadBooks() {
        List<Livre> livres = xmlHandler.readLivres();
        DefaultListModel<String> bookListModel = view.getBookListModel();
        bookListModel.clear();
        for (Livre livre : livres) {
            bookListModel.addElement(livre.getTitre() + " - " + livre.getIsbn());
        }
    }

    private void loadAuthors() {
        List<Auteur> auteurs = xmlHandler.readAuteurs();
        DefaultComboBoxModel<String> authorComboBoxModel = new DefaultComboBoxModel<>();
        for (Auteur auteur : auteurs) {
            authorComboBoxModel.addElement(auteur.getNom());
        }
        view.setAuthorComboBoxModel(authorComboBoxModel);
    }

    private void showAddBookForm() {
        JFrame addBookFrame = new JFrame("Add Book");
        addBookFrame.setSize(400, 300);
        addBookFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField titreField = new JTextField();
        JTextField isbnField = new JTextField();
        JTextField datePublicationField = new JTextField();
        JComboBox<String> authorComboBox = new JComboBox<>(view.getAuthorComboBoxModel());
        JButton addButton = new JButton("Add");

        titreField.setPreferredSize(new Dimension(200, 25));
        isbnField.setPreferredSize(new Dimension(200, 25));
        datePublicationField.setPreferredSize(new Dimension(200, 25));
        addButton.setPreferredSize(new Dimension(100, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        addBookFrame.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        addBookFrame.add(titreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addBookFrame.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        addBookFrame.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        addBookFrame.add(new JLabel("Publication Date:"), gbc);
        gbc.gridx = 1;
        addBookFrame.add(datePublicationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        addBookFrame.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        addBookFrame.add(authorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addBookFrame.add(addButton, gbc);

        addBookFrame.setVisible(true);

        addButton.addActionListener(e -> {
            String titre = titreField.getText();
            String isbn = isbnField.getText();
            String datePublication = datePublicationField.getText();
            int idAuteur = view.getAuthorComboBoxModel().getIndexOf(authorComboBox.getSelectedItem()) + 1;
            List<Livre> livres = xmlHandler.readLivres();
            livres.add(new Livre(titre, isbn, datePublication, idAuteur));
            xmlHandler.writeLivres(livres);
            loadBooks();
            addBookFrame.dispose();
        });
    }

    private void showEditBookForm() {
        int selectedIndex = view.getBookList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book to edit.");
            return;
        }

        List<Livre> livres = xmlHandler.readLivres();
        Livre selectedLivre = livres.get(selectedIndex);

        JFrame editBookFrame = new JFrame("Edit Book");
        editBookFrame.setSize(400, 300);
        editBookFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField titreField = new JTextField(selectedLivre.getTitre());
        JTextField isbnField = new JTextField(selectedLivre.getIsbn());
        JTextField datePublicationField = new JTextField(selectedLivre.getDatePublication());
        JComboBox<String> authorComboBox = new JComboBox<>(view.getAuthorComboBoxModel());
        authorComboBox.setSelectedIndex(selectedLivre.getIdAuteur() - 1);
        JButton saveButton = new JButton("Save");

        titreField.setPreferredSize(new Dimension(200, 25));
        isbnField.setPreferredSize(new Dimension(200, 25));
        datePublicationField.setPreferredSize(new Dimension(200, 25));
        saveButton.setPreferredSize(new Dimension(100, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        editBookFrame.add(new JLabel("Title:"), gbc);
        gbc.gridx = 1;
        editBookFrame.add(titreField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editBookFrame.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        editBookFrame.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        editBookFrame.add(new JLabel("Publication Date:"), gbc);
        gbc.gridx = 1;
        editBookFrame.add(datePublicationField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        editBookFrame.add(new JLabel("Author:"), gbc);
        gbc.gridx = 1;
        editBookFrame.add(authorComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editBookFrame.add(saveButton, gbc);

        editBookFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            selectedLivre.setTitre(titreField.getText());
            selectedLivre.setIsbn(isbnField.getText());
            selectedLivre.setDatePublication(datePublicationField.getText());
            selectedLivre.setIdAuteur(view.getAuthorComboBoxModel().getIndexOf(authorComboBox.getSelectedItem()) + 1);
            xmlHandler.writeLivres(livres);
            loadBooks();
            editBookFrame.dispose();
        });
    }

    private void showDeleteBookForm() {
        int selectedIndex = view.getBookList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this book?", "Delete Book", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Livre> livres = xmlHandler.readLivres();
            livres.remove(selectedIndex);
            xmlHandler.writeLivres(livres);
            loadBooks();
        }
    }

    private void showAuthorManagementForm() {
        SwingUtilities.invokeLater(() -> {
            XMLHandler xmlHandler = new XMLHandler();
            AuthorManagementView view = new AuthorManagementView();
            new AuthorManagementController(xmlHandler, view);
        });
    }
}