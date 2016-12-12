package com.izv.dam.newquip.dialogo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;


import com.izv.dam.newquip.R;
import com.izv.dam.newquip.dialogo.interfaces.OnBorrarDialogListener;
import com.izv.dam.newquip.pojo.Nota;

/**
 * Created by Pilar on 26/09/2016.
 */

public class DialogoBorrar extends DialogFragment {
    private Nota n;
    // Interfaz de comunicación
    OnBorrarDialogListener listener;

    public DialogoBorrar() {
    }

    public static DialogoBorrar newInstance(Nota n) {
        DialogoBorrar fragment = new DialogoBorrar();
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
        return createDialogBorrar();
    }
    public AlertDialog createDialogBorrar() {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String titulo_dialogo;
        String texto_dialogo;
        if (n.isPapelera()) {
            titulo_dialogo= String.format("%s %s", getString(R.string.tituloBorrarPapelera),n.getTitulo());
            texto_dialogo = getString(R.string.textoBorrarPapelera);
        }
        else  {
            titulo_dialogo= String.format("%s %s", getString(R.string.etiqueta_dialogo_borrar),n.getTitulo());
            texto_dialogo = getString(R.string.mensaje_confirm_borrar);
        }

        builder.setTitle(titulo_dialogo);
        builder.setMessage(texto_dialogo);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               listener.onBorrarPossitiveButtonClick(n);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onBorrarNegativeButtonClick();
            }
        });
        AlertDialog alertBorrar = builder.create();
        alertBorrar.setCanceledOnTouchOutside(false); // añadido para evitar que se pueda tocar fuera de la pantalla
        return alertBorrar;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (OnBorrarDialogListener) context;

        } catch (ClassCastException e) {
            throw new ClassCastException(
                    context.toString() +
                            " no implementó OnBorrarDialogListener");

        }
    }

}

