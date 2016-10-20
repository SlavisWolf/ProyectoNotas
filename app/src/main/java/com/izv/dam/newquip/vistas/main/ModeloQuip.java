package com.izv.dam.newquip.vistas.main;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.izv.dam.newquip.ContentProviders.Proveedor;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloQuip implements ContratoMain.InterfaceModelo {

    //private GestionNota gn = null;
    private ContentResolver cr;
    private Cursor cursor;

    public ModeloQuip(Context c) {
        cr = c.getContentResolver();
    }

    @Override
    public void close() {
       // cr.
    }

    @Override
    public long deleteNota(Nota n) {
        Uri uri = ContentUris.withAppendedId(Proveedor.CONTENT_URI_NOTA,n.getId());
        return cr.delete(uri,"",null);
    }

    @Override
    public long deleteNota(int position) {
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return this.deleteNota(n);
    }

    @Override
    public Nota getNota(int position) {
        cursor.moveToPosition(position);
        Nota n = Nota.getNota(cursor);
        return n;
    }

    @Override
    public void loadData(OnDataLoadListener listener) {
        cursor = cr.query(Proveedor.CONTENT_URI_NOTA,ContratoBaseDatos.TablaNota.PROJECTION_ALL,null,null,ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
       // Log.v("MSG",Proveedor.CONTENT_URI_NOTA.toString());
        listener.setCursor(cursor);
    }
}