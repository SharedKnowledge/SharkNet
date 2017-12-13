package net.sharksystem.sharknet.locationprofile.util;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Max on 13.12.17.
 *
 * @author Max Oehme (546545)
 */

public class Utils {
    public static double calculateDistance(LatLng startNode, LatLng endNode) {
        double r = 6371; // Earth radius in meters
        double dLong = Math.toRadians(endNode.longitude - startNode.longitude);
        double dLat = Math.toRadians(endNode.latitude - startNode.latitude);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(startNode.latitude)) * Math.cos(Math.toRadians(endNode.latitude)) *
                        Math.sin(dLong/2) * Math.sin(dLong/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return r * c * 1000;
        //return Math.sqrt(Math.pow(a<0?-1*a:a, 2)+Math.pow(b<0?-1*b:b, 2));
    }


    public static double calculateRadius(LatLng node1, LatLng node2, LatLng node3) {
        double a = projectOnPlane(calculateDistance(node1, node2));
        double b = projectOnPlane(calculateDistance(node2, node3));
        double c = projectOnPlane(calculateDistance(node1, node3));

        double alpha = Math.acos( (Math.pow(b,2) + Math.pow(c,2) - Math.pow(a,2)) / (2*b*c) );

        double radius = a / (2 * Math.sin(alpha));

        return radius;
    }

    public static double projectOnPlane(double lenght) {
        double r = 6371000;
        return 2 * r * Math.sin(lenght/(2*r));
    }
}
