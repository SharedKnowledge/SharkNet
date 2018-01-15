package net.sharksystem.sharknet.chat;


import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;

import net.sharkfw.knowledgeBase.geom.PointGeometry;
import net.sharksystem.sharknet.BaseActivity;
import net.sharksystem.sharknet.R;
import net.sharksystem.sharknet.data.SharkNetDbHelper;
import net.sharksystem.sharknet.locationprofile.PolygonLocationProfile;
import net.sharksystem.sharknet.locationprofile.geometry.PolygonLocation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Max Oehme (546545)
 */
public class ChatAnnotationLocationActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnCameraMoveListener {
    private static final String TAG = "ChatAnnotation";

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 0;
    private static boolean mLocationPermissionGranted = false;

    private GoogleMap mMap;

    private TextView longitureText, latitudeText;
    private ImageButton imageButtonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayoutResource(R.layout.activity_chat_annotation_location);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.chat_location_title);

        longitureText = (TextView) findViewById(R.id.longitudeText);
        latitudeText = (TextView) findViewById(R.id.lattitudeText);
        imageButtonSave = (ImageButton) findViewById(R.id.imageButtonSave);
        imageButtonSave.setOnClickListener(new OnClickSaveButton());


        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnCameraMoveListener(this);
        mMap.getUiSettings().setCompassEnabled(false);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(52.529820, 13.465224), 11));

        requestLocationPermission();
        updateLocationUI();

        drawLocationProfileGraph(mMap);


    }


    @Override
    public void onCameraMove() {
        LatLng pos = mMap.getCameraPosition().target;

        longitureText.setText(getString(R.string.chat_location_longitude, pos.longitude));
        latitudeText.setText(getString(R.string.chat_location_latitude, pos.latitude));

    }

    private void drawLocationProfileGraph(GoogleMap googleMap) {
        List<PointGeometry> points = SharkNetDbHelper.getInstance().readPointGeometryFromDB(this);

        PolygonLocation poly = PolygonLocationProfile.createConvexPolygon(points);

        List<LatLng> polygon = new ArrayList<>();
        for (PointGeometry point : poly.getCorners()){
            polygon.add(new LatLng(point.getX(),point.getY()));
        }

        googleMap.addPolygon(new PolygonOptions().addAll(polygon));

        for (PointGeometry point : points) {
            googleMap.addCircle(new CircleOptions().center(new LatLng(point.getX(),point.getY())).radius(2));
        }
    }

    private void requestLocationPermission() {
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
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    class OnClickSaveButton implements View.OnClickListener {
        @Override
        public void onClick(View view) {
//            myLineList.add(mMap.getCameraPosition().target);
//            myLine.setPoints(myLineList);

            LatLng myPos = mMap.getCameraPosition().target;
//
//            Location l = new Location("self");
//            l.setLongitude(myPos.longitude);
//            l.setLatitude(myPos.latitude);
//
//            profile.getPosibilityToReachLocation(l);
            PolygonLocationProfile prof = new PolygonLocationProfile(ChatAnnotationLocationActivity.this);
            prof.calculateSpatialInformationFromProfile(new PointGeometry(myPos.longitude, myPos.latitude));
        }
    }
}
