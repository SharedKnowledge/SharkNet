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
    private static final double POLYGONSIZE = 1000;
    private Context mContext;
    private List<PointGeometry> pointGeometries;

    public PolygonLocationProfile(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public SpatialInformation calculateSpatialInformationFromProfile(PointGeometry pointGeometry) {
        SpatialInformation spatialInformation = new SpatialInformationImpl();
        loadLocationsFromDatabase();

        PolygonLocation poly = createPolygonWithJarvisMarchAlgorithm(pointGeometries);

        Log.e("Test", "" + poly.distanceTo(pointGeometry));
        // TODO

        return spatialInformation;
    }

    private void loadLocationsFromDatabase(){
        pointGeometries = SharkNetDbHelper.getInstance().readPointGeometryFromDB(mContext);
    }

    /**
     * Creating a Polygon from a list of points using the Jarvis-March algorithm
     *
     * @param pointList
     * @return
     */
    public static PolygonLocation createPolygonWithJarvisMarchAlgorithm(List<PointGeometry> pointList){
        List<PointGeometry> polygonPointList = new ArrayList<>();
        PointGeometry startPoint = pointList.get(0);
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
            PointGeometry compareToPoint = selectedNext;
            for (PointGeometry nextPoint : pointList) {
                if (nextPoint != startPoint && nextPoint != compareToPoint) {

                    if (nextPoint.getX() - currentPoint.getX() > selectedNext.getX() - currentPoint.getX() && nextPoint.getY() - currentPoint.getY() > selectedNext.getY() - currentPoint.getY()) {
                        selectedNext = nextPoint;
                    }
                }
            }
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
                        } else if (a == 0 || b == 0 || c == 0) {
                            addToPoint = nextPoint;
                        }
                    }
                }
                for (PointGeometry edge : polygonPointList) {
                    if (edge == addToPoint) {
                        check = false;
                    }
                }

                if (check) {
                    polygonPointList.add(addToPoint);

                    currentPoint = selectedNext;
                    selectedNext = addToPoint;
                }
            } while (check);

        }

        // TODO Remove Points within too!!
        pointList.removeAll(polygonPointList);
        PolygonLocation polygon = new PolygonLocation(polygonPointList);

        if (polygon.getCorners().size() > 2) {
            Iterator<PointGeometry> iter = pointList.iterator();
            while (iter.hasNext()) {
                if (polygon.isPointInside(iter.next())) {
                    iter.remove();
                    polygon.setWeight(polygon.getWeight()+1);
                }
            }
        }
        return polygon;
    }

    private static double calcCosinusGamma(double a, double b, double c){
        return Math.toDegrees(Math.acos((Math.cos(c) - (Math.cos(a) * Math.cos(b))) / (Math.sin(a) * Math.sin(b))));
    }
}
