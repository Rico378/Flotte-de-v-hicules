package model;


public interface Maintenable {
   
    void signalerEntretien(String description);
    
 // Effectuer la maintenance et remettre le véhicule en service
    void effectuerEntretien();
    
   
    boolean necessiteEntretien();
    
   
    double obtenirCoutEntretien();
}
