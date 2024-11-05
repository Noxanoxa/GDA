public class Emprunt {
    private String isbn;
    private String nomUtilisateur;
    private String dateEmprunt;
    private String dateRetour;

    public Emprunt(String isbn, String nomUtilisateur, String dateEmprunt, String dateRetour) {
        this.isbn = isbn;
        this.nomUtilisateur = nomUtilisateur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public void setNomUtilisateur(String nomUtilisateur) {
        this.nomUtilisateur = nomUtilisateur;
    }

    public String getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(String dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public String getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(String dateRetour) {
        this.dateRetour = dateRetour;
    }
}