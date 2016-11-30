package com.izv.dam.newquip.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.pojo.MarcaNota;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by anton on 28/11/2016.
 */

public class AyudanteOrm extends OrmLiteSqliteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private Dao<MarcaNota, Integer> marcaNotaDao;

    public AyudanteOrm(Context context) {
        super(context, ContratoBaseDatos.BASEDATOS_OBJETOS, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, MarcaNota.class);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {}


    public Dao<MarcaNota,Integer> getMarcaNotaDao() throws SQLException {
        if (marcaNotaDao == null) {
            marcaNotaDao = getDao(MarcaNota.class);
        }
        return marcaNotaDao;
    }

    @Override
    public void close() {
        super.close();
    }
}
