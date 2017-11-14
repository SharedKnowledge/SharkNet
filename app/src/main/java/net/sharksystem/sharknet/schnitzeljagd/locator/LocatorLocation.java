package net.sharksystem.sharknet.schnitzeljagd.locator;
import android.location.Location;

import java.io.Serializable;

/**
 * <p>
 * This class extends the default android.location.Location class and additionally provides a boolean to determine whether the location
 * was acquired using the FusedLocationProviderApi or an indoor location service.
 * </p>
 */
public class LocatorLocation extends Location implements Serializable{

    private boolean indoor;

    /**
     * Constructs a new LocatorLocation.
     * @param provider The provider used to acquire this location
     */
    public LocatorLocation(String provider) {
        super(provider);
        indoor = false;
    }

    /**
     * Constructs a new LocatorLocation as a copy from a location.
     * @param l The Location to clone
     */
    public LocatorLocation(Location l) {
        super(l);
        indoor = false;
    }

    /**
     * Constructs a new LocatorLocation as a copy from another.
     * @param l The LocatorLocation to clone
     */
    public LocatorLocation(LocatorLocation l) {
        this((Location) l);
        indoor = l.isIndoor();
    }

    /**
     * Determines whether this location was acquired by an indoor location service.
     * @return true, if the Location was acquired using indoor localization, else false
     */
    public boolean isIndoor() {
        return indoor;
    }

    /**
     * Set whether this location was acquired by an indoor location service.
     * @param indoor true, if the Location was acquired using indoor localization, else false
     */
    public void setIndoor(boolean indoor) {
        this.indoor = indoor;
    }

    @Override
    public void set(Location l) {
        super.set(l);
        indoor = false;
    }

    /**
     * Sets all parameters to the same value as the given LocatorLocation.
     * @param l The LocatorLocation to get the values from
     */
    public void set(LocatorLocation l) {
        super.set((Location) l);
        indoor = l.isIndoor();
    }

    /**
     * Returns the approximate initial bearing in degrees East of true North when traveling along the
     * shortest path between this location and the given location. The shortest path is defined using
     * the WGS84 ellipsoid. Locations that are (nearly) antipodal may produce meaningless results.
     * @param l The destination LocatorLocation
     * @return The initial bearing in degrees
     */
    public float bearingTo(LocatorLocation l) {
        return super.bearingTo((Location) l);
    }

    /**
     * Returns the approximate distance in meters between this location and the given location.
     * Distance is defined using the WGS84 ellipsoid.
     * @param l The destination LocatorLocation
     * @return The approximate distance in meters
     */
    public float distanceTo(LocatorLocation l) {
        return super.distanceTo((Location) l);
    }

    @Override
    public String toString() {
        return "LocatorLocation: (Location data: (" + super.toString() + "); indoor=" + indoor + ")";
    }
}