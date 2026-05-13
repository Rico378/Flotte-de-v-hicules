package model;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;


public class Registre<T extends Véhicule> implements Serializable {
    private static final long serialVersionUID = 1L;
    
    // Collections avancées et justifiées
    private List<T> vehicules; 
    private Map<String, T> indexImmatriculations; 
    private PriorityQueue<T> entretienPriorite; 

    // Comparator pour ordonner les véhicules par urgence d'entretien
    // Les véhicules avec le plus kilométrage en priorité 
    public Registre() {
        this.vehicules = new ArrayList<>();
        this.indexImmatriculations = new HashMap<>();
        
        this.entretienPriorite = new PriorityQueue<>(
            Comparator.comparingInt(Véhicule::getKilometrage).reversed()
        );
    }
    
  // Ajouter un véhicule au registre
    public void ajouter(T vehicule) {
        if (vehicule == null) return;
        vehicules.add(vehicule);
        indexImmatriculations.put(vehicule.getImmatriculation(), vehicule);
        if (vehicule instanceof Maintenable) {
            entretienPriorite.offer(vehicule);
        }
    }
    
   //   Supprimer un véhicule du registre par immatriculation
    public void supprimer(String immatriculation) {
        T vehicule = indexImmatriculations.remove(immatriculation);
        if (vehicule != null) {
            vehicules.remove(vehicule);
            entretienPriorite.remove(vehicule);
        }
    }
    
   // Rechercher un véhicule par immatriculation
    public T rechercher(String immatriculation) {
        return indexImmatriculations.get(immatriculation);
    }
    
   // Obtenir tous les véhicules
    public List<T> obtenirTous() {
        return new ArrayList<>(vehicules);
    }
    
    // Obtenir les véhicules nécessitant un entretien, triés par priorité
    public List<T> obtenirEntretienPriorite() {
        // Créer une copie pour ne pas modifier la queue
        List<T> resultat = new ArrayList<>(entretienPriorite);
        return resultat.stream()
            .filter(v -> v instanceof Maintenable)
            .filter(v -> ((Maintenable) v).necessiteEntretien())
            .sorted(Comparator.comparingInt(Véhicule::getKilometrage).reversed())
            .collect(Collectors.toList());
    }
    
    
    public List<T> filtrerParCriteres(String statut, String type) {
        return vehicules.stream()
            .filter(v -> v.getStatut().equalsIgnoreCase(statut))
            .filter(v -> v.obtenirType().equalsIgnoreCase(type))
            .collect(Collectors.toList());
    }
    
    // Filtrer les véhicules disponibles (non assignés et en service)
    public List<? extends Véhicule> filtrerDisponibles() {
        return vehicules.stream()
            .filter(v -> v instanceof Assignable)
            .filter(v -> ((Assignable) v).estDisponible())
            .collect(Collectors.toList());
    }
    
    //  Trier les véhicules par statut puis par kilométrage décroissant
    public List<T> trierParStatutEtKilometrage() {
        return vehicules.stream()
            .sorted(
                Comparator.comparing(Véhicule::getStatut)
                    .thenComparingInt(Véhicule::getKilometrage).reversed()
            )
            .collect(Collectors.toList());
    }
    
   // Trier les véhicules par kilométrage décroissant
    public List<T> trierParKilometrage() {
        return vehicules.stream()
            .sorted(Comparator.comparingInt(Véhicule::getKilometrage).reversed())
            .collect(Collectors.toList());
    }
    
   // Statistique 1 : Taux de disponibilité
    public double calculerTauxDisponibilite() {
        if (vehicules.isEmpty()) return 0.0;
        long disponibles = vehicules.stream()
            .filter(v -> v instanceof Assignable)
            .filter(v -> ((Assignable) v).estDisponible())
            .count();
        return (disponibles * 100.0) / vehicules.size();
    }
    
   // Statistique 2 : Coût total d'entretien
    public double calculerCoutTotalEntretien() {
        return vehicules.stream()
            .filter(v -> v instanceof Maintenable)
            .mapToDouble(v -> ((Maintenable) v).obtenirCoutEntretien())
            .sum();
    }
    
   // Statistique 3 : Nombre de véhicules en maintenance
    public long compterVehiculesEnMaintenance() {
        return vehicules.stream()
            .filter(v -> v.getStatut().equals("En maintenance"))
            .count();
    }
    
  // Statistique 4 : Kilométrage moyen
    public double calculerKilometragesMoyen() {
        if (vehicules.isEmpty()) return 0.0;
        return vehicules.stream()
            .mapToInt(Véhicule::getKilometrage)
            .average()
            .orElse(0.0);
    }
    
  // Statistique 5 : Nombre de véhicules par type
    public long compterUrgencesActives() {
        return vehicules.stream()
            .filter(v -> v instanceof Urgence)
            .filter(v -> ((Urgence) v).modeUrgenceActif())
            .count();
    }
    
   // Obtenir la taille du registre
    public int taille() {
        return vehicules.size();
    }
    
 // Vider le registre
    public void vider() {
        vehicules.clear();
        indexImmatriculations.clear();
        entretienPriorite.clear();
    }
}
