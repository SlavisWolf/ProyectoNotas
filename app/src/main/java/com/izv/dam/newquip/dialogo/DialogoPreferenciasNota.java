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

import com.izv.dam.newquip.util.PreferenciasCompartidas;

/**
 * Created by anton on 17/11/2016.
 */

public class DialogoPreferenciasNota extends DialogFragment {


    public DialogoPreferenciasNota() {

    }
    public static  DialogoPreferenciasNota newInstance() {
        DialogoPreferenciasNota dialogo = new DialogoPreferenciasNota();
        return dialogo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return crearDialogoPreferenciasNota();
    }

    public AlertDialog crearDialogoPreferenciasNota() {
        String titulo_dialogo= String.format("%s", getString(R.string.dialogoPreferenciasNotaTitulo));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final PreferenciasCompartidas prefs = new PreferenciasCompartidas(getContext());
        builder.setTitle(titulo_dialogo);
        builder.setMultiChoiceItems(prefs.PREFERENCIAS_NOTA, prefs.getPreferenciasNota(), new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                PreferenciasCompartidas prefs = new PreferenciasCompartidas(getContext());

                switch (which) {
                    case 0 : {
                        prefs.setPrefsTitulo(isChecked);
                        break;
                    }
                    case 1 : {
                        prefs.setPrefsGuardar(isChecked);
                        break;
                    }
                    case 2 : {
                        prefs.setPrefsEditable(isChecked);
                        break;
                    }
                }
            }
        });
        builder.setNeutralButton(getString(R.string.textoCerrar),new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //NO HACE NADA
            }
        });
        AlertDialog dialogoPreferencias = builder.create();
        return dialogoPreferencias;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
}

