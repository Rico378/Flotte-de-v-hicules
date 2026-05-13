package model;


public class MissionCourte extends Mission {
    private static final long serialVersionUID = 1L;
    
    private static final int DUREE_ESTIMEE = 2; // 2 heures par défaut
    
    public MissionCourte(String description) {
        super(description);
    }
    
    @Override
    public int obtenirDureeEstimee() {
        return DUREE_ESTIMEE;
    }
    
    @Override
    public String obtenirType() {
        return "Courte";
    }
}
