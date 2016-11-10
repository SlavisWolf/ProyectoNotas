package com.izv.dam.newquip.pojo;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.util.UtilBoolean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alumno on 20/10/2016.
 */

public class ItemNotaLista implements Parcelable {

    private long id;
    private String texto;
    private boolean marcado;
    private long id_NotaLista;

    public ItemNotaLista(long id, String texto, boolean marcado, long id_NotaLista) {
        this.id = id;
        this.texto = texto;
        this.marcado = marcado;
        this.id_NotaLista = id_NotaLista;
    }

    @Override
    public String toString() {
        return "ItemNotaLista{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", marcado=" + marcado +
                ", id_NotaLista=" + id_NotaLista +
                '}';
    }

    public ItemNotaLista(){
        this(0,null,false,0);
    }

    protected ItemNotaLista(Parcel in) { // escribirlos en el mismo orden en write to parcel
        id = in.readLong();
        texto = in.readString();
        marcado = in.readByte() != 0;
        id_NotaLista = in.readLong();
    }

    public static final Creator<ItemNotaLista> CREATOR = new Creator<ItemNotaLista>() {
        @Override
        public ItemNotaLista createFromParcel(Parcel in) {
            return new ItemNotaLista(in);
        }

        @Override
        public ItemNotaLista[] newArray(int size) {
            return new ItemNotaLista[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public boolean isMarcado() {
        return marcado;
    }

    public void setMarcado(boolean marcado) {
        this.marcado = marcado;
    }

    public long getId_NotaLista() {
        return id_NotaLista;
    }

    public void setId_NotaLista(long id_NotaLista) {
        this.id_NotaLista = id_NotaLista;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(texto);
        dest.writeByte(UtilBoolean.booleanToByte(marcado));
        dest.writeLong(id_NotaLista);
    }

    public ContentValues getContentValues(){
        return this.getContentValues(false);
    }

    public ContentValues getContentValues(Boolean withId){
        ContentValues valores= new ContentValues();
        if (withId)
            valores.put(ContratoBaseDatos.TablaItemNotaLista._ID,id);
        valores.put(ContratoBaseDatos.TablaItemNotaLista.TEXTO,texto);
        valores.put(ContratoBaseDatos.TablaItemNotaLista.MARCADO,marcado);
        valores.put(ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA,id_NotaLista);
        return valores;
    }

    public static ItemNotaLista getItemNotaLista(Cursor c){
        ItemNotaLista item = new ItemNotaLista();
        item.setId(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaItemNotaLista._ID)));
        item.setTexto(c.getString(c.getColumnIndex(ContratoBaseDatos.TablaItemNotaLista.TEXTO)));
        item.setMarcado(UtilBoolean.intToBoolean(c.getInt(c.getColumnIndex(ContratoBaseDatos.TablaItemNotaLista.MARCADO))));
        item.setId_NotaLista(c.getLong(c.getColumnIndex(ContratoBaseDatos.TablaItemNotaLista.ID_NOTA_LISTA)));
        return item;
    }


    public static List<ItemNotaLista> CursorToListaItemNotaLista(Cursor c){
        List<ItemNotaLista>  lista = new ArrayList<ItemNotaLista>();
        while(c.moveToNext()){
            lista.add(ItemNotaLista.getItemNotaLista(c));
        }
        return lista;
    }

    public void vaciarContenido(){ //ESTO ES USADO PARA QUE SEA MAS COMODO EL METODO vaciarContenitoTodosItems() DEL ADAPTADOR DE ITEMS
        texto=null;
        marcado=false;
    }

}


