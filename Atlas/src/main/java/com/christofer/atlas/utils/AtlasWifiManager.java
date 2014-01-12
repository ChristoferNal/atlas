package com.christofer.atlas.utils;

import android.content.Context;
import android.net.wifi.WifiManager;

/**
 * @author Christoforos Nalmpantis
 *         This class manages the wifi of the phone when the application runs. It is a singleton
 *         and it fits with the lifecycle of an activity.
 */
public class AtlasWifiManager {

    private WifiManager wifiManager;
    private boolean wifiWasEnabled;
    private static AtlasWifiManager whWifiManagerInstance;

    protected AtlasWifiManager(Context context) {
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiWasEnabled = wifiManager.isWifiEnabled();
    }

    /**
     * Create an instance of {@link com.christofer.atlas.utils.AtlasWifiManager}
     * if it doesn't exist.
     *
     * @param context
     * @return
     */
    public static AtlasWifiManager newInstance(Context context) {
        if (whWifiManagerInstance == null) {
            whWifiManagerInstance = new AtlasWifiManager(context);
        }
        return whWifiManagerInstance;
    }

    /**
     * It turns on the wifi of the phone if it is turned off.
     * It should be called at onCreate() of an activity.
     */
    public void onCreate() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    /**
     * It turns off the wifi of the phone if it was turned on.
     * It should be called at onDestroy() of an activity.
     */
    public void onDestroy() {
        if (wifiManager.isWifiEnabled() && (!wifiWasEnabled)) {
            wifiManager.setWifiEnabled(false);
        }
    }

}
