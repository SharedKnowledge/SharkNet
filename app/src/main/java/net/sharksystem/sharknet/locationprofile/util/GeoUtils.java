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


    /**
     * @param a Gegenueberliegende Seite zum Winkel
     * @param b Links Anliegende Seite zum Winkel
     * @param c Rechts Anliegende Seite zum Winkel
     * @return Winkel in Grad
     */
    public static double calcAngleFromEdgesSphere(double a, double b, double c, double sphereRadius){
        double a2 = Math.toRadians(a/sphereRadius);
        double b2 = Math.toRadians(b/sphereRadius);
        double c2 = Math.toRadians(c/sphereRadius);
        return Math.toDegrees(Math.acos((Math.cos(a2) - (Math.cos(b2) * Math.cos(c2))) / (Math.sin(b2) * Math.sin(c2))));
    }

    /**
     * @param a Gegenueberliegende Seite zum Winkel
     * @param b Links Anliegende Seite zum Winkel
     * @param c Rechts Anliegende Seite zum Winkel
     * @return Winkel in Grad
     */
    public static double calcAngleFromEdgesSphereHalfAngle(double a, double b, double c, double sphereRadius){
        double s = (a + b + c) / 2.0;
        return Math.toDegrees(Math.asin(Math.sqrt((Math.sin(s - b) * Math.sin(s-c))/(Math.sin(b) * Math.sin(c)))) * 2.0);
    }

    /**
     * @param a Gegenueberliegende Seite zum Winkel
     * @param b Links Anliegende Seite zum Winkel
     * @param c Rechts Anliegende Seite zum Winkel
     * @return Winkel in Grad
     */
    public static double calcAngleFromEdgesPlane(double a, double b, double c){
        return Math.toDegrees(Math.acos((Math.pow(b,2) + Math.pow(c,2) - Math.pow(a,2))/(2*b*c)));
    }
}
