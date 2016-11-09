package com.izv.dam.newquip.contrato;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class ContratoBaseDatos {

    public final static String BASEDATOS = "quiip.sqlite";
    public static final String AUTHORITY ="com.izv.dam.bd";

    private ContratoBaseDatos(){
    }

    public static abstract class TablaNota implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "nota";
        public static final String TITULO = "titulo";
        public static final String NOTA = "nota";
        public static final String IMAGEN ="imagen";
        public static final String AUDIO = "audio";
        public static final String TIPO ="tipo";
        public static final String[] PROJECTION_ALL = {_ID, TITULO, NOTA, IMAGEN, AUDIO,TIPO};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

        public static final String URI_NOTA ="content://"+AUTHORITY+"/"+ContratoBaseDatos.TablaNota.TABLA; // el content se refiere a que el elemento es un content provider, el 2º // apartado es el id, se llama authority(contenedores.ejemplos)
        public static final Uri CONTENT_URI_NOTA = Uri.parse(URI_NOTA);
    }

   /* public static abstract class TablaNotaLista implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "nota_lista";
        public static final String TITULO = "titulo";
        public static final String[] PROJECTION_ALL = {_ID, TITULO};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

        public static final String URI_NOTA_LISTA ="content://"+AUTHORITY+"/"+ContratoBaseDatos.TablaNotaLista.TABLA; // el content se refiere a que el elemento es un content provider, el 2º // apartado es el id, se llama authority(contenedores.ejemplos)
        public static final Uri CONTENT_URI_NOTA_LISTA = Uri.parse(URI_NOTA_LISTA);
    }*/

    public static abstract class TablaItemNotaLista implements BaseColumns {
        //BaseColumns incluye de forma predeterminada el campo _id
        public static final String TABLA = "item_nota_lista";
        public static final String TEXTO = "texto";
        public static final String MARCADO ="marcado";
        public static final String ID_NOTA_LISTA = "id_nota_lista";
        public static final String[] PROJECTION_ALL = {_ID, TEXTO,MARCADO,ID_NOTA_LISTA};
        public static final String SORT_ORDER_DEFAULT = _ID + " desc";

        public static final String URI_ITEM_NOTA_LISTA ="content://"+AUTHORITY+"/"+ContratoBaseDatos.TablaItemNotaLista.TABLA; // el content se refiere a que el elemento es un content provider, el 2º // apartado es el id, se llama authority(contenedores.ejemplos)
        public static final Uri CONTENT_URI_ITEM_NOTA_LISTA = Uri.parse(URI_ITEM_NOTA_LISTA);
    }
}