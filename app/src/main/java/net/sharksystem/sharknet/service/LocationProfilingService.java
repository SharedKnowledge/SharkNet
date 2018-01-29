package net.sharksystem.sharknet.service;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.chat.ChatAnnotationLocationActivity;
import net.sharksystem.sharknet.data.SharkNetDbHelper;
import net.sharksystem.sharknet.data.dataprovider.SQLPolygonDataProvider;
import net.sharksystem.sharknet.location.LastLocationImpl;
import net.sharksystem.sharknet.locationprofile.IDataProvider;
import net.sharksystem.sharknet.profile.EntryProfileActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Oehme (546545)
 */
public class LocationProfilingService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = LocationProfilingService.class.getSimpleName();

    private static final long INTERVAL = 5000;
    private static final long FASTINTERVAL = 1000;

    private List<SharkPoint> sharkPointList = new ArrayList<>();
    private IDataProvider dataProvider = new SQLPolygonDataProvider(this);

    GoogleApiClient mLocationClient;
    LocationRequest mLocationRequest;

    int currentPrio = LocationRequest.PRIORITY_HIGH_ACCURACY;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTINTERVAL);

        mLocationRequest.setPriority(currentPrio);
        mLocationClient.connect();

        Intent notificationIntent = new Intent(this, ChatAnnotationLocationActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);


        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.locationprofile_notify_title))
                .setContentText(getString(R.string.locationprofile_notify_content))
                .setSmallIcon(R.drawable.shark_red_lowerres)
                .setColor(ContextCompat.getColor(this, R.color.cyan_500))
                //.setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(this);
        client.requestLocationUpdates(mLocationRequest, new LocationUpdateCallback(), null);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "Location Services: Connection suspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.w(TAG, "Location Services: Connection failed: " + connectionResult.getErrorMessage());
    }

    class LocationUpdateCallback extends LocationCallback {
        private Handler mmHandler = new Handler();

        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLastLocation();
            SharkPoint lastpoint = new SharkPoint(location.getLatitude(),location.getLongitude());
            sharkPointList.add(lastpoint);
            Log.e(TAG, "Point: " + lastpoint.getWKT() + " Prio: " + mLocationRequest.getPriority());

            if (sharkPointList.size() >= 50) {
                final List<SharkPoint> tmpList = new ArrayList<>(sharkPointList);
                sharkPointList.clear();

                mmHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "Saving Last " + tmpList.size() + " Points to Database");
                        dataProvider.putAllData(tmpList);
                    }
                });
            }
        }
    }
}
