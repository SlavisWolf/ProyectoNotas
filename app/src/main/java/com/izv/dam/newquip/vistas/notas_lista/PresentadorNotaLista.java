package com.izv.dam.newquip.vistas.notas_lista;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoNotaLista;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;

import java.util.List;

/**
 * Created by alumno on 25/10/2016.
 */

public class PresentadorNotaLista implements ContratoNotaLista.InterfacePresentador {

    private ContratoNotaLista.InterfaceVista vista;
    private ContratoNotaLista.InterfaceModelo modelo;


    public PresentadorNotaLista(VistaNotaLista vista) {
        this.vista = vista;
        this.modelo = new ModeloNotaLista((Context)vista);
    }
    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {
    }

   /* @Override
    public Cursor getItems(Long id) {
        return  modelo.getItems(id);
    }*/

    @Override
    public void onSaveNota(Nota n, List<ItemNotaLista> lista, List<ItemNotaLista> borrados) {

            Log.v("ID NOTA",n.getId()+"");

            if (n.getId()==0) { // no hay ids a 0 en la base de datos, asi que si la id es 0 es porque se ha creado nueva.
                  modelo.insertNota(n,lista);
            }
            else {
                 modelo.updateNota(n,lista,borrados); // aunque devuelve un entero, lo castea automaticamente a entero.
            }
        //return -1; //NO SE GUARDA
    }
}
