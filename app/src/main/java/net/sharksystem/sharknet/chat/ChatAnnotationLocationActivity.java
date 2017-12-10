package net.sharksystem.sharknet.chat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import net.sharksystem.api.service.GeoTrackingService;
import net.sharksystem.sharknet.R;

import java.util.ArrayList;
import java.util.List;

public class ChatAnnotationLocationActivity  extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;

    private static boolean mLocationPermissionGranted = false;

    private GoogleMap mMap;
    private Marker mCenterMarker;
    private LatLng mLastKnownLocation;

    private TextView longitureText, latitudeText;
    private ImageButton imageButtonSave;

    private Polyline myLine;
    private List<LatLng> myLineList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_annotation_location);

        longitureText = (TextView) findViewById(R.id.longitudeText);
        latitudeText = (TextView) findViewById(R.id.lattitudeText);
        imageButtonSave = (ImageButton) findViewById(R.id.imageButtonSave);
        imageButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myLineList.add(mMap.getCameraPosition().target);
                myLine.setPoints(myLineList);
            }
        });


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent service = new Intent(this, GeoTrackingService.class);
        startService(service);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraMoveListener(this);
        mMap.getUiSettings().setCompassEnabled(false);

        LatLng pos = mMap.getCameraPosition().target;
        //mCenterMarker = mMap.addMarker(new MarkerOptions().position(pos).title("Position"));

        myLine = mMap.addPolyline(new PolylineOptions());

    }


    @Override
    public void onCameraMove() {
        LatLng pos = mMap.getCameraPosition().target;

        longitureText.setText(getString(R.string.chat_location_longitude, pos.longitude));
        latitudeText.setText(getString(R.string.chat_location_latitude, pos.latitude));

        //mCenterMarker.setPosition(pos);

        getDeviceLocation();
        updateLocationUI();

    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}
