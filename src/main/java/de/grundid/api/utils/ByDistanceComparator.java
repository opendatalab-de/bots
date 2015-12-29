package de.grundid.api.utils;

import org.geojson.Feature;
import org.geojson.Point;

import java.util.Comparator;

public class ByDistanceComparator implements Comparator<Feature> {

    private double lat;
    private double lon;

    public ByDistanceComparator(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    @Override public int compare(Feature o1, Feature o2) {
        double distance1 = distance((Point)o1.getGeometry());
        double distance2 = distance((Point)o2.getGeometry());
        return distance1 < distance2 ? -1 : distance1 > distance2 ? 1 : o1.hashCode() - o2.hashCode();
    }

    private double distance(Point point) {
        return Utils.distFrom(lat, lon, point.getCoordinates().getLatitude(), point.getCoordinates().getLongitude());
    }
}
