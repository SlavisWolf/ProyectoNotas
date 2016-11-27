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

    public  void setPrefsNombreUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre",valor);
        editor.commit();
    }
    public  String getPrefsNombreUsuario () {
        return prefs.getString("nombre",null);
    }
    public  void setPrefsApellidosUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("apellidos",valor);
        editor.commit();
    }
    public  String getPrefsApellidosUsuario () {
        return prefs.getString("apellidos",null);
    }

    public  void setPrefsCiudadUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("ciudad",valor);
        editor.commit();
    }
    public  String getPrefsCiudadUsuario () {
        return prefs.getString("ciudad",null);
    }

    public  void setPrefsCPUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("cp",valor);
        editor.commit();
    }
    public  String getPrefsCPUsuario () {
        return prefs.getString("cp",null);
    }

    public  void setPrefsNacimientoUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nacimiento",valor);
        editor.commit();
    }
    public  String getPrefsNacimientoUsuario () {
        return prefs.getString("nacimiento",null);
    }

    public  void setPrefsEstadoCivilUsuario (int valor) { //posición del elemento de la lista.
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("estado_civil",valor);
        editor.commit();
    }
    public  int getPrefsEstadoCivilUsuario () {
        return prefs.getInt("estado_civil",0);
    }

    public  void setPrefsTelefonoUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("telefono",valor);
        editor.commit();
    }
    public  String getPrefsTelefonoUsuario () {
        return prefs.getString("telefono",null);
    }

    public  void setPrefsCorreoUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("correo",valor);
        editor.commit();
    }
    public  String getPrefsCorreoUsuario () {
        return prefs.getString("correo",null);
    }

    public  void setPrefsAvatarUsuario (String valor) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("avatar",valor);
        editor.commit();
    }
    public  String getPrefsAvatarUsuario () {
        return prefs.getString("avatar",null);
    }

    public void borrarDatosUsuario(){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("nombre",null);
        editor.putString("apellidos",null);
        editor.putString("ciudad",null);
        editor.putString("cp",null);
        editor.putInt("estado_civil",0);
        editor.putString("telefono",null);
        editor.putString("correo",null);
        editor.putString("avatar",null);
        editor.commit();
    }
}
