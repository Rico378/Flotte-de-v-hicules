package view;

import controller.*;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.util.Enumeration;

/**
 * VueApplication : Frame principale de l'application Flotte de Véhicules.
 * Concept OOP : Vue principale qui orchestre les 3 onglets
 */
public class VueApplication extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private GestionnaireFlotte gestionnaire;
    private JTabbedPane onglets;
    
    public VueApplication(float uiScale) {
        setTitle("Gestionnaire Flotte de Véhicules");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize((int) (1000 * uiScale), (int) (700 * uiScale));
        setLocationRelativeTo(null);
        setResizable(true);
        
        gestionnaire = new GestionnaireFlotte();
        gestionnaire.charger();
        
        if (gestionnaire.obtenirVehicules().isEmpty()) {
            initialiserDonneesTest();
        }
        
        onglets = new JTabbedPane();
        onglets.setTabPlacement(JTabbedPane.TOP);
        
        VueListeVehicules vueVehicules = new VueListeVehicules(gestionnaire);
        VueTableauBord vueStats = new VueTableauBord(gestionnaire);
        VueDetailMissions vueMissions = new VueDetailMissions(gestionnaire);
        
        onglets.addTab("Véhicules", vueVehicules);
        onglets.addTab("Tableau de Bord", vueStats);
        onglets.addTab("Missions", vueMissions);
        
        add(onglets, BorderLayout.CENTER);
        
        creerMenuBar();
        creerBarreOutils();
        
        setVisible(true);
    }
    
    /**
     * Créer la barre de menu
     */
    private void creerMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu menuFichier = new JMenu("Fichier");
        
        JMenuItem itemCharger = new JMenuItem("Charger");
        itemCharger.addActionListener(e -> {
            gestionnaire.charger();
            JOptionPane.showMessageDialog(this, "Données chargées");
        });
        
        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder");
        itemSauvegarder.addActionListener(e -> {
            gestionnaire.sauvegarder();
            JOptionPane.showMessageDialog(this, "Données sauvegardées");
        });
        
        JMenuItem itemExporter = new JMenuItem("Exporter CSV");
        itemExporter.addActionListener(e -> {
            gestionnaire.exporterCSV();
            JOptionPane.showMessageDialog(this, "Export CSV terminé");
        });
        
        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> {
            gestionnaire.sauvegarder();
            System.exit(0);
        });
        
        menuFichier.add(itemCharger);
        menuFichier.add(itemSauvegarder);
        menuFichier.addSeparator();
        menuFichier.add(itemExporter);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);
        
        JMenu menuAide = new JMenu("Aide");
        JMenuItem itemAbout = new JMenuItem("À propos");
        itemAbout.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Gestionnaire Flotte de Véhicules\nVersion 1.0\n\nApplication universitaire POO Avancée",
                "À propos", JOptionPane.INFORMATION_MESSAGE);
        });
        menuAide.add(itemAbout);
        
        menuBar.add(menuFichier);
        menuBar.add(menuAide);
        
        setJMenuBar(menuBar);
    }
    
    /**
     * Créer la barre d'outils
     */
    private void creerBarreOutils() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        
        JButton btnSauvegarder = new JButton("Sauvegarder");
        btnSauvegarder.addActionListener(e -> {
            gestionnaire.sauvegarder();
            JOptionPane.showMessageDialog(this, "Sauvegardé!");
        });
        
        JButton btnAjouterChauffeur = new JButton("Ajouter Chauffeur");
        btnAjouterChauffeur.addActionListener(e -> afficherDialogAjoutChauffeur());
        
        toolBar.add(btnSauvegarder);
        toolBar.addSeparator();
        toolBar.add(btnAjouterChauffeur);
        
        add(toolBar, BorderLayout.NORTH);
    }
    
    /**
     * Afficher le dialog pour ajouter un chauffeur
     */
    private void afficherDialogAjoutChauffeur() {
        JDialog dialog = new JDialog(this, "Ajouter un chauffeur", true);
        dialog.setSize(350, 250);
        dialog.setLocationRelativeTo(this);
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextField txtNom = new JTextField();
        JTextField txtPrenom = new JTextField();
        JComboBox<String> cmbPermis = new JComboBox<>(new String[]{"B", "C", "D"});
        
        panel.add(new JLabel("Nom:"));
        panel.add(txtNom);
        panel.add(new JLabel("Prénom:"));
        panel.add(txtPrenom);
        panel.add(new JLabel("Permis:"));
        panel.add(cmbPermis);
        
        JButton btnValider = new JButton("Ajouter");
        btnValider.addActionListener(e -> {
            model.Chauffeur c = new model.Chauffeur(
                txtNom.getText(),
                txtPrenom.getText(),
                (String) cmbPermis.getSelectedItem()
            );
            gestionnaire.ajouterChauffeur(c);
            JOptionPane.showMessageDialog(dialog, "Chauffeur ajouté!");
            dialog.dispose();
        });
        
        panel.add(btnValider);
        dialog.add(panel);
        dialog.setVisible(true);
    }
    
    /**
     * Initialiser avec des données de test
     */
    private void initialiserDonneesTest() {
        System.out.println("=== Initialisation des données de test ===");
        
        gestionnaire.ajouterVehicule(new model.VéhiculeLéger("AB-123-CD", 45000, "Renault", "Scénic"));
        gestionnaire.ajouterVehicule(new model.VéhiculeLéger("EF-456-GH", 32000, "Peugeot", "206"));
        gestionnaire.ajouterVehicule(new model.VéhiculeLourd("IJ-789-KL", 120000, "Volvo", "FH16", 18000));
        gestionnaire.ajouterVehicule(new model.VéhiculeLéger("MN-012-OP", 8000, "Mercedes", "Vito"));
        gestionnaire.ajouterVehicule(new model.VéhiculeSpécial("QR-345-ST", 56000, "Renault", "Master", 4));
        
        gestionnaire.ajouterChauffeur(new model.Chauffeur("Dupont", "Jean", "B"));
        gestionnaire.ajouterChauffeur(new model.Chauffeur("Martin", "Marie", "C"));
        gestionnaire.ajouterChauffeur(new model.Chauffeur("Bernard", "Pierre", "D"));
        gestionnaire.ajouterChauffeur(new model.Chauffeur("Lemaire", "Sophie", "B"));
        
        // Ajouter des missions
        gestionnaire.ajouterMission(new model.MissionCourte("Livraison express à Lyon"));
        gestionnaire.ajouterMission(new model.MissionLongue("Transport marchandises Marseille"));
        gestionnaire.ajouterMission(new model.MissionCourte("Intervention d'urgence"));
        
        gestionnaire.signalerEntretien("IJ-789-KL", "Révision moteur");
        
        System.out.println("✓ Données de test créées");
    }
    
    /**
     * Point d'entrée du programme
     */
    public static void main(String[] args) {
        float uiScale = lireEchelleUI();
        appliquerEchelleUI(uiScale);
        SwingUtilities.invokeLater(() -> new VueApplication(uiScale));
    }

    /**
     * Lit l'echelle UI depuis -Dapp.uiScale (ex: 1.25 ou 125%).
     */
    private static float lireEchelleUI() {
        String valeur = System.getProperty("app.uiScale", "1.25").trim();
        try {
            if (valeur.endsWith("%")) {
                float percent = Float.parseFloat(valeur.substring(0, valeur.length() - 1));
                return Math.max(1.0f, percent / 100.0f);
            }
            return Math.max(1.0f, Float.parseFloat(valeur));
        } catch (NumberFormatException e) {
            return 1.25f;
        }
    }

    /**
     * Met a l'echelle toutes les polices Swing pour un rendu lisible sur ecrans haute resolution.
     */
    private static void appliquerEchelleUI(float scale) {
        if (scale <= 1.0f) {
            return;
        }

        UIDefaults defaults = UIManager.getLookAndFeelDefaults();
        Enumeration<Object> keys = defaults.keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = defaults.get(key);

            if (value instanceof FontUIResource) {
                FontUIResource font = (FontUIResource) value;
                int newSize = Math.max(12, Math.round(font.getSize2D() * scale));
                defaults.put(key, new FontUIResource(font.getName(), font.getStyle(), newSize));
            }
        }
    }
}
