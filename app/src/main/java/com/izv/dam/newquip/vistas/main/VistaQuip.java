package com.izv.dam.newquip.vistas.main;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.dialogo.OnBorrarDialogListener;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.dialogo.DialogoBorrar;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.vistas.VistaLienzo;
import com.izv.dam.newquip.vistas.notas.VistaNota;
import com.izv.dam.newquip.vistas.notas_lista.VistaNotaLista;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista , OnBorrarDialogListener {

    //private AdaptadorNota adaptador;
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
       AdaptadorNota adaptador = new AdaptadorNota();
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
        FloatingActionsMenu fam = (FloatingActionsMenu) findViewById(R.id.vista_principal_menuFloat);
        FloatingActionButton anadirNota = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_normal);
        anadirNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentador.onAddNota();
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

}