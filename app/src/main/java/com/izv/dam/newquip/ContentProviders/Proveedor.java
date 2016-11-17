package com.izv.dam.newquip.ContentProviders;

import android.content.AsyncQueryHandler;
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
import com.izv.dam.newquip.gestion.GestionItemNotaLista;
import com.izv.dam.newquip.gestion.GestionNota;


/**
 * Created by alumno on 18/10/2016.
 */


//AsyncQueryHandler http://codetheory.in/using-asyncqueryhandler-to-access-content-providers-asynchronously-in-android/
public class Proveedor extends ContentProvider {



    private GestionNota gestionNota;
    //private GestionNotaLista gestionNotaLista;
    private GestionItemNotaLista gestionItemNotaLista;


    //UriMatcher
    private static final int NOTA = 1;
    private static final int NOTA_ID = 2;

    /*private static final int NOTA_LISTA = 3;
    private static final int NOTA_LISTA_ID = 4;*/

    private static final int ITEM_NOTA_LISTA = 3;
    private static final int ITEM_NOTA_LISTA_ID = 4;
    private static final int ITEM_NOTA_LISTA_NOTA_ID = 5; //BUSCAR TODOS LOS ELEMENTOS DE UNA NOTA CONCRETA.
    private static final UriMatcher uriMatcher;

    //Inicializamos el UriMatcher
    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaNota.TABLA, Proveedor.NOTA );
        uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaNota.TABLA+"/#", Proveedor.NOTA_ID);
        /*uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaNotaLista.TABLA, Proveedor.NOTA_LISTA );
        uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaNotaLista.TABLA+"/#", Proveedor.NOTA_LISTA_ID);*/
        uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaItemNotaLista.TABLA, Proveedor.ITEM_NOTA_LISTA );
        uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaItemNotaLista.TABLA+"/#", Proveedor.ITEM_NOTA_LISTA_ID);
        uriMatcher.addURI(ContratoBaseDatos.AUTHORITY, ContratoBaseDatos.TablaItemNotaLista.TABLA+"/"+ContratoBaseDatos.TablaNota.TABLA+"/#", Proveedor.ITEM_NOTA_LISTA_NOTA_ID);
    }

    @Override
    public boolean onCreate() {
        gestionNota = new GestionNota(getContext());
        //gestionNotaLista = new GestionNotaLista(getContext());
        gestionItemNotaLista = new GestionItemNotaLista(getContext());
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
                Cursor c =gestionNota.getCursor(projection, where, selectionArgs, null, null, sortOrder);
                return c;
            }
            /*case NOTA_LISTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case NOTA_LISTA : {
                Cursor c =gestionNotaLista.getCursor(projection, where, selectionArgs, null, null, sortOrder);
                return c;
            }*/
            case ITEM_NOTA_LISTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
                Cursor c =gestionItemNotaLista.getCursor(projection, where, selectionArgs, null, null, sortOrder);
                return c;
            }
            case ITEM_NOTA_LISTA_NOTA_ID : {
                where = ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA+"=" + uri.getLastPathSegment();
            }
            case ITEM_NOTA_LISTA : {
                Cursor c =gestionItemNotaLista.getCursor(projection, where, selectionArgs, null, null, sortOrder);
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
            /*case Proveedor.NOTA_LISTA:
                return "vnd.android.cursor.item/com.izv.dam.nota_lista";
            case Proveedor.NOTA_LISTA_ID:
                return "vnd.android.cursor.item/com.izv.dam.nota_lista";*/
            case Proveedor.ITEM_NOTA_LISTA:
                return "vnd.android.cursor.item/com.izv.dam.item_nota_lista";
            case Proveedor.ITEM_NOTA_LISTA_ID:
                return "vnd.android.cursor.item/com.izv.dam.item_nota_lista";
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
                Uri newUri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, regId);
                Log.v("NUEVA URI",newUri.toString());
                //getContext().getContentResolver().notifyChange(uri,null);
                return newUri;
            }

            /*case NOTA_LISTA : {
                regId = gestionNotaLista.insert(values);
                Uri newUri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNotaLista.CONTENT_URI_NOTA_LISTA, regId);
                Log.v("NUEVA URI",newUri.toString());
               //getContext().getContentResolver().notifyChange(uri,null);
                return newUri;
            }*/
            case ITEM_NOTA_LISTA : {
                regId = gestionItemNotaLista.insert(values);
                Uri newUri = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, regId);
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
                //notificarCambio(uri,cont);
                return cont;
            }
           /* case NOTA_LISTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case NOTA_LISTA : {
                cont = gestionNotaLista.delete(where,selectionArgs);
                //notificarCambio(uri,cont);
                return cont;
            }*/

            case ITEM_NOTA_LISTA_NOTA_ID : { //BORRA TODOS
                where = ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA+"=" + uri.getLastPathSegment();
                cont = gestionItemNotaLista.delete(where,selectionArgs);
                return cont;
            }

            case ITEM_NOTA_LISTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case ITEM_NOTA_LISTA : {
                cont = gestionItemNotaLista.delete(where,selectionArgs);
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
                //notificarCambio(uri,cont);
                return cont;
            }

            /*case NOTA_LISTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case NOTA_LISTA : {
                cont = gestionNotaLista.update(values,where,selectionArgs);
                //notificarCambio(uri,cont);
                return cont;
            }*/

            case ITEM_NOTA_LISTA_ID : {
                where = "_id=" + uri.getLastPathSegment();
            }
            case ITEM_NOTA_LISTA : {
                cont = gestionItemNotaLista.update(values,where,selectionArgs);
                return cont;
            }
            default: {
                return 0;
            }
        }
    }

    private void notificarCambio(Uri uri,int cont){ // al final se hace en el adaptador.
            if (cont>0) {
                getContext().getContentResolver().notifyChange(uri,null);
            }
    }
}
