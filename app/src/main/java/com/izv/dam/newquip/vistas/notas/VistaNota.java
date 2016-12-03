package com.izv.dam.newquip.vistas.notas;

import android.Manifest;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.basedatos.AyudanteOrm;
import com.izv.dam.newquip.contrato.ContratoNota;
import com.izv.dam.newquip.databinding.ActivityNotaBinding;
import com.izv.dam.newquip.dialogo.DialogoBorrarGeneral;
import com.izv.dam.newquip.dialogo.interfaces.OnBorrarGeneralDialogListener;
import com.izv.dam.newquip.pojo.MarcaNota;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.ImprimirPDF;
import com.izv.dam.newquip.util.ObtenedorDeLocalizacionActual;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.PreferenciasCompartidas;
import com.izv.dam.newquip.util.UtilFecha;
import com.izv.dam.newquip.vistas.VistaLienzo;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;


import java.io.File;
import java.sql.SQLException;
import java.util.Date;


public class VistaNota extends AppCompatActivity implements ContratoNota.InterfaceVista, OnBorrarGeneralDialogListener {


    private PresentadorNota presentador;
    private ActivityNotaBinding binding;

    private ObtenedorDeLocalizacionActual odla;
    boolean notaGuardada; // esta variable se usara para que no se hagan seguidos el metodo de guardar del boton, y el de guardar automaticamente en el onPause
    private String rutaImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_nota);
        //setContentView(R.layout.activity_nota);

        notaGuardada = false;
        Nota nota = new Nota();
        presentador = new PresentadorNota(this);

        binding.imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogoBorrarGeneral fragmentBorrar = DialogoBorrarGeneral.newInstance(getString(R.string.tituloDialogoImagen), getString(R.string.textoDialogoImagen));
                fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
                return true;
            }
        });


        boolean editable;

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
            editable = savedInstanceState.getBoolean("enabled");

        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
                if (nota.getId() == 0) //PARA LAS NOTAS CREADAS DESDE LOS BOTONES CON UNA IMAGEN
                    editable = true;
                else {
                    PreferenciasCompartidas prefs = new PreferenciasCompartidas(this); //solo para notas que no son nuevas;
                    editable = prefs.isPrefsEditable();
                }
            } else {
                editable = true;
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
        }
        binding.setEditable(editable);

        binding.etTitulo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                binding.tilTituloNota.setError(null); //esto hace que al escribir se pierda el error de no titulo.
            }
        });

        binding.setNota(nota);
        mostrarNota(nota);
        if (Permisos.solicitarPermisos(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, this)) {
            odla = new ObtenedorDeLocalizacionActual(this);
        }
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_nota, menu);
        return true;
    }

    @Override


    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_nota_editar) {
            //cambiarEditable(!binding.etTitulo.isEnabled());
            binding.setEditable(!binding.getEditable());
        } else if (id == R.id.galeria) {

            if (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this)) { //AÑADIDA SOLICITUD DE PERMISOS, PORQUE AHORA CREA UN ARCHIVO NUEVO.
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");//para que busque cualquier tipo de imagen
                startActivityForResult(intent.createChooser(intent, getString(R.string.appImagen)), Permisos.GALERIA);
            } else
                Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this);
        } else if (id == R.id.menu_nota_guardar) {
            String textoTitulo = binding.etTitulo.getText().toString().trim();
            PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);
            if (textoTitulo.isEmpty() && prefs.isPrefsTitulo()) {
                binding.tilTituloNota.setError(getResources().getString(R.string.errorTituloNoEscrito));
            } else {
                saveNota();
                notaGuardada = true; // despues se saldra, con esto evitamos que duplique
                finish();
                overridePendingTransition(R.anim.zoom_fordward_in, R.anim.zoom_fordward_out);
            }

        } else if (id == R.id.camara) {

            Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, this);
            if (Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, this)) {


                new AsyncTask<Void, Void, Intent>() {  // añadido el proceso en 2º plano.
                    @Override
                    protected Intent doInBackground(Void... params) {
                        Long timestamp = System.currentTimeMillis() / 1000;
                        DirectoriosArchivosQuip.comprobarDirectorioImagenes();
                        String nombreImagen = timestamp.toString() + ".jpg";
                        rutaImage = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;
                        File newFile = new File(rutaImage);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(newFile));
                        return intent;
                    }

                    @Override
                    protected void onPostExecute(Intent intent) {
                        startActivityForResult(intent, Permisos.HACER_FOTO);
                    }
                }.execute();
            }

        } else if (id == R.id.menu_nota_imprimir) {

            if (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this)) {

                guardarDatosEnNota();
                if (binding.getNota().comprobarNotaVaciaParaPDF()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.notaVaciaPdf), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.imprimirNota), Toast.LENGTH_SHORT).show();
                    ImprimirPDF.imprimirNota(this, binding.getNota());
                }
            }
        } else if (id == R.id.menu_nota_dibujar) {
            Intent i = new Intent(this, VistaLienzo.class);
            Bundle b = new Bundle();
            guardarDatosEnNota();
            b.putParcelable("nota", binding.getNota());
            i.putExtras(b);
            Toast.makeText(this, getResources().getString(R.string.abrirLienzo), Toast.LENGTH_SHORT).show();
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.left_out);
            finish();
        } else if (id == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.zoom_fordward_in, R.anim.zoom_fordward_out);

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Bitmap bmp = null;
        switch (requestCode) {
            case Permisos.GALERIA:
                if (resultCode == RESULT_OK) {
                    new AsyncTask<Object, Void, Void>() {  //copia de archivos en 2º plano.
                        @Override
                        protected Void doInBackground(Object... params) {

                            Intent data = (Intent) params[0];
                            String rutaAbsoluta = DirectoriosArchivosQuip.getRutaImagen(data.getData(), VistaNota.this);
                            String nombreImagen = UtilFecha.fechaActual() + rutaAbsoluta.substring(rutaAbsoluta.lastIndexOf("/") + 1, rutaAbsoluta.length());
                            rutaImage = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;

                            DirectoriosArchivosQuip.crearImagen(rutaImage, BitmapFactory.decodeFile(rutaAbsoluta), VistaNota.this); // le pasas una ruta,una imagen, y te crea un nuevo archivo en esa ruta con esa imagen
                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) { // NO PODEMOS ACCEDER A ELEMENTOS DE LA INTERFAZ DE USUARIO DESDE doInBackground
                            binding.getNota().setImagen(rutaImage);
                            binding.getNota().setTipo(Nota.TIPO_IMAGEN);
                        }
                    }.execute(data);
                }
                break;
            case Permisos.HACER_FOTO: {
                if (resultCode == RESULT_OK) {


                    MediaScannerConnection.scanFile(this, new String[]{rutaImage}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    binding.getNota().setImagen(rutaImage);
                    //MetodosBinding.setImagenConRuta(imagen,rutaImage); AHORA USA BINDABLE
                    binding.getNota().setTipo(Nota.TIPO_IMAGEN);
                }
                break;
            }
        }
    }

    @Override
    protected void onPause() {
        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);
        if (prefs.isPrefsGuardar() && !notaGuardada) {
            String textoTitulo = binding.getNota().getTitulo();
            if (textoTitulo.isEmpty()) { //comprobar que el titulo esta vacio
                if (!prefs.isPrefsTitulo()) { // si el titulo esta vacio, hay que comprobar que el usuario admite titulos vacios.
                    saveNota();
                }
            } else {
                saveNota();
            }
        }
        notaGuardada = false;
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
        outState.putParcelable("nota", binding.getNota());
        outState.putBoolean("enabled", binding.getEditable());
        //outState.putStringArrayList("fotos_borradas",fotosBorradas);
    }

    @Override
    public void mostrarNota(Nota n) {
    }

    private void saveNota() {

        guardarDatosEnNota();//solo se hace trim a los textos
        Nota n = binding.getNota();
        if (!n.comprobarNotaVacia()) {
            presentador.onSaveNota(n);
            //Localización Nota!
            if (Permisos.solicitarPermisos(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, this)) {
                marcarLocalizacionNota();
            }
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.guardarNotaPart1) + " " + n.getTitulo() + " " + getResources().getString(R.string.guardarNotaPart2),
                    Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBorrarPossitiveButtonClick() {
        binding.getNota().setTipo(Nota.TIPO_DEFECTO);
        rutaImage = null;
        binding.getNota().setImagen(rutaImage);
        //MetodosBinding.setImagenConRuta(imagen,null);
    }

    @Override
    public void onBorrarNegativeButtonClick() {
    }

    private void guardarDatosEnNota() { // AHORA MISMO SOLO SE USA PARA QUITAR LOS ESPACIOS
        //QUITAR LOS ESPACIOS.
        binding.getNota().setTitulo(binding.getNota().getTitulo().trim());
        binding.getNota().setNota(binding.getNota().getNota().trim());
    }


    //http://androcode.es/2012/12/primeros-pasos-con-ormlite/
    private void marcarLocalizacionNota() {

        System.out.println("Si entra en el metodo");
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //AÑADIDO MARCAS
                try {
                    AyudanteOrm helper = OpenHelperManager.getHelper(VistaNota.this, AyudanteOrm.class);
                    Dao dao = helper.getMarcaNotaDao();
                    //OBTENCION DATOS MARCA
                    String texto;
                    Nota n = binding.getNota();
                    if (n.getTitulo() == null || n.getTitulo().trim().isEmpty())
                        texto = n.getTitulo().trim();
                    else if (n.getNota() == null || n.getNota().trim().isEmpty())
                        texto = n.getNota().trim();
                    else
                        texto = getString(R.string.marcaSinTexto);
                    Location l = odla.getLocalizacion();
                    System.out.println("Llega hasta Localización");
                    //------------------------------------------------------------------------
                        MarcaNota marca = new MarcaNota(texto, new Date(), l.getLatitude(), l.getLongitude(), n.getId());
                        Log.v("MARCA",marca.toString());
                        dao.create(marca);


                } catch (SQLException e) {
                    e.printStackTrace();
                }
                catch (NullPointerException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();
    }

}

