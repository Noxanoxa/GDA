import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserManagementController {
    private XMLHandler xmlHandler;
    private UserManagementView view;

    public UserManagementController(XMLHandler xmlHandler, UserManagementView view) {
        this.xmlHandler = xmlHandler;
        this.view = view;

        loadBooks();
        loadBorrowedBooks();

        view.getBorrowBookButton().addActionListener(e -> borrowBook());
        view.getReturnBookButton().addActionListener(e -> returnBook());
        view.getSwitchButton().addActionListener(e -> switchToAdminInterface());
    }

    private void loadBooks() {
        List<Livre> livres = xmlHandler.readLivres();
        List<Emprunt> emprunts = xmlHandler.readEmprunts();
        DefaultListModel<String> bookListModel = view.getBookListModel();
        bookListModel.clear();

        for (Livre livre : livres) {
            boolean isBorrowed = false;
            for (Emprunt emprunt : emprunts) {
                if (emprunt.getIsbnLivre().equals(livre.getIsbn()) && emprunt.getRetourne().equals("non")) {
                    isBorrowed = true;
                    break;
                }
            }
            if (!isBorrowed) {
                bookListModel.addElement(livre.getTitre() + " - " + livre.getIsbn());
            }
        }
    }

    private void loadBorrowedBooks() {
        List<Emprunt> emprunts = xmlHandler.readEmprunts();
        List<Livre> livres = xmlHandler.readLivres();
        DefaultListModel<String> borrowedListModel = view.getBorrowedListModel();
        borrowedListModel.clear();

        for (Emprunt emprunt : emprunts) {
            if (emprunt.getRetourne().equals("non")) {
                for (Livre livre : livres) {
                    if (livre.getIsbn().equals(emprunt.getIsbnLivre())) {
                        borrowedListModel.addElement(livre.getTitre() + " - " + emprunt.getDateEmprunt());
                        break;
                    }
                }
            }
        }
    }

    private void borrowBook() {
        int selectedIndex = view.getBookList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book to borrow.");
            return;
        }

        List<Livre> livres = xmlHandler.readLivres();
        Livre selectedLivre = livres.get(selectedIndex);

        List<Emprunt> emprunts = xmlHandler.readEmprunts();
        boolean alreadyBorrowed = false;
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getIsbnLivre().equals(selectedLivre.getIsbn()) && emprunt.getRetourne().equals("oui")) {
                emprunt.setRetourne("non");
                alreadyBorrowed = true;
                break;
            }
        }

        if (!alreadyBorrowed) {
            emprunts.add(new Emprunt("1", selectedLivre.getIsbn(), "2024-11-05", "non"));
        }

        xmlHandler.writeEmprunts(emprunts);
        loadBooks();
        loadBorrowedBooks();
    }

    private void returnBook() {
        int selectedIndex = view.getBorrowedList().getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(null, "Please select a book to return.");
            return;
        }

        List<Emprunt> emprunts = xmlHandler.readEmprunts();
        Emprunt selectedEmprunt = emprunts.get(selectedIndex);
        selectedEmprunt.setRetourne("oui");
        xmlHandler.writeEmprunts(emprunts);
        loadBooks();
        loadBorrowedBooks();
    }


    private void switchToAdminInterface() {
        view.getFrame().dispose();
        SwingUtilities.invokeLater(() -> {
            XMLHandler xmlHandler = new XMLHandler();
            LibraryManagementView view = new LibraryManagementView();
            new LibraryManagementController(xmlHandler, view);
        });
    }
}