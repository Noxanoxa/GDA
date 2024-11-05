public class Auteur {
    private int id;
    private String nom;
    private String nationalite;

    public Auteur(int id, String nom, String nationalite) {
        this.id = id;
        this.nom = nom;
        this.nationalite = nationalite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNationalite() {
        return nationalite;
    }

    public void setNationalite(String nationalite) {
        this.nationalite = nationalite;
    }
}