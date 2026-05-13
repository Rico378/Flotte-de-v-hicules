package model;

public abstract class Vehicule implements Maintenable, Assignable {
    protected String immatriculation;
    protected String marque;
    protected int kilometrage;
    protected boolean disponible;

    public Vehicule(String immatriculation, String marque, int kilometrage) {
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.kilometrage = kilometrage;
        this.disponible = true;
    }

    public abstract double calculerCoutAssurance();

    public String getImmatriculation() { return immatriculation; }
    public int getKilometrage() { return kilometrage; }
    
}