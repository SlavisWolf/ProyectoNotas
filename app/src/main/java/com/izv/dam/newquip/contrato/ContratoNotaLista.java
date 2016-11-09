package com.izv.dam.newquip.contrato;

import android.database.Cursor;

import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;

import java.util.List;

/**
 * Created by alumno on 21/10/2016.
 */

public interface ContratoNotaLista {

    interface InterfaceModelo {

        void close();


        Cursor getItems(long id);

        int updateNota(Nota n , List<ItemNotaLista> lista, List<ItemNotaLista> borrados);

        long insertNota(Nota n ,List<ItemNotaLista> lista);
    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        Cursor getItems(Long id);

        long onSaveNota(Nota n ,List<ItemNotaLista> lista , List<ItemNotaLista> borrados);

    }

     interface InterfaceVista {
         void cargarItems(Cursor c);
         void cargarItems(List<ItemNotaLista> l);
    }

}
