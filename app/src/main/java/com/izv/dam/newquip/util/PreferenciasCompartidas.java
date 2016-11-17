package com.izv.dam.newquip.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.izv.dam.newquip.R;

/**
 * Created by anton on 15/11/2016.
 */

public class PreferenciasCompartidas {

    private SharedPreferences   prefs;

    public final String[] PREFERENCIAS_NOTA; // usado en el dialogo

    public  PreferenciasCompartidas(Context c) {
        PREFERENCIAS_NOTA = new String[]{c.getString(R.string.menuQuipTituloObligatorio),c.getString(R.string.menuQuipGuardarAutomatico),c.getString(R.string.menuQuipNotaEditable)};
        prefs = c.getSharedPreferences("QuipPreferences",c.MODE_PRIVATE); //sSOLO ESTA APLICACIÓN TIENE ACCESO A LAS PREFERENCIAS.
    }

    public boolean  isPrefsGuardar(){
        return  prefs.getBoolean("guardar",false);
    }

    public boolean  isPrefsTitulo(){
        return  prefs.getBoolean("titulo",true);
    }

    public boolean  isPrefsEditable(){
        return  prefs.getBoolean("editar",false);
    }

    public void  setPrefsGuardar(boolean valor){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("guardar",valor);
        editor.commit();

    }

    public void  setPrefsTitulo(boolean valor){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("titulo",valor);
        editor.commit();
    }

    public void  setPrefsEditable(boolean valor){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("editar",valor);
        editor.commit();
    }

    public boolean[] getPreferenciasNota(){
       return new boolean[]{isPrefsTitulo(),isPrefsGuardar(),isPrefsEditable()};
    }
}
