package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;

public class VueTableauBord extends JPanel implements GestionnaireFlotteObserver {
    private static final long serialVersionUID = 1L;
    
    private GestionnaireFlotte gestionnaire;
    private JLabel lblDisponibilite, lblCoutEntretien, lblVehiculesEntretien;
    private JLabel lblKilometragesMoyen, lblUrgencesActives, lblMissionsEnCours;
    
    public VueTableauBord(GestionnaireFlotte gestionnaire) {
        this.gestionnaire = gestionnaire;
        gestionnaire.ajouterObservateur(this);
        
        setLayout(new GridLayout(3, 2, 15, 15));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 240, 240));

        add(creerPanneauStatut("Taux de Disponibilité", lblDisponibilite = new JLabel()));
        add(creerPanneauStatut("Coût Total Entretien", lblCoutEntretien = new JLabel()));
        add(creerPanneauStatut("Véhicules en Maintenance", lblVehiculesEntretien = new JLabel()));
        add(creerPanneauStatut("Kilométrage Moyen", lblKilometragesMoyen = new JLabel()));
        add(creerPanneauStatut("Urgences Actives", lblUrgencesActives = new JLabel()));
        add(creerPanneauStatut("Missions en Cours", lblMissionsEnCours = new JLabel()));

        mettreAJourStatistiques();
    }

    private JPanel creerPanneauStatut(String titre, JLabel label) {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBackground(Color.WHITE);
        panneau.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(titre),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        
        panneau.add(label, BorderLayout.CENTER);
        return panneau;
    }

    private void mettreAJourStatistiques() {
        lblDisponibilite.setText(String.format("%.1f%%", gestionnaire.obtenirTauxDisponibilite()));
        lblCoutEntretien.setText(String.format("%.2f €", gestionnaire.obtenirCoutTotalEntretien()));
        lblVehiculesEntretien.setText(String.valueOf(gestionnaire.obtenirNombreVehiculesEnMaintenance()));
        lblKilometragesMoyen.setText(String.format("%.0f km", gestionnaire.obtenirKilometragesMoyen()));
        lblUrgencesActives.setText(String.valueOf(gestionnaire.obtenirNombreUrgencesActives()));
        lblMissionsEnCours.setText(String.valueOf(gestionnaire.obtenirNombreMissionsEnCours()));
    }
    
    @Override
    public void flotteModifiee() {
        SwingUtilities.invokeLater(this::mettreAJourStatistiques);
    }
}
