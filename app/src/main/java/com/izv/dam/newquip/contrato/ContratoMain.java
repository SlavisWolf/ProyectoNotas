package com.izv.dam.newquip.contrato;

import android.database.Cursor;

import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.vistas.main.VistaQuip;

public interface ContratoMain {

    interface InterfaceModelo {

        void close();

        void deleteNota(int position); //devolvia long

        void deleteNota(Nota n); //devolvia long

        Nota getNota(int position);

        void setCursor(Cursor c);

        void recuperarNota(Nota n);

        void borrarConjunto(String where,boolean papelera);
    }

    interface InterfacePresentador {

        void recuperarNota(Nota n);

        void onAddNota();

        void onAddNotaLista();

        void onDeleteNota(int position);

        void onDeleteNota(Nota n);

        void onClickNota(int position);

        void onEditNota(Nota n);

        void onPause();

        void onResume();

        void onShowBorrarNota(int position);

        void cargarCursorModelo(Cursor c);

    }

    interface InterfaceVista {

        void borrarMarcasNota(long id_nota);

        void mostrarAgregarNota();

        void mostrarAgregarNotaLista();

        void mostrarDatos(Cursor c);

        void mostrarEditarNota(Nota n);

        void mostrarConfirmarBorrarNota(Nota n);
        void mostrarConfirmarRecuperarNota(Nota n);

        void reiniciarDatos(int id);

    }

}