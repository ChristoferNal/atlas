package com.christofer.atlas.events;

import com.google.android.gms.common.ConnectionResult;

/**
 * @author Christoforos Nalmpantis
 *         This class contains static classes that describe different events that are happening
 *         in this application.
 */
public class Events {

    /**
     * This class updates the address when a location update happens.
     */
    public static class UpdateLocationEvent {

        // Variables.
        public String address;

        /**
         * Creates a new {@link Events.UpdateLocationEvent}.
         *
         * @param address
         */
        public UpdateLocationEvent(String address) {
            this.address = address;
        }
    }

    /**
     * This class contains the connection result, when a google play services connection fails.
     */
    public static class GooglePlayServicesConnectionFailedEvent {

        // Variables.
        public ConnectionResult connectionResult;

        /**
         * Creates a new
         * {@link Events.GooglePlayServicesConnectionFailedEvent}.
         *
         * @param connectionResult
         */
        public GooglePlayServicesConnectionFailedEvent(ConnectionResult connectionResult) {
            this.connectionResult = connectionResult;
        }
    }

}
