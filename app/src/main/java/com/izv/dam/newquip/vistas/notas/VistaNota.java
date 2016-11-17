package com.izv.dam.newquip.vistas.notas;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.dialogo.DialogoBorrarGeneral;
import com.izv.dam.newquip.dialogo.OnBorrarGeneralDialogListener;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.PreferenciasCompartidas;
import com.izv.dam.newquip.vistas.VistaLienzo;
import com.izv.dam.newquip.vistas.main.VistaQuip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista, OnBorrarGeneralDialogListener {

    private EditText editTextTitulo, editTextNota;
    ImageView imagen;
    private TextInputLayout til_titulo;
    private Nota nota = new Nota();
    private PresentadorNota presentador;

   // private String nombreImagen;
   // private ArrayList<String> fotosBorradas;

    boolean notaGuardada; // esta variable se usara para que no se hagan seguidos el metodo de guardar del boton, y el de guardar automaticamente en el onPause
    private String rutaImage;

    private VistaQuip menuPrincipal;

    public static boolean permisos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nota);


        notaGuardada = false;

        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);
        presentador = new PresentadorNota(this);
        imagen = (ImageView) findViewById(R.id.imageView);
        imagen.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogoBorrarGeneral fragmentBorrar = DialogoBorrarGeneral.newInstance(getString(R.string.tituloDialogoImagen),getString(R.string.textoDialogoImagen));
                fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
                return true;
            }
        });
        til_titulo = (TextInputLayout) findViewById(R.id.til_tituloNota);
        editTextTitulo = (EditText) findViewById(R.id.etTitulo);
        editTextNota = (EditText) findViewById(R.id.etNota);

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
            cambiarEditable(savedInstanceState.getBoolean("enabled"));
            //fotosBorradas= savedInstanceState.getStringArrayList("fotos_borradas");
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
                if (nota.getId()==0)  //PARA LAS NOTAS CREADAS DESDE LOS BOTONES CON UNA IMAGEN
                    cambiarEditable(true);
                else {
                    prefs = new PreferenciasCompartidas(this); //solo para notas que no son nuevas;
                    cambiarEditable(prefs.isPrefsEditable());
                }

            }
            else {
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            //fotosBorradas = new ArrayList<>();
        }

        editTextTitulo.addTextChangedListener(new TextWatcher() {
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

        mostrarNota(nota);
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nota, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if( id == R.id.recordatorio) {
           /* Toast.makeText(getApplicationContext(), "recordatorio", Toast.LENGTH_SHORT).show();
            CalendarView calendar = (CalendarView) findViewById(R.id.calendario1);
            calendar.setVisibility(calendar.VISIBLE);

            LinearLayout calendario = (LinearLayout) findViewById(R.id.calendario);
            calendario.setVisibility(calendario.VISIBLE);
            */
        }
        else if(id == R.id.menu_nota_editar) {
            cambiarEditable(!editTextTitulo.isEnabled());
        }
        else if(id == R.id.galeria) {

            if  (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},this)) { //AÑADIDA SOLICITUD DE PERMISOS, PORQUE AHORA CREA UN ARCHIVO NUEVO.
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");//para que busque cualquier tipo de imagen
                startActivityForResult(intent.createChooser(intent, "Selecciona app de imagen"), Permisos.GALERIA);
            }
            else
                Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},this);
        }
        else if(id == R.id.menu_nota_guardar){
            String textoTitulo = editTextTitulo.getText().toString().trim();
            PreferenciasCompartidas prefs  = new PreferenciasCompartidas(this);
            if (textoTitulo.isEmpty() && prefs.isPrefsTitulo()) {
                    til_titulo.setError(getResources().getString(R.string.errorTituloNoEscrito));
            }
            else {
                saveNota();
                notaGuardada=true; // despues se saldra, con esto evitamos que duplique
                finish();
                //cambiarEnabled(false);
               /* Intent intent = new Intent(VistaNota.this, VistaQuip.class);
                startActivity(intent);*/
            }

        }
        else if(id == R.id.camara){

            if (Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},this)) {

                /*if (imagen.getVisibility()==View.VISIBLE) {
                    fotosBorradas.add(rutaImage); //ESTO BORRA LA IMAGEN QUE HAY AHORA MISMO(SI LA HAY)
                }*/

                Long timestamp = System.currentTimeMillis() / 1000;
                DirectoriosArchivosQuip.comprobarDirectorioImagenes();
                String nombreImagen = timestamp.toString() + ".jpg";
                rutaImage = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;
                File newFile = new File(rutaImage);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                startActivityForResult(intent, Permisos.HACER_FOTO);

            }
            else{
                Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},this);
            }
        }
        else if(id == R.id.menu_nota_imprimir){
          /*Toast.makeText(getApplicationContext(), "Se esta imprimiendo la nota...", Toast.LENGTH_SHORT).show();
            ImprimirPDF.imprimir(this);*/
        }

        else if(id == R.id.menu_nota_dibujar){
            Intent i = new Intent(this,VistaLienzo.class);
            Bundle b = new Bundle();
            guardarDatosEnNota();
            b.putParcelable("nota", nota);
            i.putExtras(b);
            Toast.makeText(this, getResources().getString(R.string.abrirLienzo), Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
        else if(id == android.R.id.home){
            Intent upIntent = NavUtils.getParentActivityIntent(this);
            if (NavUtils.shouldUpRecreateTask(this, upIntent)) {
                TaskStackBuilder.create(this).addNextIntentWithParentStack(upIntent).startActivities();
            } else {
                NavUtils.navigateUpTo(this, upIntent);
            }
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bmp = null;
        switch (requestCode){
            case Permisos.GALERIA:
                if(resultCode == RESULT_OK) {

                        /*if (imagen.getVisibility()==View.VISIBLE) {
                            fotosBorradas.add(rutaImage); //ESTO BORRA LA IMAGEN QUE HAY AHORA MISMO(SI LA HAY)
                        }*/
                        String rutaAbsoluta = DirectoriosArchivosQuip.getRutaImagen(data.getData(),this);
                        String nombreImagen = rutaAbsoluta.substring(rutaAbsoluta.lastIndexOf("/") + 1, rutaAbsoluta.length());
                        rutaImage = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;

                        DirectoriosArchivosQuip.crearImagen(rutaImage,BitmapFactory.decodeFile(rutaAbsoluta),this); // le pasas una ruta,una imagen, y te crea un nuevo archivo en esa ruta con esa imagen

                        imagen.setVisibility(View.VISIBLE);
                        imagen.setImageBitmap(BitmapFactory.decodeFile(rutaImage));
                        nota.setTipo(Nota.TIPO_IMAGEN);

                }
                break;
            case Permisos.HACER_FOTO:
                if(resultCode == RESULT_OK) {


                    imagen.setVisibility(View.VISIBLE);

                    MediaScannerConnection.scanFile(this, new String[]{rutaImage}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    bmp = BitmapFactory.decodeFile(rutaImage);
                    imagen .setImageBitmap(bmp);
                    nota.setTipo(Nota.TIPO_IMAGEN);
                }
               /* else {
                    fotosBorradas.remove(fotosBorradas.size()-1); //esto hace que sino  seleccionamos nada, quite de fotos borradas la ultima imagen.
                }*/
                break;
        }
    }

    @Override
    protected void onPause() {
        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);
        if (prefs.isPrefsGuardar() && !notaGuardada) {
            String textoTitulo = editTextTitulo.getText().toString().trim();
            if (textoTitulo.isEmpty()) { //comprobar que el titulo esta vacio
                if (!prefs.isPrefsTitulo()) { // si el titulo esta vacio, hay que comprobar que el usuario admite titulos vacios.
                    saveNota();
                }
            }
            else {
                saveNota();
            }
        }
        notaGuardada=false;
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        presentador.onResume();
        super.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        guardarDatosEnNota();
        outState.putParcelable("nota", nota);
        outState.putBoolean("enabled",editTextTitulo.isEnabled());
        //outState.putStringArrayList("fotos_borradas",fotosBorradas);
    }

    @Override
    public void mostrarNota(Nota n) {
        editTextTitulo.setText(nota.getTitulo());
        editTextNota.setText(nota.getNota());
        rutaImage=nota.getImagen();
        if (rutaImage!=null) {
            Bitmap nuevaImagen = BitmapFactory.decodeFile(rutaImage);
            imagen.setImageBitmap(nuevaImagen);
            imagen.setVisibility(View.VISIBLE);
        }
    }

    private void saveNota() {
        guardarDatosEnNota();
        /*for (String ruta:fotosBorradas) {
            Log.v("FICHEROS",ruta);
            DirectoriosArchivosQuip.borrarArchivo(ruta);
        }*/
        //fotosBorradas = new ArrayList<>(); //SE REINICIA
        if (!nota.comprobarNotaVacia()) {
             presentador.onSaveNota(nota);
            /*if (r > 0 & nota.getId() == 0) {
                nota.setId(r);
            }*/
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.guardarNotaPart1) +" "+ editTextTitulo.getText().toString() + " "+getResources().getString(R.string.guardarNotaPart2),
                    Toast.LENGTH_SHORT).show();
        }

    }

    private void cambiarEditable(boolean enabled) {
        editTextTitulo.setEnabled(enabled);
        editTextNota.setEnabled(enabled);
        imagen.setLongClickable(enabled);
    }

    @Override
    public void onBorrarPossitiveButtonClick() {
        imagen.setImageResource(0);
        nota.setTipo(Nota.TIPO_DEFECTO);
        //fotosBorradas.add(rutaImage);
        rutaImage = null;
        imagen.setVisibility(View.GONE);
    }

    @Override
    public void onBorrarNegativeButtonClick() {}

    private void guardarDatosEnNota() {
        nota.setTitulo(editTextTitulo.getText().toString().trim());
        nota.setNota(editTextNota.getText().toString().trim());
        nota.setImagen(rutaImage);
    }



}