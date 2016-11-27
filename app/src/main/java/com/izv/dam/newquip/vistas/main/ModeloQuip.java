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

   /* public  void  borrarConjunto(String where,boolean listas) {

        if (listas)
            where += " and "+ContratoBaseDatos.TablaNota.TIPO+"="+Nota.TIPO_LISTA;
        else
            where += " and "+ContratoBaseDatos.TablaNota.TIPO+"!="+Nota.TIPO_LISTA;

        pa.startDelete(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,where,null);
    }*/

    public  void  borrarConjunto(String where,boolean papelera) {
        if (papelera) {
            where = ContratoBaseDatos.TablaNota.PAPELERA+"=1";
            pa.startDelete(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,where,null);
        }
        else { //
            pa.startQuery(ProveedorAsincrono.TOKEN_QUERY_BORRAR_CONJUNTO,null,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,ContratoBaseDatos.TablaNota.PROJECTION_ALL,where,null,ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
        }

    }

    @Override
    public void close() {

    }

    @Override
    public void deleteNota(Nota n) { // ahora dependiendo de si la nota esta en la papelera, la borra, o la actualiza
        Uri uri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());
        if (n.isPapelera()){
            pa.startDelete(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,uri,"",null);
        }
        else {
            n.setPapelera(true);
            pa.startUpdate(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,uri,n.getContentValues(),"",null);
        }

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

    @Override
    public void recuperarNota(Nota n) {
        n.setPapelera(false);
        Uri uri =ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());
        pa.startUpdate(ProveedorAsincrono.TOKEN_RECUPERAR_NOTA,null,uri,n.getContentValues(),null,null);
    }

    /*public void borrarTodo(){
        pa.startDelete(0,null,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,null,null);
    }*/
}