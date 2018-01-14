package net.sharksystem.sharknet.locationprofile;

import android.content.Context;
import android.util.Log;

import net.sharkfw.knowledgeBase.geom.PointGeometry;
import net.sharkfw.knowledgeBase.spatial.LocationProfile;
import net.sharkfw.knowledgeBase.spatial.SpatialInformation;
import net.sharksystem.sharknet.data.SharkNetDbHelper;
import net.sharksystem.sharknet.locationprofile.geometry.PolygonLocation;
import net.sharksystem.sharknet.locationprofile.data.SpatialInformationImpl;
import net.sharksystem.sharknet.locationprofile.util.GeoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class PolygonLocationProfile implements LocationProfile {
    private static final double POLYGONSIZE = 100;
    private Context mContext;
    private List<PointGeometry> pointGeometries;

    public PolygonLocationProfile(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public SpatialInformation calculateSpatialInformationFromProfile(PointGeometry pointGeometry) {
        SpatialInformation spatialInformation = new SpatialInformationImpl();
        loadLocationsFromDatabase();


        // TODO Remove Points within too!!

        return spatialInformation;
    }

    private void loadLocationsFromDatabase(){
        pointGeometries = SharkNetDbHelper.getInstance().readPointGeometryFromDB(mContext);
    }

    public static PolygonLocation createPolygonProfile(List<PointGeometry> pointList) {
        List<PointGeometry> polyPoints = createPolygonWithJarvisMarchAlgorithm(pointList);

        pointList.removeAll(polyPoints);
        PolygonLocation polygon = new PolygonLocation(polyPoints);

        if (polygon.getCorners().size() > 2) {
            Iterator<PointGeometry> iter = pointList.iterator();
            while (iter.hasNext()) {
                if (polygon.isPointInside(iter.next())) {
                    //iter.remove();
                    polygon.setWeight(polygon.getWeight()+1);
                }
            }
        }

        return polygon;
    }

    /**
     * Creating a Polygon from a list of points using the Jarvis-March algorithm
     *
     * @param pointList
     * @return
     */
    public static List<PointGeometry> createPolygonWithJarvisMarchAlgorithm(List<PointGeometry> pointList){
        List<PointGeometry> polygonPointList = new ArrayList<>();
        PointGeometry startPoint = pointList.get(0);
        for (PointGeometry nextPoint : pointList) {
            if (nextPoint != startPoint) {
                if (nextPoint.getY() < startPoint.getY()) {
                    startPoint = nextPoint;
                }
            }
        }
        polygonPointList.add(startPoint);

        PointGeometry currentPoint = startPoint;
        PointGeometry selectedNext = null;
        for (PointGeometry nextPoint : pointList) {
            if (nextPoint != startPoint) {
                double distanceToStart = GeoUtils.distanceBetween(startPoint.getY(), startPoint.getX(), nextPoint.getY(), nextPoint.getX());

                if (distanceToStart < POLYGONSIZE) {
                    selectedNext = nextPoint;
                }
            }
        }

        if (selectedNext != null && pointList.size() > 2) {
            polygonPointList.add(selectedNext);
            boolean check = true;
            do {
                double currentGamma = 0;
                PointGeometry addToPoint = null;
                for (PointGeometry nextPoint : pointList) {
                    if (nextPoint != currentPoint && nextPoint != selectedNext) {
                        // Seitenkosinussatz
                        double a = Math.toRadians(GeoUtils.distanceBetween(currentPoint.getY(), currentPoint.getX(), selectedNext.getY(), selectedNext.getX()));
                        double b = Math.toRadians(GeoUtils.distanceBetween(selectedNext.getY(), selectedNext.getX(), nextPoint.getY(), nextPoint.getX()));
                        double c = Math.toRadians(GeoUtils.distanceBetween(nextPoint.getY(), nextPoint.getX(), currentPoint.getY(), currentPoint.getX()));

                        double distanceToStart = GeoUtils.distanceBetween(startPoint.getY(), startPoint.getX(), nextPoint.getY(), nextPoint.getX());

                        double gamma = calcCosinusGamma(a, b, c);
                        if (gamma > currentGamma && gamma < 180 && distanceToStart < POLYGONSIZE) {
                            currentGamma = gamma;
                            addToPoint = nextPoint;
                        }
                    }
                }

                if (startPoint == addToPoint) {
                    check = false;
                }

                if (check) {
                    polygonPointList.add(addToPoint);

                    currentPoint = selectedNext;
                    selectedNext = addToPoint;
                }
            } while (check);

        }
        return polygonPointList;
    }

    private static double calcCosinusGamma(double a, double b, double c){
        return Math.toDegrees(Math.acos((Math.cos(c) - (Math.cos(a) * Math.cos(b))) / (Math.sin(a) * Math.sin(b))));
    }
}
