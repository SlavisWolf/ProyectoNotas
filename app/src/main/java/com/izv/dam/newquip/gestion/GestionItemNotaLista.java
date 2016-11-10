package com.izv.dam.newquip.gestion;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.ItemNotaLista;

/**
 * Created by alumno on 20/10/2016.
 */

public class GestionItemNotaLista extends  Gestion<ItemNotaLista> {

    public GestionItemNotaLista(Context c) {
        super(c);
    }

    public GestionItemNotaLista(Context c, boolean write) {
        super(c, write);
    }

    public ItemNotaLista get(long id) {
        String where = ContratoBaseDatos.TablaItemNotaLista._ID + " = ? ";
        String[] parametros = {String.valueOf(id)};
        Cursor c = this.getCursor(ContratoBaseDatos.TablaItemNotaLista.PROJECTION_ALL, where, parametros, null, null, ContratoBaseDatos.TablaItemNotaLista.SORT_ORDER_DEFAULT);
        if(c.getCount() > 0) {
            c.moveToFirst();
            ItemNotaLista item = ItemNotaLista.getItemNotaLista(c);
            return item;
        }
        return null;
    }

    @Override
    public long insert(ItemNotaLista objeto) {
        ContentValues valores = objeto.getContentValues();
        return this.insert(valores);
    }

    @Override
    public long insert(ContentValues objeto) {
        return this.insert(ContratoBaseDatos.TablaItemNotaLista.TABLA,objeto);
    }

    @Override
    public int deleteAll() {
        return this.deleteAll(ContratoBaseDatos.TablaItemNotaLista.TABLA);
    }

    public int delete(String condicion, String[] argumentos) {
        return this.delete(ContratoBaseDatos.TablaItemNotaLista.TABLA, condicion, argumentos);
    }

    @Override
    public int delete(ItemNotaLista objeto) {
        String where = ContratoBaseDatos.TablaItemNotaLista._ID+"=?";
        String[] argumentos = new String[]{String.valueOf(objeto.getId())};
        return this.delete(ContratoBaseDatos.TablaItemNotaLista.TABLA,where,argumentos);
    }

    @Override
    public int update(ItemNotaLista objeto) {
        ContentValues nuevosDatos = objeto.getContentValues();
        String where = ContratoBaseDatos.TablaItemNotaLista._ID +"=?";
        String[] args = new String[]{String.valueOf(objeto.getId())};
        return this.update(nuevosDatos,where,args);
    }

    @Override
    public int update(ContentValues valores, String condicion, String[] argumentos) {
        return this.update(ContratoBaseDatos.TablaItemNotaLista.TABLA,valores,condicion,argumentos);
    }

    @Override
    public Cursor getCursor() {
        return this.getCursor(ContratoBaseDatos.TablaItemNotaLista.TABLA,ContratoBaseDatos.TablaItemNotaLista.PROJECTION_ALL,ContratoBaseDatos.TablaItemNotaLista.SORT_ORDER_DEFAULT);
    }

    @Override
    public Cursor getCursor(String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return this.getCursor(ContratoBaseDatos.TablaItemNotaLista.TABLA,columns,selection,selectionArgs,groupBy,having,orderBy);
    }


}
