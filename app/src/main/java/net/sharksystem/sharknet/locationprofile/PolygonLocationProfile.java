package net.sharksystem.sharknet.locationprofile;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.sharkfw.knowledgeBase.geom.PointGeometry;
import net.sharkfw.knowledgeBase.spatial.LocationProfile;
import net.sharkfw.knowledgeBase.spatial.SpatialInformation;
import net.sharksystem.sharknet.data.SharkNetDbHelper;
import net.sharksystem.sharknet.locationprofile.geometry.PolygonLocation;
import net.sharksystem.sharknet.locationprofile.data.SpatialInformationImpl;
import net.sharksystem.sharknet.locationprofile.service.LocationProfilingService;
import net.sharksystem.sharknet.locationprofile.util.GeoUtils;
import net.sharksystem.sharknet.locationprofile.util.LocationUtil;

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
    private Intent profilingService;

    public PolygonLocationProfile(Context mContext) {
        this.mContext = mContext;
    }

    public PolygonLocationProfile(Context mContext, Class<?> profilingServiceClass) {
        this.mContext = mContext;
        startProfilingService(profilingServiceClass);
    }

    public void startProfilingService(Class<?> profilingServiceClass) {
        profilingService = new Intent(mContext, profilingServiceClass);
        mContext.startService(profilingService);
    }

    public void stopProfilingService() {
        mContext.stopService(profilingService);
        profilingService = null;
    }

    @Override
    public SpatialInformation calculateSpatialInformationFromProfile(PointGeometry pointGeometry) {
        loadLocationsFromDatabase();

        final PointGeometry[] lastLocation = {null};
        LocationUtil.getInstance().getLastLocation(mContext, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                lastLocation[0] = new PointGeometry(location.getLatitude(),location.getLongitude());
            }
        });

        while(lastLocation[0] == null) {
            SystemClock.sleep(100);
        }

        List<PolygonLocation> polygonListProfile = new ArrayList<>();



        // TODO Remove Points within too!!

        double sourceToProfile = -1;
        double destinationToProfile = -1;
        PolygonLocation entrance = null;
        PolygonLocation exit = null;
        for (PolygonLocation polygon : polygonListProfile) {
            double d_p = polygon.distanceTo(pointGeometry);
            double d_s = polygon.distanceTo(lastLocation[0]);

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
