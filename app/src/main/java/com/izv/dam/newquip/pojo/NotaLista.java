/*package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;

/**
 * Created by alumno on 20/10/2016.
 */


/*
public class NotaLista implements Parcelable { //PROBABLEMENTE ACABE EN DESUSO, SERA REEMPLAZADA POR EL SISTEMA DE TIPOS EN LAS NOTAS NORMALES.
    private long id;
    private String titulo;


    public NotaLista(long id, String titulo) {
        this.id = id;
        this.titulo = titulo;
    }

    public NotaLista(){
        this(0,null);
    }

    protected NotaLista(Parcel in) {
        id = in.readLong();
        titulo = in.readString();
    }

    public static final Creator<NotaLista> CREATOR = new Creator<NotaLista>() {
        @Override
        public NotaLista createFromParcel(Parcel in) {
            return new NotaLista(in);
        }

        @Override
        public NotaLista[] newArray(int size) {
            return new NotaLista[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    @Override
    public String toString() {
        return "NotaLista{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                '}';
    }

    public static NotaLista getNotaLista(Cursor c) {
        NotaLista lista = new NotaLista();
        lista.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaNotaLista._ID)));
        lista.setTitulo(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaNotaLista.TITULO)));
        return lista;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(titulo);
    }


    public ContentValues getContentValues() {
        return this.getContentValues(false);
    }
    public ContentValues getContentValues(Boolean withId) {
        ContentValues cv = new ContentValues();
        if (withId) {
            cv.put(ContratoBaseDatos.TablaNotaLista._ID,id);
        }
        cv.put(ContratoBaseDatos.TablaNotaLista.TITULO,titulo);
        return cv;
    }
}
*/