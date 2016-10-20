package com.izv.dam.newquip.vistas.main;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.dialogo.OnBorrarDialogListener;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.dialogo.DialogoBorrar;
import com.izv.dam.newquip.vistas.notas.VistaNota;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista , OnBorrarDialogListener {

    private AdaptadorNota adaptador;
    private PresentadorQuip presentador;
    private  RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quip);

        presentador = new PresentadorQuip(this);

        rv = (RecyclerView) findViewById(R.id.lvListaNotas);
        rv.setHasFixedSize(false); // true, la lista es estatica, false, los datos de la lista pueden variar.
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)); // forma en que se visualizan los elementos, en este caso en vertical.
        adaptador = new AdaptadorNota();
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.onEditNota(rv.getChildAdapterPosition(v));
            }
        });
        adaptador.setLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(VistaQuip.this, "delete", Toast.LENGTH_SHORT).show();
                presentador.onShowBorrarNota(rv.getChildAdapterPosition(v));
                return true;
            }
        });
        rv.setAdapter(adaptador);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNota();
            }
        });
    }

    @Override
    protected void onPause() {
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    public void mostrarAgregarNota() {
        Toast.makeText(VistaQuip.this, "add", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        startActivity(i);
    }

    @Override
    public void mostrarDatos(Cursor c) {
        adaptador.setCursor(c);
       // System.out.println(adaptador.getItemCount());
    }

    @Override
    public void mostrarEditarNota(Nota n) {
        Toast.makeText(VistaQuip.this, "edit", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        Bundle b = new Bundle();
        b.putParcelable("nota", n);
        i.putExtras(b);
        startActivity(i);
    }

    @Override
    public void mostrarConfirmarBorrarNota(Nota n) {
        DialogoBorrar fragmentBorrar = DialogoBorrar.newInstance(n);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
    }


    @Override
    public void onBorrarPossitiveButtonClick(Nota n) {
        presentador.onDeleteNota(n);
    }

    @Override
    public void onBorrarNegativeButtonClick() {

    }
}