package model;


public class VéhiculeLourd extends Véhicule implements Assignable, Maintenable {
    private static final long serialVersionUID = 1L;
    
    // Attributs spécifiques au véhicule lourd
    private int chargeMaximale; 
    private Mission missionActuelle;
    private boolean necessiteEntretien;
    private String descriptionEntretien;
    private double coutEntretien;
    
    public VéhiculeLourd(String immatriculation, int kilometrage, String marque, String modele, int chargeMaximale) {
        super(immatriculation, kilometrage, marque, modele);
        this.chargeMaximale = chargeMaximale;
        this.missionActuelle = null;
        this.necessiteEntretien = false;
        this.descriptionEntretien = "";
        this.coutEntretien = 0.0;
    }
    
    @Override
    public String obtenirType() {
        return "Lourd";
    }
    
    @Override
    public int obtenirCapaciteCharge() {
        return chargeMaximale;
    }
    
    public int getChargeMaximale() {
        return chargeMaximale;
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
        this.coutEntretien = (this.getKilometrage() / 1000) * 100;
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
        return (this.getKilometrage() % 10000) > 9500 || necessiteEntretien;
    }
    
    @Override
    public double obtenirCoutEntretien() {
        return coutEntretien;
    }
    
    public Mission getMissionActuelle() {
        return missionActuelle;
    }
}
