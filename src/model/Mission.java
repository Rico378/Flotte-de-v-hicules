package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


public abstract class Mission implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Attributs métier
    private int idMission;
    private String description;
    private String statut; 
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private String affectation; 
    private List<String> evenements; 
    
    // Compteur statique pour générer les IDs
    private static int compteurId = 1;
    
    // Constructeur
    public Mission(String description) {
        this.idMission = compteurId++;
        this.description = description;
        this.statut = "Planifiée";
        this.dateDebut = LocalDateTime.now();
        this.affectation = null;
        this.evenements = new ArrayList<>();
        this.evenements.add(String.format("[%s] Mission créée: %s", 
            LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), 
            description));
    }
    
    // Méthodes abstraites
    
    public abstract int obtenirDureeEstimee();
    
   // Type de mission 
    public abstract String obtenirType();
    
    // Méthodes concrètes
    public int getIdMission() {
        return idMission;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getStatut() {
        return statut;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
        enregistrerEvenement("Statut changé en: " + statut);
    }
    
    public LocalDateTime getDateDebut() {
        return dateDebut;
    }
    
    public LocalDateTime getDateFin() {
        return dateFin;
    }
    
    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }
    
    public String getAffectation() {
        return affectation;
    }
    
    public void setAffectation(String affectation) {
        this.affectation = affectation;
        enregistrerEvenement("Véhicule affecté: " + affectation);
    }
    
    public List<String> getEvenements() {
        return new ArrayList<>(evenements);
    }
    
    public void enregistrerEvenement(String description) {
        String timestamp = LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        evenements.add(String.format("[%s] %s", timestamp, description));
    }
    
    @Override
    public String toString() {
        return String.format("Mission #%d [%s] - %s (%s)", 
            idMission, obtenirType(), description, statut);
    }
}
