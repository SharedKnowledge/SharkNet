package net.sharksystem.sharknet.locationprofile;

import android.util.Pair;

import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharkfw.knowledgeBase.spatial.SharkLocationProfile;
import net.sharkfw.knowledgeBase.spatial.SpatialInformation;
import net.sharksystem.sharknet.locationprofile.data.SpatialInformationImpl;
import net.sharksystem.sharknet.locationprofile.geometry.PolygonLocation;
import net.sharksystem.sharknet.locationprofile.util.GeoUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Max on 13.01.18.
 *
 * @author Max Oehme (546545)
 */

public class PolygonLocationProfile implements SharkLocationProfile {
    private static final double POLYGONSIZE = 300;
    private LastLocation lastLocation;
    private PolygonDataProvider polygonDataProvider;
    private SharkBasicExecutor sharkBasicExecutor;

    public PolygonLocationProfile(PolygonDataProvider polygonDataProvider, LastLocation lastLocation) {
        this.lastLocation = lastLocation;
        this.polygonDataProvider = polygonDataProvider;
    }

    public PolygonLocationProfile(PolygonDataProvider polygonDataProvider, LastLocation lastLocation, SharkBasicExecutor sharkBasicExecutor) {
        this.lastLocation = lastLocation;
        this.polygonDataProvider = polygonDataProvider;
        this.sharkBasicExecutor = sharkBasicExecutor;
    }

    @Override
    public SpatialInformation createSpatialInformationFromProfile(SharkPoint sharkPoint) {
        List<SharkPoint> sharkPoints = polygonDataProvider.getPolygonData();
        SharkPoint lastLocationPoint = lastLocation.getLastLocation();

        List<PolygonLocation> polygonListProfile = new ArrayList<>();

        while (sharkPoints.size() > 0) {
            polygonListProfile.add(createConvexPolygon(sharkPoints));
        }

        double sourceToProfile = -1;
        double destinationToProfile = -1;
        PolygonLocation entrance = null;
        PolygonLocation exit = null;
        for (PolygonLocation polygon : polygonListProfile) {
            double d_p = polygon.distanceTo(sharkPoint);
            double d_s = polygon.distanceTo(lastLocationPoint);

            if (destinationToProfile == -1 || d_p < destinationToProfile) {
                destinationToProfile = d_p;
                exit = polygon;
            }

            if (sourceToProfile == -1 || d_s < sourceToProfile) {
                sourceToProfile = d_s;
                entrance = polygon;
            }
        }
        double entrenceToExit = entrance != null && exit != null ? entrance.distanceTo(exit) : -1;

        return new SpatialInformationImpl(sourceToProfile, entrenceToExit, destinationToProfile, entrance!=null?entrance.getWeight():1, exit!=null?exit.getWeight():1);
    }

    public static PolygonLocation createConvexPolygon(List<SharkPoint> pointList) {
        Pair<List<SharkPoint>, List<SharkPoint>> polyPoints = createPolygonWithJarvisMarchAlgorithm(pointList);

        pointList.removeAll(polyPoints.first);
        pointList.removeAll(polyPoints.second);
        PolygonLocation polygon = new PolygonLocation(polyPoints.first);
        polygon.setWeight(polyPoints.first.size() + polyPoints.second.size());

        return polygon;
    }

    /**
     * Creating a Polygon from a list of points using the Jarvis-March algorithm
     *
     * @param pointList
     * @return
     */
    private static Pair<List<SharkPoint>, List<SharkPoint>> createPolygonWithJarvisMarchAlgorithm(List<SharkPoint> pointList){
        List<SharkPoint> polygonPointList = new ArrayList<>();
        List<SharkPoint> insidePoints = new ArrayList<>();

        SharkPoint startPoint = pointList.get(0);
        for (SharkPoint nextPoint : pointList) {
            if (nextPoint != startPoint) {
                if (nextPoint.getY() < startPoint.getY()) {
                    startPoint = nextPoint;
                }
            }
        }
        polygonPointList.add(startPoint);

        SharkPoint currentPoint = startPoint;
        SharkPoint selectedNext = null;
        if (pointList.size() > 1) {
            selectedNext = pointList.get(1);
            double currentAlpha = 0;
            Iterator<SharkPoint> iter = pointList.iterator();
            while (iter.hasNext()) {
                SharkPoint nextPoint = iter.next();
                if (nextPoint != startPoint) {
                    double distanceToStart = GeoUtils.distanceBetween(startPoint.getY(), startPoint.getX(), nextPoint.getY(), nextPoint.getX());
                    double a = GeoUtils.distanceBetween(startPoint.getY(), (startPoint.getX()-0.001), nextPoint.getY(), nextPoint.getX());
                    double c = GeoUtils.distanceBetween(startPoint.getY(), (startPoint.getX()-0.001), startPoint.getY(), startPoint.getX());

                    double alpha = GeoUtils.calcAngleFromEdgesSphere(a, distanceToStart, c);

                    if (distanceToStart != 0) {
                        if (distanceToStart < POLYGONSIZE && alpha > currentAlpha && alpha < 180.0) {
                            selectedNext = nextPoint;
                            currentAlpha = alpha;
                        }
                    } else {
                        insidePoints.add(nextPoint);
                        iter.remove();
                    }
                }
            }
        }

        if (selectedNext != null && pointList.size() > 2) {
            polygonPointList.add(selectedNext);
            boolean check = true;
            do {
                double currentGamma = -1;
                SharkPoint addToPoint = null;
                for (SharkPoint nextPoint : pointList) {
                    if (nextPoint != currentPoint && nextPoint != selectedNext) {
                        double distanceToStart = GeoUtils.distanceBetween(startPoint.getY(), startPoint.getX(), nextPoint.getY(), nextPoint.getX());

                        if (distanceToStart < POLYGONSIZE) {
                            // Seitenkosinussatz
                            double a = GeoUtils.distanceBetween(currentPoint.getY(), currentPoint.getX(), selectedNext.getY(), selectedNext.getX());
                            double b = GeoUtils.distanceBetween(selectedNext.getY(), selectedNext.getX(), nextPoint.getY(), nextPoint.getX());
                            double c = GeoUtils.distanceBetween(nextPoint.getY(), nextPoint.getX(), currentPoint.getY(), currentPoint.getX());

                            double gamma = GeoUtils.calcAngleFromEdgesSphere(c, a, b);
                            if (startPoint == nextPoint) {
                                startPoint = nextPoint;
                            }
                            if (gamma > currentGamma && gamma < 180.0) {
                                if (addToPoint != null && !insidePoints.contains(addToPoint)) {
                                    insidePoints.add(addToPoint);
                                }
                                currentGamma = gamma;
                                addToPoint = nextPoint;
                            } else if (gamma <= currentGamma && gamma < 180 && nextPoint != startPoint) {
                                if (!insidePoints.contains(nextPoint)) {
                                    insidePoints.add(nextPoint);
                                }
                            }
                        }
                    }
                }

                if (startPoint == addToPoint || addToPoint == null || polygonPointList.contains(addToPoint)) {
                    check = false;
                }

                if (check) {
                    polygonPointList.add(addToPoint);

                    currentPoint = selectedNext;
                    selectedNext = addToPoint;
                }
            } while (check);
        }
        if (polygonPointList.size() == 2) {
            if (polygonPointList.get(0).equals(polygonPointList.get(1))){
                insidePoints.add(polygonPointList.get(1));
            }
            polygonPointList.remove(1);
        }
        return new Pair<>(polygonPointList,insidePoints);
    }

    public SharkBasicExecutor getSharkBasicExecutor() {
        return sharkBasicExecutor;
    }

    public void setSharkBasicExecutor(SharkBasicExecutor sharkBasicExecutor) {
        this.sharkBasicExecutor = sharkBasicExecutor;
    }
}
