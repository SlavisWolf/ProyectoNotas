package com.izv.dam.newquip.util;

import android.app.ProgressDialog;
import android.content.Context;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.vistas.main.VistaQuip;

/**
 * Created by alumno on 12/12/2016.
 */

public class UtilDialogos {

    public  static ProgressDialog crearDialogoProgreso(Context c){
        ProgressDialog dialogo = new ProgressDialog(c);
        dialogo.setTitle(R.string.tituloDialogoProgreso);
        dialogo.setCancelable(false);
        dialogo.setIndeterminate(true);
        dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogo.setCanceledOnTouchOutside(false);
        dialogo.setMessage(c.getString(R.string.textoDialogoProgreso));
        return dialogo;
    }
}
