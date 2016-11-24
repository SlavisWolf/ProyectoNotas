package com.izv.dam.newquip.ContentProviders;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.os.Bundle;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.vistas.main.VistaQuip;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by alumno on 11/11/2016.
 */




//BUSCAR BASE DE DATOS ORM ,NEODATIS,DB4O,ORMLITE
public class ProveedorAsincrono extends AsyncQueryHandler { // NO HAY QUE SOBREESCRIBIR LOS METODOS DE START, SOLO SE PUEDE SOBREESCRIBIR EL STARTQUERY

    //TOKENS USADOS


    public  static final  int TOKEN_CAMBIO_ESTANDAR=40; //todos los cambios que tengna que notificar a quip de un cambio en la bd;
    public  static  final int TOKEN_INSERCION_NOTA =60;
    public  static final  int TOKEN_INSERT_LISTA=50;
    public static final int TOKEN_INSERT_ITEM =70;
    //public  static final  int TOKEN_UPDATE_LISTA=60; //borrar nota desde quip

    public ProveedorAsincrono(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
//        super.onDeleteComplete(token, cookie, result);
        if (token==TOKEN_CAMBIO_ESTANDAR) {
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_ACTUAL);
        }
    }

    @Override
    protected void onInsertComplete(int token, Object cookie, Uri uri) {
        //super.onInsertComplete(token, cookie, uri);


        if (token==TOKEN_INSERT_LISTA) { // si insertamos una lista, hay que crear sus items.
            Bundle b = (Bundle) cookie;
            long idNuevaLista=Long.parseLong(uri.getLastPathSegment());
            Nota n = b.getParcelable("nota");
            n.setId(idNuevaLista);
            ArrayList<ItemNotaLista> lista = b.getParcelableArrayList("array");
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO); //solo avisa del cambio la inserción de la nota, no los items.


            if (!lista.isEmpty()) {
                for (ItemNotaLista item : lista){
                    item.setId_NotaLista(idNuevaLista);
                    startInsert(TOKEN_INSERT_ITEM,item, ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA,item.getContentValues());
                }
            }
        }
        else if (token==TOKEN_CAMBIO_ESTANDAR) {
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO);
        }
        else if (token==TOKEN_INSERCION_NOTA){
            long id =Long.parseLong(uri.getLastPathSegment());
            ((Nota)cookie).setId(id);
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO);
        }
        else if(token==TOKEN_INSERT_ITEM) {
                long id = Long.parseLong(uri.getLastPathSegment());
                ItemNotaLista item = (ItemNotaLista) cookie;
                item.setId(id);
        }

    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        //super.onUpdateComplete(token, cookie, result);

        /*if (token==TOKEN_UPDATE_LISTA){  // NO ES NECESARIO
            Bundle b = (Bundle)cookie;
            long idLista=b.getLong("idLista");
            ItemNotaLista[] lista = (ItemNotaLista[]) b.getParcelableArray("lista");
            ItemNotaLista[] borrados = (ItemNotaLista[]) b.getParcelableArray("borrados");

            if (lista.length>0) {
                for (ItemNotaLista item : lista) {
                    if (item.getId() == 0) {
                        item.setId_NotaLista(idLista);
                        startInsert(0,null,ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getContentValues());
                    } else {
                        Uri uriItem = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getId());
                        startUpdate(0,null,uriItem, item.getContentValues(), "", null);
                    }
                }
            }
            if (borrados.length>0) { // si hemos borrado algo del array
                for (ItemNotaLista item : borrados) {
                    if (item.getId()!=0){ // en la base de datos no hay ids que sean 0 , por tanto este elemento lo hemos borrado en la edición, pero nunca lo habiamos llegado a guardar en la base de datos.
                        Uri uriItem = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA,item.getId());
                        startDelete(0,null,uriItem,"",null);
                    }
                }
            }
        }*/
        if (token==TOKEN_CAMBIO_ESTANDAR) {
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO);
        }
    }
}


/*GUARDAR HISTORIAL DE LAS NOTAS Y QUE SE VEAN SUS PUNTOS EN EL MAPA

    -ID
    -LOCALIZACIÓN
        LATITUD
        ALTITUD
    -FECHA

 */