public class Livre {
    private String titre;
    private String isbn;
    private String datePublication;
    private int idAuteur;

    public Livre(String titre, String isbn, String datePublication, int idAuteur) {
        this.titre = titre;
        this.isbn = isbn;
        this.datePublication = datePublication;
        this.idAuteur = idAuteur;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDatePublication() {
        return datePublication;
    }

    public void setDatePublication(String datePublication) {
        this.datePublication = datePublication;
    }

    public int getIdAuteur() {
        return idAuteur;
    }

    public void setIdAuteur(int idAuteur) {
        this.idAuteur = idAuteur;
    }
}