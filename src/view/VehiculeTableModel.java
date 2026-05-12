package view;

import model.Véhicule;
import javax.swing.table.AbstractTableModel;
import java.util.*;

/**
 * VehiculeTableModel : TableModel personnalisé pour afficher les véhicules.
 * Concept OOP : Modèle MVC pour JTable, tri dynamique
 * Permet le tri au clic sur les en-têtes
 */
public class VehiculeTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 1L;
    
    private List<Véhicule> donnees;
    private String[] colonnes = {"Immatriculation", "Type", "Marque", "Modele", "Km", "Statut", "Charge"};
    private int colonneTriee = -1;
    private boolean triAscendant = true;
    
    public VehiculeTableModel(List<Véhicule> vehicules) {
        this.donnees = new ArrayList<>(vehicules);
    }
    
    @Override
    public int getRowCount() {
        return donnees.size();
    }
    
    @Override
    public int getColumnCount() {
        return colonnes.length;
    }
    
    @Override
    public String getColumnName(int columnIndex) {
        return colonnes[columnIndex];
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Véhicule v = donnees.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return v.getImmatriculation();
            case 1:
                return v.obtenirType();
            case 2:
                return v.getMarque();
            case 3:
                return v.getModele();
            case 4:
                return v.getKilometrage();
            case 5:
                return v.getStatut();
            case 6:
                return v.obtenirCapaciteCharge();
            default:
                return "";
        }
    }
    
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 4:
            case 6:
                return Integer.class;
            default:
                return String.class;
        }
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // Les cellules ne sont pas éditables directement
    }
    
    /**
     * Mettre à jour les données et rafraîchir le tableau
     */
    public void mettreAJourDonnees(List<Véhicule> vehicules) {
        this.donnees = new ArrayList<>(vehicules);
        fireTableDataChanged();
    }
    
    /**
     * Trier les données selon la colonne sélectionnée
     */
    public void trierParColonne(int colonneIndex) {
        if (colonneTriee == colonneIndex) {
            // Inverser l'ordre si on clique sur la même colonne
            triAscendant = !triAscendant;
        } else {
            colonneTriee = colonneIndex;
            triAscendant = true;
        }
        
        final int col = colonneIndex;
        final boolean ascendant = triAscendant;
        
        donnees.sort((v1, v2) -> {
            int comparaison = 0;
            Object val1 = getValueAt(donnees.indexOf(v1), col);
            Object val2 = getValueAt(donnees.indexOf(v2), col);
            
            if (val1 instanceof Integer) {
                comparaison = Integer.compare((Integer) val1, (Integer) val2);
            } else if (val1 instanceof String) {
                comparaison = ((String) val1).compareTo((String) val2);
            }
            
            return ascendant ? comparaison : -comparaison;
        });
        
        fireTableDataChanged();
    }
    
    /**
     * Obtenir le véhicule à une ligne donnée
     */
    public Véhicule obtenirVehicule(int rowIndex) {
        if (rowIndex >= 0 && rowIndex < donnees.size()) {
            return donnees.get(rowIndex);
        }
        return null;
    }
}
