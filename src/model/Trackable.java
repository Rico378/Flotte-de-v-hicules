package model;

public interface Trackable {

    String obtenirPosition();
    
    void mettreAJourPosition(String position);
    
    void enregistrerEvenement(String description);
    
    String obtenirHistorique();
}
