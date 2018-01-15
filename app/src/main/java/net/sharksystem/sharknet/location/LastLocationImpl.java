package net.sharksystem.sharknet.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import net.sharkfw.knowledgeBase.geom.SharkPoint;
import net.sharksystem.sharknet.locationprofile.LastLocation;

/**
 * Created by Max on 15.01.18.
 *
 * @author Max Oehme (546545)
 */

public class LastLocationImpl implements LastLocation {
    private Context mContext;
    private SharkPoint location = null;

    public LastLocationImpl(Context context) {
        this.mContext = context;
    }

    @Override
    public SharkPoint getLastLocation() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        Task<Location> pos = client.getLastLocation();
        pos.addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                LastLocationImpl.this.location = new SharkPoint(location.getLatitude(),location.getLongitude());
            }
        });

        while(location == null) {
            SystemClock.sleep(100);
        }
        return location;
    }
}
