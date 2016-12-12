package com.izv.dam.newquip.ContentProviders;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.vistas.main.VistaQuip;
import com.izv.dam.newquip.vistas.notas.VistaNota;
import com.izv.dam.newquip.vistas.notas_lista.VistaNotaLista;

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
    public  static final  int TOKEN_RECUPERAR_NOTA=90;
    public static final int TOKEN_QUERY_BORRAR_CONJUNTO =80;
    public static  final  int TOKEN_PAPELERA=100;
    //public  static final  int TOKEN_UPDATE_LISTA=60; //borrar nota desde quip

    public ProveedorAsincrono(ContentResolver cr) {
        super(cr);
    }

    @Override
    protected void onDeleteComplete(int token, Object cookie, int result) {
//        super.onDeleteComplete(token, cookie, result);
        if (token==TOKEN_CAMBIO_ESTANDAR) {
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
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
            VistaNotaLista.LISTA_ACTUAL.marcarLocalizacionLista();
            ArrayList<ItemNotaLista> lista = b.getParcelableArrayList("array");
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO); //solo avisa del cambio la inserción de la nota, no los items.
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
            VistaQuip.ID_CURSOR_ACTUAL=VistaQuip.ID_CURSOR_TODO;


            if (!lista.isEmpty()) {
                for (ItemNotaLista item : lista){
                    item.setId_NotaLista(idNuevaLista);
                    startInsert(TOKEN_INSERT_ITEM,item, ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA,item.getContentValues());
                }
            }
        }
        else if (token==TOKEN_CAMBIO_ESTANDAR) {
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO);
            VistaQuip.ID_CURSOR_ACTUAL=VistaQuip.ID_CURSOR_TODO;
        }
        else if (token==TOKEN_INSERCION_NOTA){
            long id =Long.parseLong(uri.getLastPathSegment());
            ((Nota)cookie).setId(id);
            VistaNota.NOTA_ACTUAL.marcarLocalizacionNota();
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO);
            VistaQuip.ID_CURSOR_ACTUAL=VistaQuip.ID_CURSOR_TODO;
        }
        else if(token==TOKEN_INSERT_ITEM) {
                long id = Long.parseLong(uri.getLastPathSegment());
                ItemNotaLista item = (ItemNotaLista) cookie;
                item.setId(id);
        }

    }

    @Override
    protected void onUpdateComplete(int token, Object cookie, int result) {
        if (token==TOKEN_CAMBIO_ESTANDAR) {
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_TODO);
            VistaQuip.ID_CURSOR_ACTUAL=VistaQuip.ID_CURSOR_TODO;
        }
        else if(token==TOKEN_RECUPERAR_NOTA) {
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_ACTUAL);
        }
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        //super.onQueryComplete(token, cookie, cursor);
        if (token==TOKEN_QUERY_BORRAR_CONJUNTO) {
            while(cursor.moveToNext()) {
                Nota n = Nota.getNota(cursor);
                VistaQuip.REFERENCIA_MENU_PRINCIPAL.borrarMarcasNota(n.getId());
                n.setPapelera(true);
                Uri uri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());
                startUpdate(0,null,uri,n.getContentValues(),null,null);
            }
            AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
            VistaQuip.REFERENCIA_MENU_PRINCIPAL.reiniciarDatos(VistaQuip.ID_CURSOR_ACTUAL);
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