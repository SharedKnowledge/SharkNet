package net.sharksystem.sharknet.locationprofile.geometry;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public interface GeometricLocation<T> {

    T getLocation();
    void setLocation(T location);

    double distanceTo(GeometricLocation<T> location);
}
