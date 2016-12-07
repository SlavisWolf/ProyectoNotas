package com.izv.dam.newquip.util;


import android.content.Context;
import android.location.Location;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by anton on 28/11/2016.
 */


//esta clase se iniciara en las notas y nos permitira tener la localizacion más precisa posible.
public class ObtenedorDeLocalizacionActual implements   GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static  Location localizacion;
    private GoogleApiClient apiClient;

    public ObtenedorDeLocalizacionActual(Context c) {

        System.out.println("Constructor");
        if (apiClient == null) {
            apiClient = new GoogleApiClient.Builder(c)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();

            apiClient.connect(); // ERES ESTUPIDO POR NO HABER PUESTO ESTE METODO ANTONIO
            System.out.println("Entra API");
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // ACTUALIZAR POSICION
        System.out.println("onConnected");
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(2000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);//ES EL MAS EQUILIBRADO entre precision y batería.
        LocationServices.FusedLocationApi.requestLocationUpdates(apiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed");
        localizacion= LocationServices.FusedLocationApi.getLastLocation(apiClient);
    }
    @Override
    public void onLocationChanged(Location location) {
        System.out.println("onLocationChanged");
        localizacion=location;
    }
    public Location getLocalizacion() {
        return localizacion;
    }

    public  void desconectar(){
         apiClient.disconnect();
    }
}


