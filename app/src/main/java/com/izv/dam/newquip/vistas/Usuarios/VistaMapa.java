package com.izv.dam.newquip.vistas.Usuarios;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.basedatos.AyudanteOrm;
import com.izv.dam.newquip.pojo.MarcaNota;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.vistas.notas.VistaNota;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by anton on 01/12/2016.
 */

public class VistaMapa extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private GoogleApiClient googleApiClient;
    private long  id_nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.activity_mapa);

        Bundle b = getIntent().getExtras();
        if (b!=null)
            id_nota = b.getLong("id_nota");
        else
            id_nota = 0;
        mapFragment.getMapAsync(this);
        init();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */


    @Override
    public void onConnected(@Nullable Bundle bundle) {

       /* // ACTUALIZAR POSICION
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);//ES EL MAS EQUILIBRADO entre precision y batería.
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);*/
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        try {
            AyudanteOrm helper = OpenHelperManager.getHelper(this, AyudanteOrm.class);
            Dao dao = helper.getMarcaNotaDao();
            List<MarcaNota> marcas;
            if (id_nota==0)
                 marcas= dao.queryForAll();
            else {
                QueryBuilder query = dao.queryBuilder();
                query.setWhere(query.where().eq(MarcaNota.ID_NOTA,id_nota));
                marcas = dao.query(query.prepare());
            }
            if (!marcas.isEmpty()) {
                LatLng posicion=null;
                for (MarcaNota marca : marcas) {
                    posicion = new LatLng(marca.getLatitud(), marca.getLongitud());
                    System.out.println(marca.toString());
                    mMap.addMarker(new MarkerOptions().position(posicion).title(marca.getTexto()));
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(posicion, 18));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void finish() { // animación
        super.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }

    /* @Override
    public void onLocationChanged(Location location) {
        LatLng punto = new LatLng(location.getLatitude(), location.getLongitude()); // Latitud y longitud.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(punto,18));
    }*/
}
