package com.izv.dam.newquip.util;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by anton on 28/11/2016.
 */

public class Localizaciones {

    public static Location devolverLocalizacionActual(AppCompatActivity app){
        if (Permisos.solicitarPermisos(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},app)) {
            Log.v("LOCALIZACIONES","ENTROOO");
            LocationManager locationManager = (LocationManager)
                    app.getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
            return location;
        }
        return null;
    }
}
