package net.sharksystem.sharknet.locationprofile;

import net.sharksystem.sharknet.locationprofile.geometry.GeometricLocation;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public interface LocationProfile<T extends GeometricLocation> {
    int CLOSE_POINT_RADIUS = 100;
    int INFLUENCE_POINT_RADIUS = 1000;
    int PROXIMITY_RADIUS = 1500;

    void addLocation(T location);
}
