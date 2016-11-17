package com.izv.dam.newquip.vistas.main;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.izv.dam.newquip.ContentProviders.Proveedor;
import com.izv.dam.newquip.ContentProviders.ProveedorAsincrono;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloQuip implements ContratoMain.InterfaceModelo {

    //private GestionNota gn = null;
    //private ContentResolver cr;
    private  ProveedorAsincrono pa;
    private Cursor cursor;



    public ModeloQuip(Context c) {
        pa = new ProveedorAsincrono(c.getContentResolver());
        //cr=c.getContentResolver();
    }

    @Override
    public void close() {

    }

    @Override
    public void deleteNota(Nota n) {
        Uri uri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());
        pa.startDelete(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,uri,"",null);
       /* if (n.getTipo()==Nota.TIPO_LISTA) { //BORRA TAMBIEN LOS ITEMS// USAREMOS UN TRIGGER MEJOR.
            String uriItems = ContratoBaseDatos.TablaItemNotaLista.URI_ITEM_NOTA_LISTA+"/"+ContratoBaseDatos.TablaNota.TABLA+"/"+uri.getLastPathSegment();//URI PARA BUSCAR LOS ELEMENTOS DE UNA LISTA
            Log.v("ITEMS_URI",uriItems);
            //pa.startDelete(0,null,Uri.parse(uriItems),"",null);
            //cr.delete(Uri.parse(uriItems),"",null);
        }*/
        //return cr.delete(uri,"",null);

        //return valor

    }

    @Override
    public void deleteNota(int position) {
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        this.deleteNota(n);
       // return this.deleteNota(n);
    }

    @Override
    public Nota getNota(int position) {
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return n;
    }

    @Override
    public void setCursor(Cursor c) {
        this.cursor=c;
    }

   /* @Override
    public void loadData(OnDataLoadListener listener) {
        cursor = cr.query(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,ContratoBaseDatos.TablaNota.PROJECTION_ALL,null,null,ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
       // Log.v("MSG",Proveedor.CONTENT_URI_NOTA.toString());
        listener.setCursor(cursor);
    }*/


}