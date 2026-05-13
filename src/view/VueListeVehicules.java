package view;

import model.*;
import controller.*;
import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;


public class VueListeVehicules extends JPanel implements GestionnaireFlotteObserver {
    private static final long serialVersionUID = 1L;
    
    private GestionnaireFlotte gestionnaire;
    private JTable tableVehicules;
    private VehiculeTableModel modelVehicules;
    private JLabel labelInfos;
    private JButton btnAjouter, btnSupprimer, btnEntretien;
    
    public VueListeVehicules(GestionnaireFlotte gestionnaire) {
        this.gestionnaire = gestionnaire;
        gestionnaire.ajouterObservateur(this);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        modelVehicules = new VehiculeTableModel(gestionnaire.obtenirVehicules());
        tableVehicules = new JTable(modelVehicules);
        tableVehicules.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableVehicules.setRowHeight(25);
        
        JTableHeader header = tableVehicules.getTableHeader();
        header.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = tableVehicules.columnAtPoint(e.getPoint());
                modelVehicules.trierParColonne(col);
            }
            
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });
        
        JScrollPane scrollVehicules = new JScrollPane(tableVehicules);
        add(scrollVehicules, BorderLayout.CENTER);
        
        labelInfos = new JLabel();
        mettreAJourInfos();
        add(labelInfos, BorderLayout.NORTH);
        
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAjouter = new JButton("Ajouter");
        btnSupprimer = new JButton("Supprimer");
        btnEntretien = new JButton("Entretien");
        
        btnAjouter.addActionListener(e -> afficherDialogAjout());
        btnSupprimer.addActionListener(e -> supprimerVehiculeSelectionnee());
        btnEntretien.addActionListener(e -> afficherDialogEntretien());
        
        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnSupprimer);
        panneauBoutons.add(btnEntretien);
        
        add(panneauBoutons, BorderLayout.SOUTH);
    }
    
  private void mettreAJourInfos() {
        long total = gestionnaire.obtenirVehicules().size();
        long maintenance = gestionnaire.obtenirNombreVehiculesEnMaintenance();
        double taux = gestionnaire.obtenirTauxDisponibilite();
        
        labelInfos.setText(String.format(
            "Total: %d | En maintenance: %d | Disponibilité: %.1f%%",
            total, maintenance, taux
        ));
    }
    
   private void afficherDialogAjout() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ajouter un véhicule", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtImmat = new JTextField();
        JTextField txtMarque = new JTextField();
        JTextField txtModele = new JTextField();
        JTextField txtKm = new JTextField("0");
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"Léger", "Lourd", "Spécial"});
        JTextField txtCharge = new JTextField("1500");
        
        panel.add(new JLabel("Immatriculation:"));
        panel.add(txtImmat);
        panel.add(new JLabel("Marque:"));
        panel.add(txtMarque);
        panel.add(new JLabel("Modele:"));
        panel.add(txtModele);
        panel.add(new JLabel("Kilometrage:"));
        panel.add(txtKm);
        panel.add(new JLabel("Type:"));
        panel.add(cmbType);
        panel.add(new JLabel("Charge max (kg):"));
        panel.add(txtCharge);
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            try {
                String type = (String) cmbType.getSelectedItem();
                Véhicule v;
                switch (type) {
                    case "Léger":
                        v = new VéhiculeLéger(
                            txtImmat.getText(),
                            Integer.parseInt(txtKm.getText()),
                            txtMarque.getText(),
                            txtModele.getText()
                        );
                        break;
                    case "Lourd":
                        v = new VéhiculeLourd(
                            txtImmat.getText(),
                            Integer.parseInt(txtKm.getText()),
                            txtMarque.getText(),
                            txtModele.getText(),
                            Integer.parseInt(txtCharge.getText())
                        );
                        break;
                    case "Spécial":
                        v = new VéhiculeSpécial(
                            txtImmat.getText(),
                            Integer.parseInt(txtKm.getText()),
                            txtMarque.getText(),
                            txtModele.getText(),
                            3
                        );
                        break;
                    default:
                        v = null;
                        break;
                }
                
                if (v != null) {
                    gestionnaire.ajouterVehicule(v);
                    JOptionPane.showMessageDialog(dialog, "Véhicule ajouté avec succès!");
                    dialog.dispose();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur: valeurs numériques invalides", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(btnValider);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
  private void supprimerVehiculeSelectionnee() {
        int row = tableVehicules.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un véhicule");
            return;
        }
        
        Véhicule v = modelVehicules.obtenirVehicule(row);
        if (v != null) {
            gestionnaire.supprimerVehicule(v.getImmatriculation());
            JOptionPane.showMessageDialog(this, "Véhicule supprimé");
        }
    }
    
    private void afficherDialogEntretien() {
        int row = tableVehicules.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Sélectionnez un véhicule");
            return;
        }
        
        Véhicule v = modelVehicules.obtenirVehicule(row);
        if (v == null || !(v instanceof Maintenable)) {
            JOptionPane.showMessageDialog(this, "Véhicule non compatible");
            return;
        }
        
        String description = JOptionPane.showInputDialog(this, "Description de l'entretien:");
        if (description != null && !description.isEmpty()) {
            gestionnaire.signalerEntretien(v.getImmatriculation(), description);
            JOptionPane.showMessageDialog(this, "Entretien signalé");
        }
    }
    
    @Override
    public void flotteModifiee() {
        // Mise à jour thread-safe avec SwingUtilities.invokeLater()
        SwingUtilities.invokeLater(() -> {
            modelVehicules.mettreAJourDonnees(gestionnaire.obtenirVehicules());
            mettreAJourInfos();
        });
    }
}
