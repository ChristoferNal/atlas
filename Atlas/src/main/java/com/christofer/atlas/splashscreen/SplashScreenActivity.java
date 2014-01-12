package com.christofer.atlas.splashscreen;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.christofer.atlas.mainscreen.MainActivity;
import com.christofer.atlas.services.BackgroundLocationService;
import com.christofer.atlas.utils.AtlasGooglePlayServicesUtils;
import com.christofer.atlas.events.Events;
import com.google.android.gms.common.ConnectionResult;

/**
 * @author Christoforos Nalmpantis
 *         Shows a splash screen with a progress bar.
 */
public class SplashScreenActivity extends FragmentActivity implements DataLoaderFragment.ProgressListener {

    // Constants.
    private static final String className = SplashScreenActivity.class.getName();
    private static final String TAG_DATA_LOADER = "dataLoader";
    private static final String TAG_SPLASH_SCREEN = "splashScreen";

    // Variables.
    public DataLoaderFragment dataLoaderFragment;
    public SplashScreenFragment splashScreenFragment;
    private BackgroundLocationService backgroundLocationService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            backgroundLocationService = (BackgroundLocationService) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, BackgroundLocationService.class));

        final FragmentManager fragmentManager = getSupportFragmentManager();
        dataLoaderFragment = (DataLoaderFragment) fragmentManager.findFragmentByTag(TAG_DATA_LOADER);
        if (dataLoaderFragment == null) {
            dataLoaderFragment = new DataLoaderFragment();
            dataLoaderFragment.setProgressListener(this);
            dataLoaderFragment.startLoading();
            fragmentManager.beginTransaction().add(dataLoaderFragment, TAG_DATA_LOADER).commit();
        } else {
            if (checkCompletionStatus()) {
                return;
            }
        }
        // Show loading fragment
        splashScreenFragment = (SplashScreenFragment) fragmentManager.findFragmentByTag(TAG_SPLASH_SCREEN);
        if (splashScreenFragment == null) {
            splashScreenFragment = new SplashScreenFragment();
            fragmentManager.beginTransaction().add(android.R.id.content, splashScreenFragment, TAG_SPLASH_SCREEN).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (dataLoaderFragment != null) {
            checkCompletionStatus();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (dataLoaderFragment != null) {
            dataLoaderFragment.removeProgressListener();
        }
    }

    /**
     * This method is triggered when a
     * {@link com.christofer.atlas.events.Events.GooglePlayServicesConnectionFailedEvent} event
     * happens and shows the respective message to the user.
     *
     * @param connectionFailedEvent
     */
    public void onEventMainThread(Events.GooglePlayServicesConnectionFailedEvent connectionFailedEvent) {
        Log.v(className, "onEventMainThread with connection result");
        ConnectionResult connectionResult = connectionFailedEvent.connectionResult;
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this,
                        AtlasGooglePlayServicesUtils.getConnectionFailureResolutionRequest());
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            AtlasGooglePlayServicesUtils.showErrorDialog(this, connectionResult.getErrorCode());
        }
    }

    public void onEventMainThread(Events.UpdateLocationEvent locationEvent) {
        Log.v(className, "onEventMainThread with locationEvent");
    }

    /**
     * Checks if data is done loading, if it is, the result is handled
     *
     * @return true if data is done loading
     */
    private boolean checkCompletionStatus() {
        if (dataLoaderFragment.hasResult()) {
            onCompletion(dataLoaderFragment.getResult());
            FragmentManager fm = getSupportFragmentManager();
            splashScreenFragment = (SplashScreenFragment) fm.findFragmentByTag(TAG_SPLASH_SCREEN);
            if (splashScreenFragment != null) {
                fm.beginTransaction().remove(splashScreenFragment).commit();
            }
            return true;
        }
        dataLoaderFragment.setProgressListener(this);
        return false;
    }

    @Override
    public void onCompletion(Double result) {
        Intent pictureListIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(pictureListIntent);
        dataLoaderFragment = null;
        finish();
    }

    @Override
    public void onProgressUpdate(int value) {
        splashScreenFragment.setProgress(value);
    }

}
