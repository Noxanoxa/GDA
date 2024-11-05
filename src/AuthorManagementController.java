import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AuthorManagementController {
    private XMLHandler xmlHandler;
    private AuthorManagementView view;

    public AuthorManagementController(XMLHandler xmlHandler, AuthorManagementView view) {
        this.xmlHandler = xmlHandler;
        this.view = view;

        loadAuthors();

        view.getAddAuthorButton().addActionListener(e -> showAddAuthorForm());
        view.getEditAuthorButton().addActionListener(e -> showEditAuthorForm());
        view.getDeleteAuthorButton().addActionListener(e -> showDeleteAuthorForm());
    }

    private void loadAuthors() {
        List<Auteur> auteurs = xmlHandler.readAuteurs();
        DefaultListModel<String> authorListModel = view.getAuthorListModel();
        authorListModel.clear();
        for (Auteur auteur : auteurs) {
            authorListModel.addElement(auteur.getNom() + " - " + auteur.getNationalite());
        }
    }

    private void showAddAuthorForm() {
        JFrame addAuthorFrame = new JFrame("Add Author");
        addAuthorFrame.setSize(350, 200);
        addAuthorFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nomField = new JTextField();
        JTextField nationaliteField = new JTextField();
        JButton addButton = new JButton("Add");

        nomField.setPreferredSize(new Dimension(200, 25));
        nationaliteField.setPreferredSize(new Dimension(200, 25));
        addButton.setPreferredSize(new Dimension(100, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        addAuthorFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        addAuthorFrame.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        addAuthorFrame.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1;
        addAuthorFrame.add(nationaliteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        addAuthorFrame.add(addButton, gbc);

        addAuthorFrame.setVisible(true);

        addButton.addActionListener(e -> {
            String nom = nomField.getText();
            String nationalite = nationaliteField.getText();
            List<Auteur> auteurs = xmlHandler.readAuteurs();
            int newId = auteurs.size() + 1;
            auteurs.add(new Auteur(newId, nom, nationalite));
            xmlHandler.writeAuteurs(auteurs);
            loadAuthors();
            addAuthorFrame.dispose();
        });
    }

    private void showEditAuthorForm() {
        int selectedIndex = view.getAuthorList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an author to edit.");
            return;
        }

        List<Auteur> auteurs = xmlHandler.readAuteurs();
        Auteur selectedAuteur = auteurs.get(selectedIndex);

        JFrame editAuthorFrame = new JFrame("Edit Author");
        editAuthorFrame.setSize(350, 200);
        editAuthorFrame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField nomField = new JTextField(selectedAuteur.getNom());
        JTextField nationaliteField = new JTextField(selectedAuteur.getNationalite());
        JButton saveButton = new JButton("Save");

        nomField.setPreferredSize(new Dimension(200, 25));
        nationaliteField.setPreferredSize(new Dimension(200, 25));
        saveButton.setPreferredSize(new Dimension(100, 30));

        gbc.gridx = 0;
        gbc.gridy = 0;
        editAuthorFrame.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        editAuthorFrame.add(nomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        editAuthorFrame.add(new JLabel("Nationality:"), gbc);
        gbc.gridx = 1;
        editAuthorFrame.add(nationaliteField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        editAuthorFrame.add(saveButton, gbc);

        editAuthorFrame.setVisible(true);

        saveButton.addActionListener(e -> {
            selectedAuteur.setNom(nomField.getText());
            selectedAuteur.setNationalite(nationaliteField.getText());
            xmlHandler.writeAuteurs(auteurs);
            loadAuthors();
            editAuthorFrame.dispose();
        });
    }

    private void showDeleteAuthorForm() {
        int selectedIndex = view.getAuthorList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select an author to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this author?", "Delete Author", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            List<Auteur> auteurs = xmlHandler.readAuteurs();
            auteurs.remove(selectedIndex);
            xmlHandler.writeAuteurs(auteurs);
            loadAuthors();
        }
    }
}