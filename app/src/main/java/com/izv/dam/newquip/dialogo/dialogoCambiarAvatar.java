package com.izv.dam.newquip.dialogo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.dialogo.interfaces.OnCambiarAvatar;
import com.izv.dam.newquip.dialogo.interfaces.OnGuardarGeneralDialogListener;

/**
 * Created by alumno on 22/11/2016.
 */

public class DialogoCambiarAvatar extends DialogFragment {
    private OnCambiarAvatar listener;
    public DialogoCambiarAvatar() {}
    public static  DialogoCambiarAvatar newInstance() {
        DialogoCambiarAvatar dialogo = new DialogoCambiarAvatar();
        return dialogo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogoParaGuardar();
    }

    public AlertDialog crearDialogoParaGuardar() {
        String titulo_dialogo= String.format("%s", getString(R.string. dialogoCambiarAvatarTitulo));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo_dialogo);
        builder.setMessage(R.string.dialogoCambiarAvatarTexto);
        builder.setPositiveButton(R.string.dialogoNuevoAvatar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.nuevoAvatar();
            }
        });


        builder.setNegativeButton(R.string.dialogoBorrarAvatar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.borrarAvatar();
            }
        });

        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertGuardar = builder.create();
        return alertGuardar;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnCambiarAvatar) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            " no implementó el listener");

        }
    }
}

