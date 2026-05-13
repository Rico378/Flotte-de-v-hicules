package model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class GestionnaireMissions implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Mission> missions;
    
    public GestionnaireMissions() {
        this.missions = new ArrayList<>();
    }
    
    // Ajouter une mission
    public void ajouter(Mission mission) {
        if (mission != null) {
            missions.add(mission);
        }
    }
    
    // Supprimer une mission par ID

    public List<Mission> obtenirTous() {
        return new ArrayList<>(missions);
    }
    
    // Filtrer les missions par statut
    public List<Mission> filtrerParStatut(String statut) {
        return missions.stream()
            .filter(m -> m.getStatut().equalsIgnoreCase(statut))
            .collect(Collectors.toList());
    }
    
    // Filtrer les missions par type
    public List<Mission> filtrerParType(String type) {
        return missions.stream()
            .filter(m -> m.obtenirType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }
    
    // Filtrer les missions par statut et type
    public List<Mission> filtrerParCriteres(String statut, String type) {
        return missions.stream()
            .filter(m -> m.getStatut().equalsIgnoreCase(statut))
            .filter(m -> m.obtenirType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }
    
    // Trier les missions par date de début
    public List<Mission> trierParDate() {
        return missions.stream()
            .sorted(Comparator.comparing(Mission::getDateDebut).reversed())
            .collect(Collectors.toList());
    }
    
    // Trier les missions par statut puis par durée estimée
    public List<Mission> trierParStatutEtDuree() {
        return missions.stream()
            .sorted(
                Comparator.comparing(Mission::getStatut)
                    .thenComparingInt(Mission::obtenirDureeEstimee)
            )
            .collect(Collectors.toList());
    }
    
    // Obtenir les missions trackables 
    public List<Mission> obtenirMissionsTrackables() {
        return missions.stream()
            .filter(m -> m instanceof Trackable)
            .collect(Collectors.toList());
    }
    
    // Statistique : Nombre de missions terminées
    public long compterMissionsTerminees() {
        return missions.stream()
            .filter(m -> m.getStatut().equals("Terminée"))
            .count();
    }
    
    // Statistique : Nombre de missions en cours
    public long compterMissionsEnCours() {
        return missions.stream()
            .filter(m -> m.getStatut().equals("En cours"))
            .count();
    }
    
   //   Statistique : Durée totale estimée des missions en cours
    public int calculerDureeTotaleMissionsEnCours() {
        return missions.stream()
            .filter(m -> m.getStatut().equals("En cours") || m.getStatut().equals("Planifiée"))
            .mapToInt(Mission::obtenirDureeEstimee)
            .sum();
    }
    
   // Rechercher une mission par ID
    public Mission rechercherParId(int idMission) {
        return missions.stream()
            .filter(m -> m.getIdMission() == idMission)
            .findFirst()
            .orElse(null);
    }
    
    // Supprimer une mission par ID
    public void supprimerParId(int idMission) {
        missions.removeIf(m -> m.getIdMission() == idMission);
    }
    
    // Obtenir le nombre total de missions
    public int taille() {
        return missions.size();
    }
    
   // Vider toutes les missions
    public void vider() {
        missions.clear();
    }
}
