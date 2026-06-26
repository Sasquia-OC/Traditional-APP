package test;

public class Taches {

    private static int compteur = 1; // ID auto-incrémenté
    private int id;
    private String nom;
    private String date;
    private Priorite priorite;

    public Taches(String nom, String date, Priorite priorite) {
        this.id = compteur++;
        this.nom = nom;
        this.date = date;
        this.priorite = priorite;
    }

    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDate() {
        return date;
    }

    public Priorite getPriorite() {
        return priorite;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPriorite(Priorite p) {
        this.priorite = p;
    }

    @Override
    public String toString() {
        return String.format("[ID:%d] %-20s | Date: %-12s | Priorité: %s",
                id, nom, date, priorite);
    }
}
