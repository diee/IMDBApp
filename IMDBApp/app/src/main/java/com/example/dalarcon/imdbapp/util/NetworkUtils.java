package com.example.dalarcon.imdbapp.util;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by administrador on 10/2/17.
 */

public class NetworkUtils {

    public static boolean isWifiConnected(Context c) {
        ConnectivityManager connManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
    }
}
