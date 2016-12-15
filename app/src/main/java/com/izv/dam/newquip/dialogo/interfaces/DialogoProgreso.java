package com.izv.dam.newquip.dialogo.interfaces;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.izv.dam.newquip.R;

/**
 * Created by anton on 15/12/2016.
 */

public class DialogoProgreso extends DialogFragment {

    public DialogoProgreso() {

    }
    public static  DialogoProgreso newInstance() {
        DialogoProgreso dialogo = new DialogoProgreso();
        return dialogo;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogoProgreso();
    }

    public Dialog crearDialogoProgreso() {
        ProgressDialog dialogo = new ProgressDialog(getContext());
        dialogo.setTitle(R.string.tituloDialogoProgreso);
        dialogo.setCancelable(false);
        dialogo.setIndeterminate(true);
        dialogo.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogo.setCanceledOnTouchOutside(false);
        dialogo.setMessage(getContext().getString(R.string.textoDialogoProgreso));
        return dialogo;
    }

    @Override
    public void onDestroyView() // añadido para que no se pierda el gragmento al girar la pantalla
    {
        Dialog dialog = getDialog();
        // Work around bug: http://code.google.com/p/android/issues/detail?id=17423
        if ((dialog != null) && getRetainInstance())
            dialog.setDismissMessage(null);

        super.onDestroyView();
    }
}
