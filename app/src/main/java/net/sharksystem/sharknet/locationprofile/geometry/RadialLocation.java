package net.sharksystem.sharknet.locationprofile.geometry;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public class RadialLocation extends CenterPointLocation {
    private double radius;
    private List<Location> containingLocations;
    private int weight;
    private List<Location> proximityLocations;

    public RadialLocation(Location location) {
        this(location, 0);
    }

    public RadialLocation(Location location, double radius) {
        super(location);
        this.containingLocations = new ArrayList<>();
        this.proximityLocations = new ArrayList<>();
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public List<Location> getContainingLocations() {
        return containingLocations;
    }

    public void setContainingLocations(List<Location> containingLocations) {
        this.containingLocations = containingLocations;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<Location> getProximityLocations() {
        return proximityLocations;
    }

    public void setProximityLocations(List<Location> proximityLocations) {
        this.proximityLocations = proximityLocations;
    }
}
