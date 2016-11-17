package com.izv.dam.newquip.util;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.vistas.notas.VistaNota;

/**
 * Created by dam on 27/10/16.
 */

public class Permisos {

    private static final int SOLICITUD_PERMISO = 1;
    public static final int GALERIA = 200;
    public static final int GUARDAR_DIBUJO = 202;
    public static final int HACER_FOTO = 201;
    //private static boolean permisos = false;
   /* private static Context c; NO ES NECESARIO CONSTRUCTOR EN UNA CLASE CON METODOS ESTATICOS.

    public Permisos ( Context c ) {
        this.c = c;
    }*/

    public static boolean solicitarPermisos( String[] permisos,AppCompatActivity actividad){  //SI PASAMOS LA ACTIVIDAD, EL CODIGO FUNCIONARA TAMBIEN EN OTRAS CLASES
        boolean isPer = false;
        //Context c = actividad;
        for ( String permiso : permisos ) {
            //isPer = ActivityCompat.checkSelfPermission((VistaNota)c, permiso) == PackageManager.PERMISSION_GRANTED;
            isPer = ActivityCompat.checkSelfPermission(actividad, permiso) == PackageManager.PERMISSION_GRANTED;
        }
        if (!isPer){
           solicitarPermisoHacerLlamada(permisos,actividad);
        }
        return isPer;
    }

    private static void solicitarPermisoHacerLlamada( String[] permisos, AppCompatActivity actividad) {
        //Pedimos el permiso o los permisos con un cuadro de dialogo del sistema
        ActivityCompat.requestPermissions(actividad, permisos, SOLICITUD_PERMISO);
    }

}
