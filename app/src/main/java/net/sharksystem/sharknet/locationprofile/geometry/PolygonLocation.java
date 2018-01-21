package net.sharksystem.sharknet.locationprofile.geometry;

import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharksystem.sharknet.locationprofile.util.GeoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class PolygonLocation implements ProfileGeometry{
    private List<SharkPoint> corners = new ArrayList<>();
    private int weight = 0;

    public PolygonLocation(List<SharkPoint> corners) {
        this.corners = corners;
        this.weight = corners.size();
    }

    public List<SharkPoint> getCorners() {
        return corners;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public double distanceTo(SharkPoint location) {
        double distance = -1;

        for(int i=0; i<corners.size();i++) {
            SharkPoint pointA = corners.get(i);
            SharkPoint pointB = corners.get(i % (corners.size()-1));

            double a = GeoUtils.distanceBetween(pointB.getY(), pointB.getX(), location.getY(), location.getX());
            double b = GeoUtils.distanceBetween(pointA.getY(), pointA.getX(), location.getY(), location.getX());
            double c = GeoUtils.distanceBetween(pointA.getY(), pointA.getX(), pointB.getY(), pointB.getX());

            double beta = GeoUtils.calcAngleFromEdgesSphere(b, a, c);
            double alpha = GeoUtils.calcAngleFromEdgesSphere(a,b,c);

            double currentDistance;
            if (alpha < 90 && beta < 90){
                currentDistance = Math.asin(Math.sin(a / GeoUtils.EARTHRADIUS) * Math.sin(Math.toRadians(beta)));
            } else {
                currentDistance = GeoUtils.distanceBetween(pointA.getY(), pointA.getX(), location.getY(), location.getX());
            }

            if (currentDistance < distance || distance == -1) {
                distance = currentDistance;
            }
        }

        return distance;
    }

    public double distanceTo(PolygonLocation polygon) {
        double distance = -1;
        for (SharkPoint point : polygon.getCorners()) {
            double d = this.distanceTo(point);
            if (distance == -1 || d < distance) {
                distance = d;
            }
        }
        return distance;
    }
}
