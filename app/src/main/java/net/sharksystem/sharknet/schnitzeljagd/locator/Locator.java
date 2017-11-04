package net.sharksystem.sharknet.schnitzeljagd.locator;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * <p>Requires android.permission.ACCESS_FINE_LOCATION to work. Please make sure to check and request permission before using this class.</p>
 * <p>
 * This class provides methods to get the location of the Android device. It uses the FusedLocationProviderApi class to get the GPS / Network-based location.
 * Please note, that the 'FusedLocationProviderApi' class is deprecated and replaced by the connectionless 'FusedLocationProviderClient' class. However, the official
 * documentation recommends to still use this deprecated class until Google Play services version 12.0.0 is available (Early 2018). Using the new class would
 * cause the client app to crash when the Play Services are updated. See https://developer.android.com/training/location/retrieve-current.html for more details.
 * //TODO name indoor API here
 * </p>
 * @author Emil Schoenawa
 * @version 29.10.2017
 */
public class Locator implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public final static int LOCATION_SOURCE_FUSED = 0;
    public final static int LOCATION_SOURCE_INDOOR = 1;
    public final static int LOCATION_SOURCE_INDOOR_FUSED = 2;

    private GoogleApiClient googleApiClient;
    private Context context;
    private ArrayList<LocatorLocationListener> listeners;
    private boolean googleApiClientConnected = false;

    private int locationSource;

    /**
     * Constructs a new Locator instance with the default location source (Indoor if available, fallback to Fused if not).
     * @param context The context in which this class should operate (this context must have the 'ACCESS_FINE_LOCATION'-permission)
     */
    public Locator(Context context) {
        this.context = context;
        this.listeners = new ArrayList<>();
        this.locationSource = LOCATION_SOURCE_INDOOR_FUSED;
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        this.googleApiClient.connect();
    }

    /**
     * Constructs a new Locator instance.
     * @param context The context in which this class should operate (this context must have the 'ACCESS_FINE_LOCATION'-permission)
     * @param locationSource The location source (0=Fused only; 1=Indoor only; 2=Indoor if available, fallback to Fused if not)
     */
    public Locator(Context context, int locationSource) {
        this.context = context;
        this.listeners = new ArrayList<>();
        this.locationSource = locationSource;
        this.googleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        this.googleApiClient.connect();
    }

    /**
     * This method returns true if the GoogleApiClient is connected and the Locator is ready to determine the device location.
     * @return true, if the Locator is ready and the GoogleApiClient is connected, else false
     */
    public boolean isGoogleApiClientConnected() {
        return googleApiClientConnected;
    }

    /**
     * Checks if indoor locating is available.
     * @return true, if Indoor locating is available, else false
     */
    public boolean indoorAvailable() {
        //TODO
        return false;
    }

    /**
     * Returns the location source.
     * @return The location source (0=Fused only; 1=Indoor only; 2=Indoor if available, fallback to Fused if not)
     */
    public int getLocationSource() {
        return this.locationSource;
    }

    /**
     * Sets the location source.
     * @param locationSource The location source (0=Fused only; 1=Indoor only; 2=Indoor if available, fallback to Fused if not)
     */
    public void setLocationSource(int locationSource) {
        this.locationSource = locationSource;
    }

    /**
     * Returns the (last fixed) location of the device.
     * @return The location of the device
     * @throws SecurityException If the context supplied in the constructor doesn't have the required permission (ACCESS_FINE_LOCATION)
     */
    public LocatorLocation getLastLocation() throws SecurityException {
        switch (this.locationSource) {
            case LOCATION_SOURCE_FUSED:
                return getFusedLocation();
            case LOCATION_SOURCE_INDOOR:
                return getIndoorLocation();
            case LOCATION_SOURCE_INDOOR_FUSED:
            default:
                if (indoorAvailable()) {
                    return getIndoorLocation();
                }
                return getFusedLocation();
        }
    }

    /**
     * Initiates regular location updates.
     * UNFINISHED: Missing parameters
     */
    public void startLocationUpdates() {
        //TODO implement regular updates for fused location
        //TODO implement regular updates for indoor location
    }

    /**
     * Register a LocatorLocationListener for receiving location updates.
     * @param listener The listener to register
     */
    public void registerLocationListener(LocatorLocationListener listener) {
        //TODO Implement registry

    }

    /**
     * Unregister a LocatorLocationListener from receiving location updates.
     * @param listener The listener to unregister
     */
    public void unregisterLocationListener(LocatorLocationListener listener) {
        //TODO
    }

    /**
     * Method to get the location from the FusedLocationProviderApi.
     * @return The location of the device
     * @throws SecurityException If the context supplied in the constructor doesn't have the required permission (ACCESS_FINE_LOCATION)
     */
    private LocatorLocation getFusedLocation() throws SecurityException {
        //TODO Maybe add alternative location source to retrieve altitude data
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            throw new SecurityException("Permission denied! Cannot get a location fix without 'ACCESS_FINE_LOCATION'-permission!");
        }
        if (googleApiClient.isConnected()) {
            //Google Android Developer Documentation states to use deprecated FusedLocationProviderApi class until newer API-version (12.0.0) is available
            //https://developer.android.com/training/location/retrieve-current.html
            //noinspection deprecation
            LocatorLocation result = new LocatorLocation(LocationServices.FusedLocationApi.getLastLocation(googleApiClient));
            if (result != null)
                result.setProvider("locator-fused");
            return result;
        }
        else {
            Log.d("LOCATOR", "GoogleApiClient not connected!");
            return null;
        }
    }

    /**
     * Method to get the location from the indoor location service.
     * @return The location of the device
     * @throws SecurityException If the context supplied in the constructor doesn't have the required permission (ACCESS_FINE_LOCATION)
     */
    private LocatorLocation getIndoorLocation() throws SecurityException {
        //TODO Implement indoor location
        return null;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        this.googleApiClientConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
