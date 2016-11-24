package com.izv.dam.newquip.vistas.notas_lista;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.izv.dam.newquip.ContentProviders.ProveedorAsincrono;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoNotaLista;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alumno on 25/10/2016.
 */

public class ModeloNotaLista implements ContratoNotaLista.InterfaceModelo {

    ContentResolver cr;
    ProveedorAsincrono pa;

    public ModeloNotaLista(Context c) {
        cr= c.getContentResolver();
        pa = new ProveedorAsincrono(c.getContentResolver());
    }


    @Override
    public void close() {
    }

    /*@Override
    public Cursor getItems(long id) {
        String uri = ContratoBaseDatos.TablaItemNotaLista.URI_ITEM_NOTA_LISTA+"/"+ContratoBaseDatos.TablaNota.TABLA+"/"+String.valueOf(id);
        //Log.v("URI",uri.toString());
        return cr.query(Uri.parse(uri),ContratoBaseDatos.TablaItemNotaLista.PROJECTION_ALL,"",null,ContratoBaseDatos.TablaItemNotaLista.SORT_ORDER_DEFAULT);
    }*/

    @Override
    public void updateNota(Nota n, ArrayList<ItemNotaLista> lista, ArrayList<ItemNotaLista> borrados) {
        Uri uriNota = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());

       /* Bundle b = new Bundle(); // le pasaremos el id, y las listas.
        b.putLong("idLista",n.getId());
        b.putParcelableArray("lista",lista.toArray(new ItemNotaLista[lista.size()]));
        b.putParcelableArray("borrados",borrados.toArray(new ItemNotaLista[borrados.size()]));*/
        pa.startUpdate(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,uriNota,n.getContentValues(),"",null); // solo notificará este metodo los item de abajo no lo haran

        //int num = cr.update(uriNota,n.getContentValues(),"",null);
        long idNota = n.getId();
        if (!lista.isEmpty()) {
            for (ItemNotaLista item : lista) {
                if (item.getId() == 0) {
                    item.setId_NotaLista(idNota);
                    pa.startInsert(ProveedorAsincrono.TOKEN_INSERT_ITEM,item,ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getContentValues());
                    //cr.insert(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getContentValues());
                } else {
                    Uri uriItem = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getId());
                    pa.startUpdate(0,null,uriItem, item.getContentValues(), "", null);
                    //cr.update(uriItem, item.getContentValues(), "", null);
                }
            }
        }
       if (!borrados.isEmpty()) { // si hemos borrado algo del array list.
           for (ItemNotaLista item : borrados) {
               if (item.getId()!=0){ // en la base de datos no hay ids que sean 0 , por tanto este elemento lo hemos borrado en la edición, pero nunca lo habiamos llegado a guardar en la base de datos.
                   Uri uriItem = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA,item.getId());
                   pa.startDelete(0,null,uriItem,"",null);
                   //cr.delete(uriItem,"",null);
               }
           }
           borrados.clear(); //BORRA LOS ELEMENTOS
       }
        //return num ; //devuelve el num de notas actualizadas, no deberia cambiar de 1 xD*/
    }

    @Override
    public void insertNota(Nota n, ArrayList<ItemNotaLista> lista) {

        Bundle b= new Bundle();
        b.putParcelable("nota",n);
        b.putParcelableArrayList("array", lista);

        pa.startInsert(ProveedorAsincrono.TOKEN_INSERT_LISTA,b,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getContentValues()); // le pasamos la lista para que el proveedor se encargue de añadir los items



        //Uri uriNewNota=cr.insert(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getContentValues()); //INSERTAMOS LA NOTA
       // long idNota= Long.parseLong(uriNewNota.getLastPathSegment());// la id te la da la base de datos la primera vez que insertamos la nota

        /*if (!lista.isEmpty()) {
            for (ItemNotaLista item : lista) {
                item.setId_NotaLista(idNota); // tenemos que saber a que nota pertenece el elemento, aqui es donde se referencia.
                cr.insert(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getContentValues());
            }
        }*/
        //return idNota; // DEVUELVE EL ID DE  LA NOTA SOLO
    }


}

