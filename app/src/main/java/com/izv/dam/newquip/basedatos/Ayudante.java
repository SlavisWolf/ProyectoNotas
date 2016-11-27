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

    private static final int VERSION = 2;

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
                ContratoBaseDatos.TablaNota.TIPO +" smallint," +
                ContratoBaseDatos.TablaNota.PAPELERA +" boolean" +
                ")";
        Log.v("TABLA 1",sql);
        db.execSQL(sql);

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

        sql= "create trigger if not exists tr_borrado_items before delete on "+ContratoBaseDatos.TablaNota.TABLA+
                " for each row"+
                " when old."+ContratoBaseDatos.TablaNota.TIPO+"=5"+
                " BEGIN delete from "+ContratoBaseDatos.TablaItemNotaLista.TABLA+ " " +
                "where "+ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA+"=old."+ContratoBaseDatos.TablaNota._ID+
                "; END;";
        Log.v("TRIGGER",sql);
        db.execSQL(sql);
        db.execSQL("PRAGMA foreign_kets=ON");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

       if(newVersion==2 && oldVersion==1){
           String sql;
           sql="alter table "+ContratoBaseDatos.TablaNota.TABLA+ " add column "+ContratoBaseDatos.TablaNota.IMAGEN+" text;";
           db.execSQL(sql);
           sql="alter table "+ContratoBaseDatos.TablaNota.TABLA+ " add column "+ContratoBaseDatos.TablaNota.AUDIO+" text;";
           db.execSQL(sql);
           sql="alter table "+ContratoBaseDatos.TablaNota.TABLA+ " add column "+ContratoBaseDatos.TablaNota.TIPO+" smallint;";
           db.execSQL(sql);
           sql="alter table "+ContratoBaseDatos.TablaNota.TABLA+ " add column "+ContratoBaseDatos.TablaNota.PAPELERA+" boolean;";
           db.execSQL(sql);

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

           sql= "create trigger if not exists tr_borrado_items before delete on "+ContratoBaseDatos.TablaNota.TABLA+
                   " for each row"+
                   " when old."+ContratoBaseDatos.TablaNota.TIPO+"=5"+
                   " BEGIN delete from "+ContratoBaseDatos.TablaItemNotaLista.TABLA+ " " +
                   "where "+ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA+"=old."+ContratoBaseDatos.TablaNota._ID+
                   "; END;";
           Log.v("TRIGGER",sql);
           db.execSQL(sql);

           sql ="update nota set "+ContratoBaseDatos.TablaNota.PAPELERA+"='false'";
           db.execSQL(sql);
           db.execSQL("PRAGMA foreign_kets=ON");
       }
    }
}