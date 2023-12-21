package gps;

import gps.liaison.TypeRoute;

public class LiaisonCommune {
    private final TypeRoute typeRoute;
    private final double distance;
    private final Commune destination;
    private final Commune origine;

    @Override
    public String toString() {
        return "LiaisonCommune{" +
                "typeRoute=" + typeRoute +
                ", distance=" + distance +
                ", destination=" + destination +
                ", origine=" + origine +
                '}';
    }

    public LiaisonCommune(Commune origine, Commune destination, TypeRoute typeRoute) {
        this.origine = origine;
        this.destination = destination;
        this.typeRoute = typeRoute;
        this.distance = origine.distance(destination);
    }

    public double distance() {
        return distance;
    }

    public double temps() {
        return distance / typeRoute.vitesse();
    }

    public Commune getDestination() {
        return this.destination;
    }

    public Commune getOrigine() {
        return this.origine;
    }
}
