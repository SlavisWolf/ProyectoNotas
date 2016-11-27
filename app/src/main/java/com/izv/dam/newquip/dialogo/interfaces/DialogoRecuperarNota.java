package com.izv.dam.newquip.dialogo.interfaces;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.dialogo.DialogoBorrar;
import com.izv.dam.newquip.pojo.Nota;

/**
 * Created by anton on 27/11/2016.
 */

public class DialogoRecuperarNota extends DialogFragment {
    private Nota n;
    // Interfaz de comunicación
    OnRecuperarDialogListener listener;

    public DialogoRecuperarNota() {
    }

    public static DialogoRecuperarNota newInstance(Nota n) {
        DialogoRecuperarNota fragment = new DialogoRecuperarNota();
        Bundle args = new Bundle();
        args.putParcelable("nota",n);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            n=getArguments().getParcelable("nota");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return createDialogRecuperar();
    }
    public AlertDialog createDialogRecuperar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String titulo_dialogo;
        String texto_dialogo;

        titulo_dialogo= String.format("%s %s", getString(R.string.dialogoRecuperarTitulo),n.getTitulo());
        texto_dialogo = getString(R.string.dialogoRecuperarNotaTexto);

        builder.setTitle(titulo_dialogo);
        builder.setMessage(texto_dialogo);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onRecuperarNotaPossitiveButtonClick(n);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onRecuperarNotaNegativeButtonClick();
            }
        });
        AlertDialog alertBorrar = builder.create();
        return alertBorrar;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnRecuperarDialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            " no implementó OnBorrarDialogListener");

        }
    }

}


