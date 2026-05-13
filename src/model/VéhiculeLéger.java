package model;


public class VéhiculeLéger extends Véhicule implements Assignable, Maintenable {
    private static final long serialVersionUID = 1L;
    
    // Attributs spécifiques au véhicule léger
    private Mission missionActuelle;
    private boolean necessiteEntretien;
    private String descriptionEntretien;
    private double coutEntretien;
    
    public VéhiculeLéger(String immatriculation, int kilometrage, String marque, String modele) {
        super(immatriculation, kilometrage, marque, modele);
        this.missionActuelle = null;
        this.necessiteEntretien = false;
        this.descriptionEntretien = "";
        this.coutEntretien = 0.0;
    }
    
    @Override
    public String obtenirType() {
        return "Léger";
    }
    
    @Override
    public int obtenirCapaciteCharge() {
        return 1500; // 1.5 tonnespour un véhicule léger
    }
    
    // Implémentation de Assignable
    @Override
    public void assigner(Mission mission) {
        this.missionActuelle = mission;
        mission.setAffectation(this.getImmatriculation());
        this.setStatut("En mission");
    }
    
    @Override
    public void liberer() {
        if (missionActuelle != null) {
            missionActuelle.setStatut("Terminée");
            missionActuelle.setDateFin(java.time.LocalDateTime.now());
            missionActuelle = null;
        }
        this.setStatut("En service");
    }
    
    @Override
    public boolean estDisponible() {
        return missionActuelle == null && this.getStatut().equals("En service");
    }
    
    // Implémentation de Maintenable
    @Override
    public void signalerEntretien(String description) {
        this.necessiteEntretien = true;
        this.descriptionEntretien = description;
        // Calcul simple du coût : 50€ par 1000 km supplémentaires
        this.coutEntretien = (this.getKilometrage() / 1000) * 50;
        this.setStatut("En maintenance");
    }
    
    @Override
    public void effectuerEntretien() {
        this.necessiteEntretien = false;
        this.descriptionEntretien = "";
        this.coutEntretien = 0.0;
        this.setStatut("En service");
    }
    
    @Override
    public boolean necessiteEntretien() {
        return (this.getKilometrage() % 15000) > 14000 || necessiteEntretien;
    }
    
    @Override
    public double obtenirCoutEntretien() {
        return coutEntretien;
    }
    
    public Mission getMissionActuelle() {
        return missionActuelle;
    }
}
