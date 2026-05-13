package model;

public class ChauffeurIndisponibleException extends Exception {
    
    public ChauffeurIndisponibleException(String message) {
        super(message);
    }
    
    public ChauffeurIndisponibleException(String message, Throwable cause) {
        super(message, cause);
    }
}
