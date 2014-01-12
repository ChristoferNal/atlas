package com.christofer.atlas.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.christofer.atlas.dialogs.ErrorDialogFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * @author Christoforos Nalmpantis
 *         This class contains utilities using google play services.
 */
public class AtlasGooglePlayServicesUtils {

    // Constants
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    /**
     * Check if the services are connected.
     *
     * @param activity
     * @return
     */
    public static boolean servicesConnected(Context activity) {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);
        boolean servicesConnected = false;
        if (resultCode == ConnectionResult.SUCCESS) {
            Log.d("Location Updates",
                    "Google Play services is available.");
            servicesConnected = true;
        } else {
            servicesConnected = false;
            showErrorDialog((Activity) activity, resultCode);
        }
        return servicesConnected;
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    public static void showErrorDialog(Activity activity, int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                errorCode,
                activity,
                CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {
            ErrorDialogFragment errorDialogFragment = new ErrorDialogFragment();
            errorDialogFragment.setDialog(errorDialog);
            errorDialogFragment.show(activity.getFragmentManager(), "Location updates");
        }
    }

    /**
     * Returns the connection failure resolution request.
     *
     * @return
     */
    public static int getConnectionFailureResolutionRequest() {
        return CONNECTION_FAILURE_RESOLUTION_REQUEST;
    }
}
