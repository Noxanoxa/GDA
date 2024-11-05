public class Emprunt {
    private String userId;
    private String isbnLivre;
    private String dateEmprunt;
    private String retourne;

    public Emprunt(String userId, String isbnLivre, String dateEmprunt, String retourne) {
        this.userId = userId;
        this.isbnLivre = isbnLivre;
        this.dateEmprunt = dateEmprunt;
        this.retourne = retourne;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIsbnLivre() {
        return isbnLivre;
    }

    public void setIsbnLivre(String isbnLivre) {
        this.isbnLivre = isbnLivre;
    }

    public String getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(String dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public String getRetourne() {
        return retourne;
    }

    public void setRetourne(String retourne) {
        this.retourne = retourne;
    }

}