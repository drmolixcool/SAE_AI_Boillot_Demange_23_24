package gps;

public record PointRadians(Double latitude, Double longitude) {

    public static final int EARTH_RADIUS = 6371;

    public PointRadians(PointDegree pointDegree) {
        this(Math.toRadians(pointDegree.latitude()), Math.toRadians(pointDegree.longitude()));
    }

    public double distance(PointRadians autrePoint) {
        double deltaLat = Math.abs(this.latitude - autrePoint.latitude);
        double deltaLong = Math.abs(this.longitude - autrePoint.longitude);
        double a = Math.pow(Math.sin(deltaLat / 2), 2) + Math.cos(this.latitude) * Math.cos(autrePoint.latitude) * Math.pow(Math.sin(deltaLong / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}
