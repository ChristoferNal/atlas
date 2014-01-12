package com.christofer.atlas.services;

import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import com.christofer.atlas.utils.AtlasGooglePlayServicesUtils;
import com.christofer.atlas.utils.AtlasLocationUpdateHelper;
import com.christofer.atlas.events.Events;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/**
 * @author Christoforos Nalmpantis
 *         A {@link android.app.Service} which runs in the background in the main thread
 *         and is location aware.
 */
public class BackgroundLocationService extends Service implements
        GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

    // Constants.
    private final String className = BackgroundLocationService.class.getName();

    // Variables.
    private LocationClient locationClient;
    private Location currentLocation;
    private LocationRequest locationRequest;
    private EventBus bus;
    private boolean isSetup;

    @Override
    public void onCreate() {
        super.onCreate();
        isSetup = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int result = super.onStartCommand(intent, flags, startId);
        setupService();
        return result;
    }

    @Override
    public IBinder onBind(Intent intent) {
        setupService();
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean onUnbind = super.onUnbind(intent);
        if (locationClient.isConnected()) {
            locationClient.removeLocationUpdates(this);
        }
        locationClient.disconnect();
        stopSelf();
        return onUnbind;
    }

    @Override
    public void onConnected(Bundle bundle) {
        currentLocation = locationClient.getLastLocation();
        if (currentLocation != null) {
            Log.d(className, currentLocation.toString());
        } else {
            Log.d(className, "Last location is unknown");
        }
        getAddress();
        locationClient.requestLocationUpdates(locationRequest, this);
    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        bus.post(new Events.GooglePlayServicesConnectionFailedEvent(connectionResult));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            currentLocation = location;
        }
        getAddress();
    }

    /**
     * Finds the address of the current location and runs in a background thread.
     */
    public void getAddress() {
        // Ensure that a Geocoder services is available
        if (Build.VERSION.SDK_INT >=
                Build.VERSION_CODES.GINGERBREAD
                &&
                Geocoder.isPresent()) {
            /*
             * Reverse geocoding is long-running and synchronous.
             * Run it on a background thread.
             * Pass the current addressTextView to the background task.
             * When the task finishes,
             * onPostExecute() displays the address.
             */
            (new GetAddressTask()).execute(currentLocation);
        }
    }

    private void setupService() {
        if (!isSetup) {
            locationClient = new LocationClient(getApplicationContext(), this, this);
            locationRequest = AtlasLocationUpdateHelper.
                    createLocationRequestWithPriorityHighAccuracyAndUpdateIntervals5Sec();
            boolean servicesConnected = AtlasGooglePlayServicesUtils.servicesConnected(getApplicationContext());
            Log.v(className, "Google services enabled = " + servicesConnected);
            locationClient.connect();
            bus = EventBus.getDefault();
            isSetup = true;
        }
    }

    private void postCurrentAddress(String address) {
        Log.v(className, address);
        bus.post(new Events.UpdateLocationEvent(address));
    }

    private class GetAddressTask extends AsyncTask<Location, Void, String> {

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
            Location location = params[0];
            List<Address> addresses = null;
            try {
                if (location != null) {
                    addresses = geocoder.getFromLocation(location.getLatitude(),
                            location.getLongitude(), 1);
                }
            } catch (IOException e1) {
                Log.e(className,
                        "IO Exception in getFromLocation()");
                e1.printStackTrace();
                return ("Make sure you have internet access.");
            } catch (IllegalArgumentException e2) {
                // Error message to post in the log
                String errorString = "Illegal arguments " +
                        Double.toString(location.getLatitude()) +
                        " , " +
                        Double.toString(location.getLongitude()) +
                        " passed to address service";
                Log.e(className, errorString);
                e2.printStackTrace();
                return errorString;
            }

            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String addressText = String.format("%s, %s, %s", address.getMaxAddressLineIndex() > 0 ?
                        address.getAddressLine(0) : "", address.getLocality(), address.getCountryName());
                return addressText;
            } else {
                return "No address found.";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            postCurrentAddress(s);
        }
    }

}
