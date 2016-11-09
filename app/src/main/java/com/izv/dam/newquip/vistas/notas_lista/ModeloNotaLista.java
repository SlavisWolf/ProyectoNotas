package com.izv.dam.newquip.vistas.notas_lista;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoNotaLista;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;

import java.util.List;

/**
 * Created by alumno on 25/10/2016.
 */

public class ModeloNotaLista implements ContratoNotaLista.InterfaceModelo {

    ContentResolver cr;

    public ModeloNotaLista(Context c) {
        cr= c.getContentResolver();
    }


    @Override
    public void close() {
    }

    @Override
    public Cursor getItems(long id) {
        String uri = ContratoBaseDatos.TablaItemNotaLista.URI_ITEM_NOTA_LISTA+"/"+ContratoBaseDatos.TablaNota.TABLA+"/"+String.valueOf(id);
        //Log.v("URI",uri.toString());
        return cr.query(Uri.parse(uri),ContratoBaseDatos.TablaItemNotaLista.PROJECTION_ALL,"",null,ContratoBaseDatos.TablaItemNotaLista.SORT_ORDER_DEFAULT);
    }

    @Override
    public int updateNota(Nota n, List<ItemNotaLista> lista, List<ItemNotaLista> borrados) {
        Uri uriNota = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());
        int num = cr.update(uriNota,n.getContentValues(),"",null);
        long idNota = n.getId();
        if (!lista.isEmpty()) {
            for (ItemNotaLista item : lista) {
                if (item.getId() == 0) {
                    item.setId_NotaLista(idNota);
                    cr.insert(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getContentValues());
                } else {
                    Uri uriItem = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getId());
                    cr.update(uriItem, item.getContentValues(), "", null);
                }
            }
        }
       if (!borrados.isEmpty()) { // si hemos borrado algo del array list.
           for (ItemNotaLista item : borrados) {
               if (item.getId()!=0){ // en la base de datos no hay ids que sean 0 , por tanto este elemento lo hemos borrado en la edición, pero nunca lo habiamos llegado a guardar en la base de datos.
                   Uri uriItem = ContentUris.withAppendedId(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA,item.getId());
                   cr.delete(uriItem,"",null);
               }
           }
       }
        return num ; //devuelve el num de notas actualizadas, no deberia cambiar de 1 xD
    }

    @Override
    public long insertNota(Nota n, List<ItemNotaLista> lista) {
        Uri uriNewNota=cr.insert(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getContentValues()); //INSERTAMOS LA NOTA
        long idNota= Long.parseLong(uriNewNota.getLastPathSegment());// la id te la da la base de datos la primera vez que insertamos la nota

        if (!lista.isEmpty()) {
            for (ItemNotaLista item : lista) {
                item.setId_NotaLista(idNota); // tenemos que saber a que nota pertenece el elemento, aqui es donde se referencia.
                cr.insert(ContratoBaseDatos.TablaItemNotaLista.CONTENT_URI_ITEM_NOTA_LISTA, item.getContentValues());
            }
        }
        return idNota; // DEVUELVE EL ID DE  LA NOTA SOLO
    }


}

