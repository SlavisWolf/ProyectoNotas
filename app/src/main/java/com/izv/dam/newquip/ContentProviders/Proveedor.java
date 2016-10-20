package com.izv.dam.newquip.ContentProviders;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.gestion.GestionNota;

/**
 * Created by alumno on 18/10/2016.
 */

public class Proveedor extends ContentProvider {

    public static final String AUTHORITY ="com.izv.dam.bd";
    public static final String URI_NOTA ="content://"+Proveedor.AUTHORITY+"/"+ContratoBaseDatos.TablaNota.TABLA; // el content se refiere a que el elemento es un content provider, el 2º // apartado es el id, se llama authority(contenedores.ejemplos)
    public static final Uri CONTENT_URI_NOTA = Uri.parse(Proveedor.URI_NOTA);

    private GestionNota gestionNota;


    //UriMatcher
    private static final int NOTA = 1;
    private static final int NOTA_ID = 2;
    private static final UriMatcher uriMatcher;

    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Proveedor.AUTHORITY, ContratoBaseDatos.TablaNota.TABLA, Proveedor.NOTA );
        uriMatcher.addURI(Proveedor.AUTHORITY, ContratoBaseDatos.TablaNota.TABLA+"/#", Proveedor.NOTA_ID);
    }

    @Override
    public boolean onCreate() {
        gestionNota = new GestionNota(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

            //Si es una consulta a un ID concreto construimos el WHERE
            String where = selection;
        switch( uriMatcher.match(uri) ) {
            case NOTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case NOTA : {
                Cursor c =gestionNota.getCursor(ContratoBaseDatos.TablaNota.TABLA, projection, where, selectionArgs, null, null, sortOrder);
                return c;
            }
            default: {
                return null;
            }
        }

    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);
        switch (match)
        {
            case Proveedor.NOTA:
                return "vnd.android.cursor.dir/com.izv.dam.nota";
            case Proveedor.NOTA_ID:
                return "vnd.android.cursor.item/com.izv.dam.nota";
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long regId = 1;
        switch( uriMatcher.match(uri) ) {
            case NOTA : {
                regId = gestionNota.insert(values);
                Uri newUri = ContentUris.withAppendedId(CONTENT_URI_NOTA, regId);
                Log.v("NUEVA URI",newUri.toString());
                return newUri;
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int cont;
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        switch( uriMatcher.match(uri) ) {
            case NOTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case NOTA : {
                cont = gestionNota.delete(where,selectionArgs);
                return cont;
            }
            default: {
                return 0;
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int cont;
        //Si es una consulta a un ID concreto construimos el WHERE
        String where = selection;
        switch( uriMatcher.match(uri) ) {
            case NOTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case NOTA : {
                cont = gestionNota.update(values,where,selectionArgs);
                return cont;
        }
            default: {
                return 0;
            }
        }
    }
}
