package util;

import model.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;


public class PersistenceManager {
    
    private static final String VEHICULES_FILE = "resources/vehicules.dat";
    private static final String MISSIONS_FILE = "resources/missions.dat";
    private static final String CHAUFFEURS_FILE = "resources/chauffeurs.dat";
    
    private static final String VEHICULES_CSV = "resources/vehicules.csv";
    private static final String MISSIONS_CSV = "resources/missions.csv";
    
    static {
        // Créer le dossier resources s'il n'existe pas
        try {
            Files.createDirectories(Paths.get("resources"));
        } catch (IOException e) {
            System.err.println("Impossible de créer le dossier resources: " + e.getMessage());
        }
    }
    
    // Sauvegarder les véhicules
    public static void sauvegarderVehicules(Registre<?> registre) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(VEHICULES_FILE))) {
            oos.writeObject(registre);
            System.out.println("✓ Véhicules sauvegardés: " + VEHICULES_FILE);
        } catch (IOException e) {
            System.err.println("✗ Erreur sauvegarde véhicules: " + e.getMessage());
        }
    }
    
    // Charger les véhicules
    @SuppressWarnings("unchecked")
    public static Registre<Véhicule> chargerVehicules() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(VEHICULES_FILE))) {
            Registre<Véhicule> registre = (Registre<Véhicule>) ois.readObject();
            System.out.println("✓ Véhicules chargés: " + VEHICULES_FILE);
            return registre;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ Pas de fichier de sauvegarde, création vide");
            return new Registre<>();
        }
    }
    
    // Sauvegarder les missions
    public static void sauvegarderMissions(GestionnaireMissions gestionnaire) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(MISSIONS_FILE))) {
            oos.writeObject(gestionnaire);
            System.out.println("✓ Missions sauvegardées: " + MISSIONS_FILE);
        } catch (IOException e) {
            System.err.println("✗ Erreur sauvegarde missions: " + e.getMessage());
        }
    }
    
    // Charger les missions
    public static GestionnaireMissions chargerMissions() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(MISSIONS_FILE))) {
            GestionnaireMissions gestionnaire = (GestionnaireMissions) ois.readObject();
            System.out.println("✓ Missions chargées: " + MISSIONS_FILE);
            return gestionnaire;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ Pas de fichier missions, création vide");
            return new GestionnaireMissions();
        }
    }
// Sauvegarder les chauffeurs
    public static void sauvegarderChauffeurs(List<Chauffeur> chauffeurs) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(CHAUFFEURS_FILE))) {
            oos.writeObject(chauffeurs);
            System.out.println("✓ Chauffeurs sauvegardés: " + CHAUFFEURS_FILE);
        } catch (IOException e) {
            System.err.println("✗ Erreur sauvegarde chauffeurs: " + e.getMessage());
        }
    }
    
    // Charger les chauffeurs
    @SuppressWarnings("unchecked")
    public static List<Chauffeur> chargerChauffeurs() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CHAUFFEURS_FILE))) {
            List<Chauffeur> chauffeurs = (List<Chauffeur>) ois.readObject();
            System.out.println("✓ Chauffeurs chargés: " + CHAUFFEURS_FILE);
            return chauffeurs;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("ℹ Pas de fichier chauffeurs, création vide");
            return new ArrayList<>();
        }
    }
    
    // Exporter les véhicules en CSV
    public static void exporterVehiculesCSV(Registre<?> registre) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(VEHICULES_CSV), StandardCharsets.UTF_8))) {
            // En-têtes
            writer.println("Immatriculation,Type,Marque,Modele,Kilometrage,Statut,Charge");
            
            // Données
            for (Véhicule v : registre.obtenirTous()) {
                writer.printf("%s,%s,%s,%s,%d,%s,%d%n",
                    v.getImmatriculation(),
                    v.obtenirType(),
                    v.getMarque(),
                    v.getModele(),
                    v.getKilometrage(),
                    v.getStatut(),
                    v.obtenirCapaciteCharge()
                );
            }
            System.out.println("✓ Véhicules exportés en CSV: " + VEHICULES_CSV);
        } catch (IOException e) {
            System.err.println("✗ Erreur export CSV véhicules: " + e.getMessage());
        }
    }
    
    // Exporter les missions en CSV
    public static void exporterMissionsCSV(GestionnaireMissions gestionnaire) {
        try (PrintWriter writer = new PrintWriter(
                new OutputStreamWriter(new FileOutputStream(MISSIONS_CSV), StandardCharsets.UTF_8))) {
            // En-têtes
            writer.println("ID,Type,Description,Statut,DateDebut,Affectation");
            
            // Données
            for (Mission m : gestionnaire.obtenirTous()) {
                writer.printf("%d,%s,%s,%s,%s,%s%n",
                    m.getIdMission(),
                    m.obtenirType(),
                    m.getDescription().replace(",", ";"),
                    m.getStatut(),
                    m.getDateDebut(),
                    m.getAffectation() != null ? m.getAffectation() : "---"
                );
            }
            System.out.println("✓ Missions exportées en CSV: " + MISSIONS_CSV);
        } catch (IOException e) {
            System.err.println("✗ Erreur export CSV missions: " + e.getMessage());
        }
    }
}
