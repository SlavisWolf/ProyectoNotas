package com.izv.dam.newquip.vistas.notas_lista;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorItemNotaLista;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ContratoNotaLista;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilArray;
import com.izv.dam.newquip.util.UtilFecha;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alumno on 25/10/2016.
 */




public class VistaNotaLista extends AppCompatActivity implements ContratoNotaLista.InterfaceVista{

    private EditText titulo;
    private RecyclerView items;
    private FloatingActionButton anadirItem;
    private Nota nota = new Nota(Nota.TIPO_LISTA);
    private PresentadorNotaLista presentador;
    private TextInputLayout til_titulo;
    private boolean marcarDesmarcar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota_lista);

        marcarDesmarcar=true;
        til_titulo = (TextInputLayout) findViewById(R.id.nota_lista_til_titulo);
        titulo = (EditText) findViewById(R.id.nota_lista_titulo);
        items = (RecyclerView) findViewById(R.id.nota_lista_items);
        anadirItem = (FloatingActionButton) findViewById(R.id.nota_lista_añadir_item);
        anadirItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemNotaLista item = new ItemNotaLista();
                AdaptadorItemNotaLista adapter = (AdaptadorItemNotaLista) items.getAdapter();
                adapter.añadirItem(item);
                //items.request
            }
        });
        presentador = new PresentadorNotaLista(this);

        items = (RecyclerView) findViewById(R.id.nota_lista_items);
        items.setHasFixedSize(false); // true, la lista es estatica, false, los datos de la lista pueden variar.
        items.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)); // forma en que se visualizan los elementos, en este caso en vertical.
        AdaptadorItemNotaLista adaptador = new AdaptadorItemNotaLista();
        items.setAdapter(adaptador); //TIENES QUE ASIGNAR EL ADAPTADOR AL RECYCLER ANTES DE EJECUTAR CUALQUIER METODO CON NOTIFYDATASETCHANGED



        boolean enabled = false; //ESTO DETERMINARA, SI NADA MÁS ABRIR LA NOTA ESTA SERA EDITABLE O NO
        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
            enabled = savedInstanceState.getBoolean("enabled");
            List<ItemNotaLista> lista = savedInstanceState.getParcelableArrayList("lista");
            List<ItemNotaLista> borrados = savedInstanceState.getParcelableArrayList("borrados");
            adaptador.setLista(lista);
            adaptador.setBorrados(borrados);
        } else {
            Bundle b = getIntent().getExtras();
            if(b != null ) {
                nota = b.getParcelable("nota");
            }
            else {
                enabled=true;
            }
            cargarItems(presentador.getItems(nota.getId()));
        }
        cambiarEditable(enabled);
        titulo.setText(nota.getTitulo());
        titulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                til_titulo.setError(null); //esto hace que al escribir se pierda el error de no titulo.
            }
        });
    }
    @Override
    public void cargarItems(Cursor c) {
        cargarItems(ItemNotaLista.CursorToListaItemNotaLista(c));
    }

    @Override
    public void cargarItems(List<ItemNotaLista> l) {
        ((AdaptadorItemNotaLista)items.getAdapter()).setLista(l);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);

        List<ItemNotaLista> list = ((AdaptadorItemNotaLista)items.getAdapter()).getLista();
        List<ItemNotaLista> borrados = ((AdaptadorItemNotaLista)items.getAdapter()).getBorrados();
        outState.putParcelableArrayList("lista", UtilArray.listToArrayList(list));
        outState.putParcelableArrayList("borrados", UtilArray.listToArrayList(borrados));
        outState.putBoolean("enabled",titulo.isEnabled());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nota_lista,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_nota_lista_editar : {
                cambiarEditable(!titulo.isEnabled());
                return true;
            }

            case R.id.menu_nota_lista_guardar: {

                String textoTitulo = titulo.getText().toString().trim();
                if (textoTitulo.isEmpty()) {
                    til_titulo.setError(getResources().getString(R.string.errorTituloNoEscrito));
                }
                else {
                    AdaptadorItemNotaLista adapter = (AdaptadorItemNotaLista) items.getAdapter();
                    nota.setTitulo(textoTitulo);
                    long id =presentador.onSaveNota(nota, adapter.getLista(), adapter.getBorrados());
                    if (nota.getId()==0) {
                        nota.setId(id);
                    }
                    Toast.makeText(getApplicationContext(),
                            getResources().getString(R.string.guardarNotaPart1)+" "+nota.getTitulo()+" "+getResources().getString(R.string.guardarNotaPart2),
                            Toast.LENGTH_SHORT).show();
                    //CUANDO LE DAMOS A GUARDAR, LOS ELEMENTOS SE DESACTIVAN SI O SI.
                    cambiarEditable(false);
                }
                return true;
            }

            case R.id.menu_nota_lista_borrar_items :{
                ((AdaptadorItemNotaLista)items.getAdapter()).borrarListaCompleta();
                return  true;
            }

            case R.id.menu_nota_lista_vaciar_items : {
                ((AdaptadorItemNotaLista)items.getAdapter()).vaciarContenitoTodosItems();
                return true;
            }

            case R.id.menu_nota_lista_marcar_items : {
                AdaptadorItemNotaLista adapter = (AdaptadorItemNotaLista)items.getAdapter();
                if (adapter.comprobarTodosMarcados()) {
                    adapter.marcarDesmarcarTodosCheckBox(false);
                }
                else if (adapter.comprobarTodosDesmarcados()) {
                    adapter.marcarDesmarcarTodosCheckBox(true);
                }
                else {
                    adapter.marcarDesmarcarTodosCheckBox(marcarDesmarcar);
                    marcarDesmarcar = !marcarDesmarcar;
                }
                return true;
            }

            case R.id.menu_nota_lista_invertir_items : {
                ((AdaptadorItemNotaLista)items.getAdapter()).invertirCheckBox();
                return true;
            }

            case android.R.id.home : {
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                    TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
                } else {
                    NavUtils.navigateUpTo(this, upIntent);
                }
            }
            default: {
                return false;
            }
        }
    }

    private void cambiarEditable(boolean editable) {
        AdaptadorItemNotaLista adapter = (AdaptadorItemNotaLista)items.getAdapter();
        adapter.setViewAreEnabled(editable);
        if (editable)
            anadirItem.setVisibility(View.VISIBLE);
        else
            anadirItem.setVisibility(View.GONE);
        titulo.setEnabled(editable);
    }
}
