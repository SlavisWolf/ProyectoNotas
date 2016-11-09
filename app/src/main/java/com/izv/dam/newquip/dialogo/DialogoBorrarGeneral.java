package com.izv.dam.newquip.dialogo;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.izv.dam.newquip.R;

/**
 * Created by anton on 07/11/2016.
 */

public class DialogoBorrarGeneral extends DialogFragment {

   private String texto;
   private String titulo;
   private Bitmap imagen;
   private  OnBorrarGeneralDialogListener listener;
   public DialogoBorrarGeneral() {

   }
   public static  DialogoBorrarGeneral newInstance(String titulo,String texto) {
      DialogoBorrarGeneral dialogo = new DialogoBorrarGeneral();
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
      return crearDialogoParaBorrar();
   }

   public AlertDialog crearDialogoParaBorrar() {
      String titulo_dialogo= String.format("%s %s", getString(R.string.etiqueta_dialogo_borrar),titulo);
      AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
      builder.setTitle(titulo_dialogo);
      builder.setMessage(getString(R.string.textoDialogoGeneral)+" "+texto);
      builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            listener.onBorrarPossitiveButtonClick();
         }
      });
      builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialogInterface, int i) {
            listener.onBorrarNegativeButtonClick();
         }
      });
      AlertDialog alertBorrar = builder.create();
      return alertBorrar;
   }

   @Override
   public void onAttach(Context context) {
      super.onAttach(context);

      try {
         listener = (OnBorrarGeneralDialogListener) context;

      } catch (ClassCastException e) {
         throw new ClassCastException(
                 context.toString() +
                         " no implementó OnBorrarDialogListener");

      }
   }
}
