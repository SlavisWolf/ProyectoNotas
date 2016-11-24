package com.izv.dam.newquip.dialogo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.dialogo.interfaces.OnGuardarGeneralDialogListener;

/**
 * Created by alumno on 22/11/2016.
 */

public class DialogoGuardarGeneral extends DialogFragment {
    private String texto;
    private String titulo;
    private OnGuardarGeneralDialogListener listener;
    public DialogoGuardarGeneral() {}
    public static  DialogoGuardarGeneral newInstance(String titulo,String texto) {
        DialogoGuardarGeneral dialogo = new DialogoGuardarGeneral();
        Log.v("TEXTOS",titulo+texto);
        Bundle b = new Bundle();
        b.putString("texto",texto);
        b.putString("titulo",titulo);
        dialogo.setArguments(b);
        return dialogo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            texto = getArguments().getString("texto");
            titulo = getArguments().getString("titulo");
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogoParaGuardar();
    }

    public AlertDialog crearDialogoParaGuardar() {
        String titulo_dialogo= String.format("%s %s", getString(R.string.etiqueta_dialogo_guardar),titulo);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titulo_dialogo);
        builder.setMessage(texto);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onGuardarPossitiveButtonClick();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onGuardarNegativeButtonClick();
            }
        });
        AlertDialog alertGuardar = builder.create();
        return alertGuardar;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (OnGuardarGeneralDialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            " no implementó OnGuardarDialogListener");

        }
    }
}

