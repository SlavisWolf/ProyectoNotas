package com.izv.dam.newquip.contrato;

import com.izv.dam.newquip.pojo.Nota;

public interface ContratoNota {

    interface InterfaceModelo {

        void close();

        Nota getNota(long id);

        void saveNota(Nota n); //long

    }

    interface InterfacePresentador {

        void onPause();

        void onResume();

        void onSaveNota(Nota n);

    }

    interface InterfaceVista {

        void mostrarNota(Nota n);
        void marcarLocalizacionNota();
    }

}