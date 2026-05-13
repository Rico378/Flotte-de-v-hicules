package model;

/**
 * Interface Maintenable : Contrats pour les véhicules nécessitant une maintenance.
 * Concept OOP : Ségrégation des interfaces (ISP - Interface Segregation Principle)
 */
public interface Maintenable {
    /**
     * Marquer le véhicule comme nécessitant une maintenance
     * @param description Raison de la maintenance
     */
    void signalerEntretien(String description);
    
    /**
     * Effectuer la maintenance du véhicule
     */
    void effectuerEntretien();
    
    /**
     * Vérifier si le véhicule nécessite une maintenance
     * @return true si maintenance requise, false sinon
     */
    boolean necessiteEntretien();
    
    /**
     * Obtenir le coût estimé de la maintenance
     * @return Coût en euros
     */
    double obtenirCoutEntretien();
}
