package com.izv.dam.newquip.vistas.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;


import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.dialogo.DialogoPreferenciasNota;
import com.izv.dam.newquip.dialogo.OnBorrarDialogListener;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.dialogo.DialogoBorrar;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.PreferenciasCompartidas;
import com.izv.dam.newquip.vistas.VistaLienzo;
import com.izv.dam.newquip.vistas.notas.VistaNota;
import com.izv.dam.newquip.vistas.notas_lista.VistaNotaLista;

import java.io.File;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista , OnBorrarDialogListener,LoaderManager.LoaderCallbacks<Cursor> {

    //private AdaptadorNota adaptador;
    private PresentadorQuip presentador;
    private  RecyclerView rv;
    private FloatingActionsMenu fam;

    public static  ContratoMain.InterfaceVista REFERENCIA_MENU_PRINCIPAL; // ESTA INTERFAZ SE USARA PARA QUE EL PROVEEDOR ASINCRONO PUEDA ACTUALIZAR LOS DATOS CUANDO TERMINE SU TRABAJO
    //private PreferenciasCompartidas prefs;

    /*//MENUS ITEM

    MenuItem menuGuardar;
    MenuItem menuTitulo;
    MenuItem menuEditar;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quip);

        presentador = new PresentadorQuip(this);
        rv = (RecyclerView) findViewById(R.id.lvListaNotas);
        rv.setHasFixedSize(false); // true, la lista es estatica, false, los datos de la lista pueden variar.
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)); // forma en que se visualizan los elementos, en este caso en vertical.
        AdaptadorNota adaptador = new AdaptadorNota(getString(R.string.notaVacia),getString(R.string.listaVacia));
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

        //MENU DE FLOAT BUTTONS
        fam = (FloatingActionsMenu) findViewById(R.id.vista_principal_menuFloat);
        FloatingActionButton anadirNota = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_normal);
        anadirNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNota();
               // fam.toggle();
            }
        });

        FloatingActionButton anadirNotaLista = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_lista);
        anadirNotaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNotaLista();
            }
        });

        FloatingActionButton anadirImagen = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_imagen);
        anadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if  (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},VistaQuip.this)) { //AÑADIDA SOLICITUD DE PERMISOS, PORQUE AHORA CREA UN ARCHIVO NUEVO.
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");//para que busque cualquier tipo de imagen
                    startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), Permisos.GALERIA);
                }
                else
                    Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},VistaQuip.this);
            }
        });

        FloatingActionButton anadirDibujo = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_dibujo);
        anadirDibujo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VistaQuip.this,R.string.nuevaNotaDibujo, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(VistaQuip.this,VistaLienzo.class);
                startActivity(i);
            }
        });
        //FIN MENU DE FLOAT BUTTONS

        /*menuGuardar=(MenuItem) findViewById(R.id.menu_quip_guardar);
        menuTitulo=(MenuItem) findViewById(R.id.menu_quip_titulo);
        menuEditar=(MenuItem) findViewById(R.id.menu_quip_editar);*/

        //MENU DEL TOOL
        getSupportLoaderManager().initLoader(0,null,this); // LO 1º ES ID DEL MANAGER, , LO 2º SON LOS ARGUMENTOS Y LO 3º NOSE

    }



    @Override
    protected void onPause() {
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        //presentador.onResume();
        getSupportLoaderManager().restartLoader(0,null,this);
        super.onResume();
    }

    @Override
    public void mostrarAgregarNota() {
        Toast.makeText(VistaQuip.this, getResources().getString(R.string.nuevaNota), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        startActivity(i);
    }

    @Override
    public void mostrarAgregarNotaLista() {
        Toast.makeText(VistaQuip.this, getResources().getString(R.string.nuevaNotaLista), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNotaLista.class);
        startActivity(i);
    }

    @Override
    public void mostrarDatos(Cursor c) {
        ((AdaptadorNota)rv.getAdapter()).setCursor(c);
       // System.out.println(adaptador.getItemCount());
    }

    @Override
    public void mostrarEditarNota(Nota n) {
        String textoTostada;
        Intent i;
        if (n.getTipo()==Nota.TIPO_LISTA) { // AÑADIDO ELEGIR QUE TIPO DE NOTA VAMOS A ABRIR
            textoTostada = getResources().getString(R.string.editarNotaLista);
            i = new Intent(this, VistaNotaLista.class);
        }
        else {
            textoTostada = getResources().getString(R.string.editarNota);
            i = new Intent(this, VistaNota.class);
        }

        Toast.makeText(VistaQuip.this, textoTostada, Toast.LENGTH_SHORT).show();
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
        //getSupportLoaderManager().restartLoader(0,null,this);
    }
    @Override
    public void onBorrarNegativeButtonClick() {
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Permisos.GALERIA:
                if(resultCode == RESULT_OK) {

                    String rutaAbsoluta = DirectoriosArchivosQuip.getRutaImagen(data.getData(), this);
                    String nombreImagen = rutaAbsoluta.substring(rutaAbsoluta.lastIndexOf("/") + 1, rutaAbsoluta.length());
                    String rutaNuevaImagen = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;

                    DirectoriosArchivosQuip.crearImagen(rutaNuevaImagen, BitmapFactory.decodeFile(rutaAbsoluta), this); // le pasas una ruta,una imagen, y te crea un nuevo archivo en esa ruta con esa imagen

                    Nota nota = new Nota(Nota.TIPO_IMAGEN);
                    nota.setImagen(rutaNuevaImagen);
                    Intent i = new Intent(this,VistaNota.class);
                    Bundle b = new Bundle();
                    b.putParcelable("nota", nota);
                    i.putExtras(b);
                    Toast.makeText(VistaQuip.this,R.string.nuevaNotaImagen, Toast.LENGTH_SHORT).show();
                    startActivity(i);
                }
                break;
        }
    }


    //LOAD MANAGER
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) { // HACE LA CONSULTA CON LOS DATOS,el ID ES EL ID A USAR DEL MANAGER
        return new CursorLoader(this,ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, null, null, ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) { //ASIGNA EL CURSOR A EL ADAPTADOR
        ((AdaptadorNota)rv.getAdapter()).setCursor(data);
        presentador.cargarCursorModelo(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { // ELIMINA LA REFERENCIA DE DATOS Y DEVUELVE EL ADAPTADOR A SU ESTADO INICIAL
        ((AdaptadorNota)rv.getAdapter()).setCursor(null);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            getLoaderManager().destroyLoader(0);
            AdaptadorNota adaptador = (AdaptadorNota) rv.getAdapter();
            if (adaptador != null) {
                adaptador.setCursor(null);
                adaptador = null;
            }
        } catch (Throwable localThrowable) {
            // Proyectar la excepción
            localThrowable.printStackTrace();
        }
    }

    //FIN LOAD MANAGER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quip, menu);

       /* MenuItem menuGuardar= menu.findItem(R.id.menu_quip_guardar);
        MenuItem menuTitulo = menu.findItem(R.id.menu_quip_titulo);
        MenuItem menuEditar = menu.findItem(R.id.menu_quip_editar);

       PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);

        menuGuardar.setChecked(prefs.isPrefsGuardar());
        menuTitulo.setChecked(prefs.isPrefsTitulo());
        menuEditar.setChecked(prefs.isPrefsEditable());*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       /* int id =item.getItemId();
        item.setChecked(!item.isChecked());

        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);*/

        switch (item.getItemId()) {
            case R.id.menu_quip_configuracion : {
                DialogoPreferenciasNota dialogo = DialogoPreferenciasNota.newInstance();
                dialogo.show(getSupportFragmentManager(), "Dialogo Preferencias");
                return true;
            }
        }
        return false;
    }


    @Override
    public  void reiniciarDatos(){ //USADO PARA LAS CONSULTAS A LA BASE DE DATOS EN 2º PLANO
        getSupportLoaderManager().restartLoader(0,null,this);
    }
    @Override
    protected void onStart() {
        VistaQuip.REFERENCIA_MENU_PRINCIPAL=this;
        super.onStart();
    }
}