package net.sharksystem.sharknet.locationprofile.geometry;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public abstract class CenterPointLocation implements GeometricLocation<Location> {
    protected Location location;

    CenterPointLocation(Location location) {
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public double distanceTo(GeometricLocation<Location> location) {
        double r = 6371; // Earth radius in meters
        double dLong = Math.toRadians(location.getLocation().getLongitude() - this.getLocation().getLongitude());
        double dLat = Math.toRadians(location.getLocation().getLatitude() - this.getLocation().getLatitude());

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(this.getLocation().getLatitude())) * Math.cos(Math.toRadians(location.getLocation().getLatitude())) *
                        Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return r * c * 1000;
    }

    public List<? extends CenterPointLocation> closeByLocations(List<? extends CenterPointLocation> locationList, double maxRadius) {
        List<CenterPointLocation> foundLocations = new ArrayList<>();
        for (CenterPointLocation location : locationList) {
            if (this.distanceTo(location) < maxRadius) {
                foundLocations.add(location);
            }
        }
        return foundLocations;
    }
}
