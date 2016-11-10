package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.Settings;
import android.util.Log;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

public class Ayudante extends SQLiteOpenHelper {

    //sqlite
    //tipos de datos https://www.sqlite.org/datatype3.html
    //fechas https://www.sqlite.org/lang_datefunc.html
    //trigger https://www.sqlite.org/lang_createtrigger.html

    private static final int VERSION = 1;

    public Ayudante(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql;
        sql="create table if not exists " + ContratoBaseDatos.TablaNota.TABLA +
                " (" +
                ContratoBaseDatos.TablaNota._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaNota.TITULO + " text, " +
                ContratoBaseDatos.TablaNota.NOTA + " text, " +
                ContratoBaseDatos.TablaNota.IMAGEN +" text, " +
                ContratoBaseDatos.TablaNota.AUDIO + " text, " +
                ContratoBaseDatos.TablaNota.TIPO +" smallint" +
                ")";


        Log.v("TABLA 1",sql);
        db.execSQL(sql);

        /*sql= "create table if not exists " + ContratoBaseDatos.TablaNotaLista.TABLA +
                " (" +
                ContratoBaseDatos.TablaNotaLista._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaNotaLista.TITULO + " text" +
                ")";

        Log.v("TABLA 2",sql);
        db.execSQL(sql);*/

        sql = "create table if not exists " + ContratoBaseDatos.TablaItemNotaLista.TABLA+
                " (" +
                ContratoBaseDatos.TablaItemNotaLista._ID + " integer primary key autoincrement , " +
                ContratoBaseDatos.TablaItemNotaLista.TEXTO + " text, " +
                ContratoBaseDatos.TablaItemNotaLista.MARCADO + " boolean, " +
                ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA + " integer,"  +
                "FOREIGN KEY("+ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA+") REFERENCES "+ContratoBaseDatos.TablaNota.TABLA+"("+ContratoBaseDatos.TablaNota._ID+")"+
                ");";

        Log.v("TABLA 3",sql);
        db.execSQL(sql);

        db.execSQL("PRAGMA foreign_kets=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql="drop table if exists " + ContratoBaseDatos.TablaNota.TABLA;
        db.execSQL(sql);
        Log.v("sql",sql);
    }
}