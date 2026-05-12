package model;


public class VéhiculeSpécial extends Véhicule implements Urgence, Maintenable {
    private static final long serialVersionUID = 1L;
    
    // Attributs spécifiques au véhicule spécial
    private int niveauUrgence; 
    private boolean modeUrgenceActif;
    private boolean necessiteEntretien;
    private String descriptionEntretien;
    private double coutEntretien;
    
    public VéhiculeSpécial(String immatriculation, int kilometrage, String marque, String modele, int niveauUrgence) {
        super(immatriculation, kilometrage, marque, modele);
        this.niveauUrgence = niveauUrgence;
        this.modeUrgenceActif = false;
        this.necessiteEntretien = false;
        this.descriptionEntretien = "";
        this.coutEntretien = 0.0;
    }
    
    @Override
    public String obtenirType() {
        return "Spécial";
    }
    
    @Override
    public int obtenirCapaciteCharge() {
        return 500; 
    }
    
    // Implémentation de Urgence
    @Override
    public int obtenirNiveauUrgence() {
        return niveauUrgence;
    }
    
    @Override
    public void activerModeUrgence() {
        this.modeUrgenceActif = true;
        this.setStatut("Intervention urgente");
    }
    
    @Override
    public void desactiverModeUrgence() {
        this.modeUrgenceActif = false;
        if (!this.getStatut().equals("En maintenance")) {
            this.setStatut("En service");
        }
    }
    
    @Override
    public boolean modeUrgenceActif() {
        return modeUrgenceActif;
    }
    
    // Implémentation de Maintenable
    @Override
    public void signalerEntretien(String description) {
        this.necessiteEntretien = true;
        this.descriptionEntretien = description;
        this.coutEntretien = (this.getKilometrage() / 1000) * 150;
        if (!modeUrgenceActif) {
            this.setStatut("En maintenance");
        }
    }
    
    @Override
    public void effectuerEntretien() {
        this.necessiteEntretien = false;
        this.descriptionEntretien = "";
        this.coutEntretien = 0.0;
        if (!modeUrgenceActif) {
            this.setStatut("En service");
        }
    }
    
    @Override
    public boolean necessiteEntretien() {
        return (this.getKilometrage() % 8000) > 7800 || necessiteEntretien;
    }
    
    @Override
    public double obtenirCoutEntretien() {
        return coutEntretien;
    }
}
