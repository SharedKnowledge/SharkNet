package net.sharksystem.sharknet.locationprofile;

import android.location.Location;
import android.util.Log;

import net.sharksystem.sharknet.locationprofile.geometry.RadialLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public class RadialSpotLocationProfile implements LocationProfile<RadialLocation> {
    private static final String TAG = "LOCProfile";
    private static final RadialSpotLocationProfile ourInstance = new RadialSpotLocationProfile();
    public static RadialSpotLocationProfile getInstance() {
        return ourInstance;
    }

    private RadialSpotLocationProfile() {
        this.locationsList = new ArrayList<>();
    }



    private List<RadialLocation> locationsList;


    public List<RadialLocation> getLocationsList() {
        return locationsList;
    }

    @Override
    public void addLocation(RadialLocation location) {
        for (RadialLocation l : locationsList) {
            double distance = l.distanceTo(location);
            if (distance <= PROXIMITY_RADIUS) {
                l.setWeight(l.getWeight()+1);

                if (distance > CLOSE_POINT_RADIUS && distance <= INFLUENCE_POINT_RADIUS) {
                    l.getContainingLocations().add(location.getLocation());
                    if (l.getRadius() > distance) {
                        l.setRadius(distance);
                    }
                } else {
                    l.getProximityLocations().add(location.getLocation());
                }
                return;
            }
        }
        locationsList.add(location);
    }

    public double getPosibilityToReachLocation(Location location) {
        for (RadialLocation l : locationsList) {
            double distance = l.distanceTo(new RadialLocation(location));

            double p = 1 / (Math.sqrt(distance + 1));
            Log.e(TAG, String.valueOf(p));
        }

        return 0;
    }
}
