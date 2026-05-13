package model;


public interface Urgence {
   
    int obtenirNiveauUrgence();
    
    // Activer le mode urgence
    void activerModeUrgence();
    
    // Désactiver le mode urgence
    void desactiverModeUrgence();
    
  
    boolean modeUrgenceActif();
}
