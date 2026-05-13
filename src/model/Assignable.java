package model;

public interface Assignable {
    void assigner(Mission mission);
    
    void liberer();
    
    boolean estDisponible();
}
