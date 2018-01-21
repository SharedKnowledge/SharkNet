package net.sharksystem.sharknet.service;

import android.Manifest;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
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

import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharksystem.sharknet.data.SharkNetDbHelper;
import net.sharksystem.sharknet.data.dataprovider.SQLPolygonDataProvider;
import net.sharksystem.sharknet.location.LastLocationImpl;
import net.sharksystem.sharknet.locationprofile.IDataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Oehme (546545)
 */
public class LocationProfilingService extends Service {
    private static final String TAG = "LOCATIONSERVICE";

    private List<SharkPoint> sharkPointList = new ArrayList<>();
    private IDataProvider dataProvider = new SQLPolygonDataProvider(this);

    private Handler mHandler = new Handler();
    private Runnable mRunnable;


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Create");

        mRunnable = new RecordLocationThread(this);
        mHandler.post(mRunnable);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Start");

        Notification notification = new Notification.Builder(this)
                .setContentTitle("SharkNet")
                .build();
        startForeground(1, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "END");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class RecordLocationThread implements Runnable {
        private Context mmContext;
        private Handler mmHandler = new Handler();

        public RecordLocationThread(Context context) {
            this.mmContext = context;
        }

        @Override
        public void run() {
            FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(mmContext);
            if (ActivityCompat.checkSelfPermission(mmContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mmContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Location location = task.getResult();
                    SharkPoint lastpoint = new SharkPoint(location.getLatitude(),location.getLongitude());
                    sharkPointList.add(lastpoint);


                    if (sharkPointList.size() > 50) {
                        final List<SharkPoint> tmpList = new ArrayList<>(sharkPointList);
                        sharkPointList.clear();

                        mmHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                dataProvider.putAllData(tmpList);
                            }
                        });
                    }
                }
            });

            mHandler.postDelayed(this, 1000);
        }
    }
}
