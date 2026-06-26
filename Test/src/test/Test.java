package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Test {

    private List<Taches> taches = new ArrayList<>();

    public Test() {}

    public void ajouter(String nom, String date, Priorite priorite) {
        taches.add(new Taches(nom, date, priorite));
        System.out.println("Tâche ajoutée avec succès !");
    }

    public List<Taches> getToutesTaches() {
        return taches;
    }

    public Optional<Taches> getTacheParId(int id) {
        return taches.stream()
                .filter(t -> t.getId() == id)
                .findFirst();
    }

    public boolean modifier(int id, String nouveauNom, String nouvelleDate, Priorite nouvellePriorite) {
        Optional<Taches> opt = getTacheParId(id);
        if (opt.isPresent()) {
            Taches t = opt.get();
            t.setNom(nouveauNom);
            t.setDate(nouvelleDate);
            t.setPriorite(nouvellePriorite);
            System.out.println("Tâche modifiée !");
            return true;
        }
        System.out.println("ID introuvable.");
        return false;
    }

    public boolean supprimer(int id) {
        Optional<Taches> opt = getTacheParId(id);
        if (opt.isPresent()) {
            taches.remove(opt.get());
            System.out.println("Tâche supprimée !");
            return true;
        }
        System.out.println("ID introuvable.");
        return false;
    }

    public void afficher() {
        System.out.println("\n--- LISTE DES TACHES ---");
        if (taches.isEmpty()) {
            System.out.println("  (Aucune tâche enregistrée)");
        } else {
            taches.forEach(System.out::println);
        }
    }

    public String toJson() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < taches.size(); i++) {
            Taches t = taches.get(i);
            sb.append(String.format(
                "{\"id\":%d,\"nom\":\"%s\",\"date\":\"%s\",\"priorite\":\"%s\"}",
                t.getId(), t.getNom(), t.getDate(), t.getPriorite()
            ));
            if (i < taches.size() - 1) sb.append(",");
        }
        return sb.append("]").toString();
    }

    public void ajouterDepuisJson(String json) {
        String nom  = extraire(json, "nom");
        String date = extraire(json, "date");
        Priorite p  = Priorite.valueOf(extraire(json, "priorite"));
        ajouter(nom, date, p);
    }

    public boolean modifierDepuisJson(int id, String json) {
        String nom  = extraire(json, "nom");
        String date = extraire(json, "date");
        Priorite p  = Priorite.valueOf(extraire(json, "priorite"));
        return modifier(id, nom, date, p);
    }

    private String extraire(String json, String cle) {
        String search = "\"" + cle + "\":\"";
        int start = json.indexOf(search) + search.length();
        int end   = json.indexOf("\"", start);
        return json.substring(start, end);
    }
}