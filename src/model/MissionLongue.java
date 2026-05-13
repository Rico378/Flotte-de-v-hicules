package model;

import java.util.ArrayList;
import java.util.List;


public class MissionLongue extends Mission implements Trackable {
    private static final long serialVersionUID = 1L;
    
    private static final int DUREE_ESTIMEE = 8; 
    private String positionActuelle;
    private List<String> historique; 
    
    public MissionLongue(String description) {
        super(description);
        this.positionActuelle = "Point de départ";
        this.historique = new ArrayList<>();
        this.historique.add("Point de départ enregistré");
    }
    
    @Override
    public int obtenirDureeEstimee() {
        return DUREE_ESTIMEE;
    }
    
    @Override
    public String obtenirType() {
        return "Longue";
    }
    
    // Implémentation de Trackable
    @Override
    public String obtenirPosition() {
        return positionActuelle;
    }
    
    @Override
    public void mettreAJourPosition(String position) {
        this.positionActuelle = position;
        historique.add(String.format("[%s] Position: %s", 
            java.time.LocalDateTime.now().format(
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")), 
            position));
        enregistrerEvenement("Position mise à jour: " + position);
    }
    
    @Override
    public void enregistrerEvenement(String description) {
        super.enregistrerEvenement(description);
    }
    
    @Override
    public String obtenirHistorique() {
        StringBuilder sb = new StringBuilder();
        for (String ligne : historique) {
            sb.append(ligne).append("\n");
        }
        return sb.toString();
    }
}
