package net.sharksystem.sharknet.locationprofile.geometry;

import net.sharkfw.knowledgeBase.geom.PointGeometry;
import net.sharksystem.sharknet.locationprofile.util.GeoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class PolygonLocation implements ProfileGeometry{
    private List<PointGeometry> corners = new ArrayList<>();
    private int weight = 0;

    public PolygonLocation(List<PointGeometry> corners) {
        this.corners = corners;
        this.weight = corners.size();
    }

    public List<PointGeometry> getCorners() {
        return corners;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public double distanceTo(PointGeometry location) {
        double distance = -1;

        for(int i=0; i<corners.size();i++) {
            PointGeometry pointA = corners.get(i);
            PointGeometry pointB = corners.get(i % (corners.size()-1));

            double a = Math.toRadians(GeoUtils.distanceBetween(pointB.getY(), pointB.getX(), location.getY(), location.getX()));
            double b = Math.toRadians(GeoUtils.distanceBetween(pointA.getY(), pointA.getX(), location.getY(), location.getX()));
            double c = Math.toRadians(GeoUtils.distanceBetween(pointA.getY(), pointA.getX(), pointB.getY(), pointB.getX()));

            double beta = Math.toDegrees(Math.acos((Math.cos(b) - Math.cos(a) * Math.cos(c)) /(Math.sin(a) * Math.sin(c))));
            double alpha = Math.toDegrees(Math.acos((Math.cos(a) - Math.cos(b) * Math.cos(c)) /(Math.sin(b) * Math.sin(c))));

            double currentDistance;
            if (alpha < 90 && beta < 90){
                currentDistance = Math.asin(Math.sin(a) * Math.sin(Math.toRadians(beta)));
            } else {
                currentDistance = GeoUtils.distanceBetween(pointA.getY(), pointA.getX(), location.getY(), location.getX());
            }

            if (currentDistance < distance || distance == -1) {
                distance = currentDistance;
            }
        }

        return distance;
    }

    public boolean isPointInside(PointGeometry point) {
        int t = -1;
        for (int i=0; i<corners.size();i++) {
            t = t * kreuzProdukt(point, corners.get(i), corners.get(i % (corners.size() - 1)));
            if (t == 0) {
                i = corners.size();
            }
        }
        return t == 1;
    }

    // TODO Eventuell fehlerhaft
    private int kreuzProdukt(PointGeometry point, PointGeometry cornerA, PointGeometry cornerB) {
        PointGeometry a = point, b = cornerA, c = cornerB;
        if (a.getY() == b.getY() && b.getY() == c.getY()) {
            if ((b.getX() <= a.getX() && a.getX() <= c.getX()) || (c.getX() <= a.getX() && a.getX() <= b.getX())) {
                return 0;
            } else {
                return 1;
            }
        }
        if (a.getY() == b.getY() && a.getX() == b.getX()) {
            return 0;
        }
        if (b.getY() > c.getY()) {
            PointGeometry tmp = b;
            b = c;
            c = tmp;
        }
        if (a.getY() <= b.getY() || a.getY() > c.getY()) {
            return 1;
        }
        double delta = (b.getX()-a.getX()) * (c.getY()-a.getY()) - (b.getY()-a.getY()) * (c.getX()-a.getX());
        if (delta > 0) {
            return -1;
        } else if (delta < 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
