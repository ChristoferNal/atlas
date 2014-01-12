package com.christofer.atlas.mainscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.christofer.atlas.events.Events;
import com.christofer.atlas.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import de.greenrobot.event.EventBus;

/**
 * @author Christoforos Nalmpantis
 *         <p/>
 *         This class is responsible for presenting google maps in a fragment.
 */
public class AtlasMapFragment extends Fragment {

    // Variables.
    private GoogleMap googleMap;
    private MapView mapView;
    private Bundle bundle;
    private EventBus eventBus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.wh_map_fragment, container, false);
        try {
            MapsInitializer.initialize(getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO handle this situation
        }
        eventBus = EventBus.getDefault();
        eventBus.register(this);
        mapView = (MapView) rootView.findViewById(R.id.map);
        mapView.onCreate(bundle);
        initializeMap();

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
    }


    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        eventBus.unregister(this);
        mapView.onDestroy();
        super.onStop();
    }

    /**
     * Initialize google map.
     */
    private void initializeMap() {
        if (googleMap == null) {
            googleMap = mapView.getMap();
            googleMap.setMyLocationEnabled(true);
        }
    }

    /**
     * Event that runs on the main thread. It is triggered when an update location event is caused.
     * Provides information about the location event. In this class this event works as a cause for
     * the google maps location to change.
     *
     * @param locationEvent
     */
    public void onEventMainThread(Events.UpdateLocationEvent locationEvent) {
        CameraUpdate center;
        if (googleMap.getMyLocation() != null) {
            center = CameraUpdateFactory.newLatLng(
                    new LatLng(googleMap.getMyLocation().getLatitude(),
                            googleMap.getMyLocation().getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            googleMap.moveCamera(center);
            googleMap.animateCamera(zoom);
        }
    }

}
