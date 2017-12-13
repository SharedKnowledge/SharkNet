package net.sharksystem.sharknet.locationprofile.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

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

            if (count < 480) {
                Log.e(TAG, "Loop" + count);
                handler.postDelayed(this, 60000);
            } else {
                stopSelf();
            }

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
