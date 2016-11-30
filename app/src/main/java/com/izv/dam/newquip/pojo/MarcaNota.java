package com.izv.dam.newquip.pojo;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

/**
 * Created by anton on 28/11/2016.
 */
@DatabaseTable
public class MarcaNota {

    public static final String ID="_id";
    public static final String TEXTO ="label";
    public static final String FECHA ="fecha";
    public static final String LATITUD ="latitud";
    public static final String LONGITUD ="longitud";
    public static final String ID_NOTA="id_nota";



    @DatabaseField(generatedId = true, columnName = ID)
    private int id;
    @DatabaseField(columnName = TEXTO)
    private String texto;
    @DatabaseField(columnName = FECHA)
    private Date fecha;
    @DatabaseField(columnName = LATITUD)
    private double latitud;
    @DatabaseField(columnName = LONGITUD)
    private double longitud;
    @DatabaseField(columnName = ID_NOTA)
    private long idNota;

    public MarcaNota( String texto, Date fecha, double latitud, double longitud, long idNota) {
        this.texto = texto;
        this.fecha = fecha;
        this.latitud = latitud;
        this.longitud = longitud;
        this.idNota = idNota;
    }

    public  MarcaNota (){} // es necesario para el Dao



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public long getIdNota() {
        return idNota;
    }

    public void setIdNota(long idNota) {
        this.idNota = idNota;
    }

    @Override
    public String toString() {
        return "MarcaNota{" +
                "id=" + id +
                ", texto='" + texto + '\'' +
                ", fecha=" + fecha +
                ", latitud=" + latitud +
                ", altitud=" + longitud +
                ", idNota=" + idNota +
                '}';
    }
}
