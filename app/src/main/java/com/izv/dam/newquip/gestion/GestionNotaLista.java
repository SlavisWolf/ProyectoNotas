/*package com.izv.dam.newquip.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.NotaLista;

/**
 * Created by alumno on 21/10/2016.
 */
/*
public class GestionNotaLista extends Gestion<NotaLista>{

    public GestionNotaLista(Context c) {
        super(c);
    }

    public GestionNotaLista(Context c, boolean write) {
        super(c, write);
    }

    public NotaLista get(long id) {
        String where = ContratoBaseDatos.TablaNotaLista._ID + " = ? ";
        String[] parametros = {String.valueOf(id)};
        Cursor c = this.getCursor(ContratoBaseDatos.TablaNotaLista.PROJECTION_ALL, where, parametros, null, null, ContratoBaseDatos.TablaNotaLista.SORT_ORDER_DEFAULT);
        if(c.getCount() > 0) {
            c.moveToFirst();
            NotaLista nota = NotaLista.getNotaLista(c);
            return nota;
        }
        return null;
    }

    @Override
    public long insert(NotaLista objeto) {
        ContentValues cv = objeto.getContentValues();
        return this.insert(cv);
    }

    @Override
    public long insert(ContentValues objeto) {
        return this.insert(ContratoBaseDatos.TablaNotaLista.TABLA,objeto);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaNotaLista.TABLA);
    }


    public int delete(String condicion, String[] argumentos) {
        return this.delete(ContratoBaseDatos.TablaNotaLista.TABLA, condicion, argumentos);
    }
    @Override
    public int delete(NotaLista objeto) {
        String where = ContratoBaseDatos.TablaNotaLista._ID +" = ?";
        String[] args = new String[]{String.valueOf(objeto.getId())};
        return this.delete(where,args);
    }

    @Override
    public int update(NotaLista objeto) {
        ContentValues cv = objeto.getContentValues();
        String where = ContratoBaseDatos.TablaNotaLista._ID +" = ?" ;
        String[] args = new String[]{String.valueOf(objeto.getId())} ;
        return this.update(cv,where,args);
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return this.update(ContratoBaseDatos.TablaNotaLista.TABLA,valores,condicion,argumentos);
    }

    @Override
    public Cursor getCursor() {
        return this.getCursor(ContratoBaseDatos.TablaNotaLista.TABLA,ContratoBaseDatos.TablaNotaLista.PROJECTION_ALL,ContratoBaseDatos.TablaNotaLista.SORT_ORDER_DEFAULT);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(ContratoBaseDatos.TablaNotaLista.TABLA,columns,selection,selectionArgs,groupBy,having,orderBy);
    }
}
*/