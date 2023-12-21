package gps;

public class LocationError extends Exception {
    public LocationError(String adresse) {
        super("localisation de cette adresse inconnu : " + adresse);
    }
}
