package net.sharksystem.sharknet.locationprofile.util;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class GeoUtils {
    public static double distanceBetween(double lng1, double lat1, double lng2, double lat2) {
        double r = 6371; // Earth radius in meters
        double dLong = Math.toRadians(lng2 - lng1);
        double dLat = Math.toRadians(lat2 - lat1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return r * c * 1000;
    }
}
