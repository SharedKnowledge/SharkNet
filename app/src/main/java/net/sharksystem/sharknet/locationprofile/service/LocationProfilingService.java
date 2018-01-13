package net.sharksystem.sharknet.locationprofile.service;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.sharksystem.sharknet.locationprofile.RadialSpotLocationProfile;

/**
 * @author Max Oehme (546545)
 */
public class LocationProfilingService extends Service {
    private static final String TAG = "LOCATIONSERVICE";

    int count = 0;


    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            count++;

            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(LocationProfilingService.this);
            if (ActivityCompat.checkSelfPermission(LocationProfilingService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationProfilingService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
            pos.addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    RadialSpotLocationProfile.getInstance().getLocationUpdateListener().onLocationUpdate(task.getResult());
                }
            });


            handler.postDelayed(this, 60000);

        }
    };


    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "Create");

        handler.post(runnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "Start");

        Notification notification = new Notification.Builder(this)
                .setContentTitle("SharkNet")
                .build();
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "END");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
