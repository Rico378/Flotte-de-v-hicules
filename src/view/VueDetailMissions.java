package view;

import model.*;
import controller.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VueDetailMissions extends JPanel implements GestionnaireFlotteObserver {
    private static final long serialVersionUID = 1L;
    
    private GestionnaireFlotte gestionnaire;
    private JList<Mission> listeMissions;
    private DefaultListModel<Mission> modelList;
    private JTextArea textEvenements;
    private JButton btnAjouterMission, btnTerminerMission, btnAssigner;
    
    public VueDetailMissions(GestionnaireFlotte gestionnaire) {
        this.gestionnaire = gestionnaire;
        gestionnaire.ajouterObservateur(this);
        
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // liste des missions
        modelList = new DefaultListModel<>();
        listeMissions = new JList<>(modelList);
        listeMissions.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeMissions.addListSelectionListener(e -> afficherDetailsMission());
        
        for (Mission m : gestionnaire.obtenirMissions()) {
            modelList.addElement(m);
        }
        
        JScrollPane scrollMissions = new JScrollPane(listeMissions);
        scrollMissions.setPreferredSize(new Dimension(300, 0));
        add(scrollMissions, BorderLayout.WEST);
        
        // les détails et événements de la mission sélectionnée
        textEvenements = new JTextArea();
        textEvenements.setEditable(false);
        textEvenements.setLineWrap(true);
        textEvenements.setWrapStyleWord(true);
        JScrollPane scrollEvenements = new JScrollPane(textEvenements);
        add(scrollEvenements, BorderLayout.CENTER);
        
        //panneau de boutons
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        
        btnAjouterMission = new JButton("Ajouter Mission");
        btnTerminerMission = new JButton("Terminer");
        btnAssigner = new JButton("Assigner");
        
        btnAjouterMission.addActionListener(e -> afficherDialogAjoutMission());
        btnTerminerMission.addActionListener(e -> terminerMissionSelectionnee());
        btnAssigner.addActionListener(e -> afficherDialogAssigner());
        
        panneauBoutons.add(btnAjouterMission);
        panneauBoutons.add(btnTerminerMission);
        panneauBoutons.add(btnAssigner);
        
        add(panneauBoutons, BorderLayout.SOUTH);
    }
    
   // Afficher les détails de la mission sélectionnée
    private void afficherDetailsMission() {
        Mission m = listeMissions.getSelectedValue();
        if (m == null) {
            textEvenements.setText("");
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(m.getIdMission()).append("\n");
        sb.append("Type: ").append(m.obtenirType()).append("\n");
        sb.append("Description: ").append(m.getDescription()).append("\n");
        sb.append("Statut: ").append(m.getStatut()).append("\n");
        sb.append("Début: ").append(m.getDateDebut()).append("\n");
        sb.append("Affectation: ").append(m.getAffectation() != null ? m.getAffectation() : "Non affectée").append("\n");
        sb.append("\n=== ÉVÉNEMENTS ===\n");
        
        for (String evt : m.getEvenements()) {
            sb.append(evt).append("\n");
        }
        
        textEvenements.setText(sb.toString());
        textEvenements.setCaretPosition(0);
    }
    
   // Afficher le dialog pour ajouter une nouvelle mission
    private void afficherDialogAjoutMission() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Ajouter une mission", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtDescription = new JTextField();
        JComboBox<String> cmbType = new JComboBox<>(new String[]{"Courte", "Longue"});
        
        panel.add(new JLabel("Description:"));
        panel.add(txtDescription);
        panel.add(new JLabel("Type:"));
        panel.add(cmbType);
        
        JButton btnValider = new JButton("Créer");
        btnValider.addActionListener(e -> {
            String type = (String) cmbType.getSelectedItem();
            Mission m = "Courte".equals(type) 
                ? new MissionCourte(txtDescription.getText())
                : new MissionLongue(txtDescription.getText());
            
            gestionnaire.ajouterMission(m);
            JOptionPane.showMessageDialog(dialog, "Mission ajoutée!");
            dialog.dispose();
        });
        
        panel.add(btnValider);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
   // Terminer la mission sélectionnée
    private void terminerMissionSelectionnee() {
        Mission m = listeMissions.getSelectedValue();
        if (m == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mission");
            return;
        }
        
        gestionnaire.terminerMission(m.getIdMission());
        JOptionPane.showMessageDialog(this, "Mission terminée");
    }
    
  // Afficher le dialog pour assigner un véhicule et un chauffeur à la mission sélectionnéeS
  
    private void afficherDialogAssigner() {
        Mission m = listeMissions.getSelectedValue();
        if (m == null) {
            JOptionPane.showMessageDialog(this, "Sélectionnez une mission");
            return;
        }
        
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Assigner mission", true);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        List<?> vehiculesDisponibles = gestionnaire.obtenirVehiculesDisponibles();
        JComboBox<Object> cmbVehicule = new JComboBox<>(vehiculesDisponibles.toArray());
        
        List<Chauffeur> chauffeursDisponibles = gestionnaire.obtenirChauffeursDisponibles();
        JComboBox<Chauffeur> cmbChauffeur = new JComboBox<>(chauffeursDisponibles.toArray(new Chauffeur[0]));
        
        panel.add(new JLabel("Véhicule:"));
        panel.add(cmbVehicule);
        panel.add(new JLabel("Chauffeur:"));
        panel.add(cmbChauffeur);
        
        JButton btnValider = new JButton("Assigner");
        btnValider.addActionListener(e -> {
            try {
                Véhicule v = (Véhicule) cmbVehicule.getSelectedItem();
                Chauffeur c = (Chauffeur) cmbChauffeur.getSelectedItem();
                
                if (v != null && c != null) {
                    gestionnaire.assignerMission(v.getImmatriculation(), m.getIdMission(), c.getId());
                    JOptionPane.showMessageDialog(dialog, "Mission assignée!");
                    dialog.dispose();
                }
            } catch (ChauffeurIndisponibleException ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur: " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panel.add(btnValider);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    @Override
    public void flotteModifiee() {
        SwingUtilities.invokeLater(() -> {
            modelList.clear();
            for (Mission m : gestionnaire.obtenirMissions()) {
                modelList.addElement(m);
            }
            afficherDetailsMission();
        });
    }
}
