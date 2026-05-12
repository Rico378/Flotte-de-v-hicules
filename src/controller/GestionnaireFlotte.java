package controller;

import model.*;
import util.PersistenceManager;
import java.util.*;
import java.util.stream.Collectors;

public class GestionnaireFlotte {
    
    private Registre<Véhicule> registreVehicules;
    private GestionnaireMissions gestionnaireMissions;
    private List<Chauffeur> chauffeurs;
    private List<GestionnaireFlotteObserver> observers;
    
    public GestionnaireFlotte() {
        this.registreVehicules = new Registre<>();
        this.gestionnaireMissions = new GestionnaireMissions();
        this.chauffeurs = new ArrayList<>();
        this.observers = new ArrayList<>();
    }
    
    public void ajouterObservateur(GestionnaireFlotteObserver observer) {
        observers.add(observer);
    }

    private void notifierObservateurs() {
        for (GestionnaireFlotteObserver obs : observers) {
            obs.flotteModifiee();
        }
    }

    public void ajouterVehicule(Véhicule vehicule) {
        registreVehicules.ajouter(vehicule);
        notifierObservateurs();
    }
 
    public List<Véhicule> obtenirVehicules() {
        return registreVehicules.obtenirTous();
    }

    public Véhicule rechercherVehicule(String immatriculation) {
        return registreVehicules.rechercher(immatriculation);
    }

    public void augmenterKilometrage(String immatriculation, int km) {
        Véhicule v = registreVehicules.rechercher(immatriculation);
        if (v != null) {
            v.augmenterKilometrage(km);
            notifierObservateurs();
        }
    }

    public void supprimerVehicule(String immatriculation) {
        registreVehicules.supprimer(immatriculation);
        notifierObservateurs();
    }

    public void ajouterMission(Mission mission) {
        gestionnaireMissions.ajouter(mission);
        notifierObservateurs();
    }

    public List<Mission> obtenirMissions() {
        return gestionnaireMissions.obtenirTous();
    }

    public void changerStatutMission(int idMission, String nouveauStatut) {
        Mission m = gestionnaireMissions.rechercherParId(idMission);
        if (m != null) {
            m.setStatut(nouveauStatut);
            notifierObservateurs();
        }
    }

    public void supprimerMission(int idMission) {
        gestionnaireMissions.supprimerParId(idMission);
        notifierObservateurs();
    }

    public void ajouterChauffeur(Chauffeur chauffeur) {
        chauffeurs.add(chauffeur);
        notifierObservateurs();
    }

    public List<Chauffeur> obtenirChauffeurs() {
        return new ArrayList<>(chauffeurs);
    }

    public List<Chauffeur> obtenirChauffeursDisponibles() {
        return chauffeurs.stream()
            .filter(Chauffeur::estDisponible)
            .collect(Collectors.toList());
    }

    public void assignerMission(String immatriculation, int idMission, int idChauffeur) 
            throws ChauffeurIndisponibleException {
        Véhicule v = registreVehicules.rechercher(immatriculation);
        Mission m = gestionnaireMissions.rechercherParId(idMission);
        Chauffeur c = chauffeurs.stream()
            .filter(ch -> ch.getId() == idChauffeur)
            .findFirst()
            .orElse(null);
        
        if (v != null && m != null && c != null) {
            if (v instanceof Assignable) {
                ((Assignable) v).assigner(m);
                c.affecter(m);
                m.setStatut("En cours");
                notifierObservateurs();
            }
        }
    }

    public void terminerMission(int idMission) {
        Mission m = gestionnaireMissions.rechercherParId(idMission);
        if (m != null) {
            String affectation = m.getAffectation();
            if (affectation != null) {
                Véhicule v = registreVehicules.rechercher(affectation);
                if (v instanceof Assignable) {
                    ((Assignable) v).liberer();
                }
            }
            
            Chauffeur c = chauffeurs.stream()
                .filter(ch -> ch.getMissionActuelle() == m)
                .findFirst()
                .orElse(null);
            if (c != null) {
                c.terminerMission();
            }
            
            m.setStatut("Terminée");
            m.setDateFin(java.time.LocalDateTime.now());
            notifierObservateurs();
        }
    }

    public void signalerEntretien(String immatriculation, String description) {
        Véhicule v = registreVehicules.rechercher(immatriculation);
        if (v instanceof Maintenable) {
            ((Maintenable) v).signalerEntretien(description);
            notifierObservateurs();
        }
    }

    public void effectuerEntretien(String immatriculation) {
        Véhicule v = registreVehicules.rechercher(immatriculation);
        if (v instanceof Maintenable) {
            ((Maintenable) v).effectuerEntretien();
            notifierObservateurs();
        }
    }
    
    public List<Véhicule> filtrerVehicules(String statut, String type) {
        return registreVehicules.filtrerParCriteres(statut, type);
    }

    public List<? extends Véhicule> obtenirVehiculesDisponibles() {
        return registreVehicules.filtrerDisponibles();
    }

    public List<Véhicule> obtenirVehiculesEnMaintenance() {
        return registreVehicules.filtrerParCriteres("En maintenance", "");
    }

    public List<Mission> filtrerMissions(String statut, String type) {
        return gestionnaireMissions.filtrerParCriteres(statut, type);
    }

    public List<Véhicule> trierVehicules() {
        return registreVehicules.trierParStatutEtKilometrage();
    }

    public List<Véhicule> trierVehiculesParKilometrage() {
        return registreVehicules.trierParKilometrage();
    }

    public List<Mission> trierMissions() {
        return gestionnaireMissions.trierParStatutEtDuree();
    }
    
    public double obtenirTauxDisponibilite() {
        return registreVehicules.calculerTauxDisponibilite();
    }

    public double obtenirCoutTotalEntretien() {
        return registreVehicules.calculerCoutTotalEntretien();
    }

    public long obtenirNombreVehiculesEnMaintenance() {
        return registreVehicules.compterVehiculesEnMaintenance();
    }

    public double obtenirKilometragesMoyen() {
        return registreVehicules.calculerKilometragesMoyen();
    }

    public long obtenirNombreUrgencesActives() {
        return registreVehicules.compterUrgencesActives();
    }

    public long obtenirNombreMissionsEnCours() {
        return gestionnaireMissions.compterMissionsEnCours();
    }

    public List<Véhicule> obtenirVehiculesEntretienPriorite() {
        return registreVehicules.obtenirEntretienPriorite();
    }

    public void charger() {
        this.registreVehicules = PersistenceManager.chargerVehicules();
        this.gestionnaireMissions = PersistenceManager.chargerMissions();
        this.chauffeurs = PersistenceManager.chargerChauffeurs();
        notifierObservateurs();
    }

    public void sauvegarder() {
        PersistenceManager.sauvegarderVehicules(registreVehicules);
        PersistenceManager.sauvegarderMissions(gestionnaireMissions);
        PersistenceManager.sauvegarderChauffeurs(chauffeurs);
    }

    public void exporterCSV() {
        PersistenceManager.exporterVehiculesCSV(registreVehicules);
        PersistenceManager.exporterMissionsCSV(gestionnaireMissions);
    }
}
