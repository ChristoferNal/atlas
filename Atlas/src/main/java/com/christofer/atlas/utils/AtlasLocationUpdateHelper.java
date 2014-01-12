package com.christofer.atlas.utils;

import com.google.android.gms.location.LocationRequest;

/**
 * @author Christoforos Nalmpantis
 *         This helper class provides utilities for creating custom location requests.
 */
public class AtlasLocationUpdateHelper {

    // Constants.
    private static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;
    private static final long FASTEST_INTERVAL = MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    // Variables.
    private static LocationRequest locationRequest;

    /**
     * Creates a new {@link: LocationRequest} with priority high accuracy, update intervals 5 sec
     * and fastest interval 1 sec.
     *
     * @return LocationRequest
     */
    public static LocationRequest createLocationRequestWithPriorityHighAccuracyAndUpdateIntervals5Sec() {
        if (locationRequest == null) {
            locationRequest = LocationRequest.create();
        }
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);

        return locationRequest;
    }

}
