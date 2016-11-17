package com.izv.dam.newquip.vistas.notas;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;

import com.izv.dam.newquip.ContentProviders.ProveedorAsincrono;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.gestion.GestionNota;
import com.izv.dam.newquip.pojo.Nota;

public class ModeloNota implements ContratoNota.InterfaceModelo { // modificado para usar el proovedor asíncrono

   // private GestionNota gn = null;
    private ContentResolver cr;
    private ProveedorAsincrono pa ;
    public ModeloNota(Context c) {
        //gn = new GestionNota(c);
        cr = c.getContentResolver();
        pa= new ProveedorAsincrono(c.getContentResolver());
    }

    @Override
    public void close() {
        //gn.close();
    }

    @Override
    public Nota getNota(long id) {
        Uri uri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,id);
        return Nota.getNota(cr.query(uri,ContratoBaseDatos.TablaNota.PROJECTION_ALL,null,null,ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT)); // esto no se puede hacer asincrono, esperamos que devuelva un valor.
       // return gn.get(id);
    }

    @Override
    public void saveNota(Nota n) {
        //long r;
        if(n.getId()==0) {
              this.insertNota(n);
        } else {
             this.updateNota(n);
        }
       // return r;
    }

    /*private long deleteNota(Nota n) {
        return gn.delete(n);
    }*/

    private void insertNota(Nota n) {
        /*if(n.getNota().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            return 0;
        }
        return gn.insert(n);*/
        pa.startInsert(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getContentValues());
    }

    private void updateNota(Nota n) {
        /*if(n.getNota().trim().compareTo("")==0 && n.getTitulo().trim().compareTo("")==0) {
            this.deleteNota(n);
            gn.delete(n);
            return 0;
        }
        return gn.update(n);*/
        Uri uri = ContentUris.withAppendedId(ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA,n.getId());
        pa.startUpdate(ProveedorAsincrono.TOKEN_CAMBIO_ESTANDAR,null,uri,n.getContentValues(),"",null);
    }
}