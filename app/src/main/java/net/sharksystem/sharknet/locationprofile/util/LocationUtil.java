package net.sharksystem.sharknet.locationprofile.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.sharksystem.sharknet.locationprofile.service.LocationProfilingService;

/**
 * Created by Max on 14.01.18.
 *
 * @author Max Oehme (546545)
 */

public class LocationUtil {
    private static final LocationUtil ourInstance = new LocationUtil();
    public static LocationUtil getInstance() {
        return ourInstance;
    }

    private LocationUtil() {
    }

    public void getLastLocation(Context context, OnCompleteListener<Location> completeListener) {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> pos = client.getLastLocation();
        pos.addOnCompleteListener(completeListener);
    }
}
