 package test;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class App {

    // CORRECTION : TacheService (pas Test)
    private static Test service = new Test();
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // CORRECTION : new TachesHandle(service) — pas new Test(service)
        server.createContext("/taches", new TachesHandle(service));
        server.createContext("/", new StaticHandler("web"));
        server.start();

        System.out.println("Serveur démarré sur http://localhost:8080");

        String choix;
        do {
            afficherMenu();
            choix = input.nextLine().trim();
            switch (choix) {
                case "1" -> ajouterTache();
                case "2" -> modifierTache();
                case "3" -> supprimerTache();
                case "4" -> service.afficher();
                case "0" -> System.out.println("Au revoir !");
                default  -> System.out.println("Choix invalide.");
            }
        } while (!choix.equals("0"));

        input.close();
    }

    private static void afficherMenu() {
        System.out.println("\n--- MENU ---");
        System.out.println("1. Ajouter   2. Modifier   3. Supprimer   4. Afficher   0. Quitter");
        System.out.print("Choix : ");
    }

    private static void ajouterTache() {
        System.out.print("Nom : ");
        String nom = input.nextLine();
        System.out.print("Date (ex: 2025-12-31) : ");
        String date = input.nextLine();
        service.ajouter(nom, date, saisirPriorite());
    }

    private static void modifierTache() {
        service.afficher();
        System.out.print("ID à modifier : ");
        int id = lireInt();
        System.out.print("Nouveau nom : ");
        String nom = input.nextLine();
        System.out.print("Nouvelle date : ");
        String date = input.nextLine();
        service.modifier(id, nom, date, saisirPriorite());
    }

    private static void supprimerTache() {
        service.afficher();
        System.out.print("ID à supprimer : ");
        service.supprimer(lireInt());
    }

    private static Priorite saisirPriorite() {
        while (true) {
            System.out.print("Priorité (ELEVE / MOYEN / BAS) : ");
            try {
                return Priorite.valueOf(input.nextLine().trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Priorité invalide, réessayez.");
            }
        }
    }

    private static int lireInt() {
        while (true) {
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Entrez un nombre valide.");
            }
        }
    }
}

