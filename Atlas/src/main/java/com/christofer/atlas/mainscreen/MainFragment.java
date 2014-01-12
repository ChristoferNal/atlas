package com.christofer.atlas.mainscreen;

import android.app.Fragment;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.christofer.atlas.R;
import com.christofer.atlas.utils.AtlasGooglePlayServicesUtils;
import com.christofer.atlas.events.Events;
import com.google.android.gms.common.ConnectionResult;

import de.greenrobot.event.EventBus;

/**
 * @author Christoforos Nalmpantis
 *         <p/>
 *         The main fragment presenting the address of the current location.
 *         TODO: Share the location via sms, facebook, twitter etc.
 */
public class MainFragment extends Fragment {

    // Constants.
    private final String className = MainFragment.class.getName();

    // Variables.
    private TextView addressTextView;
    private EventBus bus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bus = EventBus.getDefault();
        bus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.find_location_fragment, container, false);
        addressTextView = (TextView) rootView.findViewById(R.id.location_text_view);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                connectionResult.startResolutionForResult(getActivity(), AtlasGooglePlayServicesUtils.getConnectionFailureResolutionRequest());
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            AtlasGooglePlayServicesUtils.showErrorDialog(getActivity(), connectionResult.getErrorCode());
        }
    }

    /**
     * Event trigered when there is a location update.
     *
     * @param locationEvent Contains the address of the new location.
     */
    public void onEventMainThread(Events.UpdateLocationEvent locationEvent) {
        Log.v(className, "Update Location Event");
        addressTextView.setText(locationEvent.address);
    }

}
