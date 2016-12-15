package com.izv.dam.newquip.vistas;

/**
 * Created by alumno on 09/11/2016.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.dialogo.DialogoBorrarGeneral;
import com.izv.dam.newquip.dialogo.DialogoGuardarGeneral;
import com.izv.dam.newquip.dialogo.interfaces.OnBorrarGeneralDialogListener;
import com.izv.dam.newquip.dialogo.interfaces.OnGuardarGeneralDialogListener;
import com.izv.dam.newquip.pojo.Lienzo;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.UtilFecha;
import com.izv.dam.newquip.vistas.notas.VistaNota;

import java.io.File;

public class VistaLienzo extends AppCompatActivity implements View.OnClickListener,OnGuardarGeneralDialogListener,OnBorrarGeneralDialogListener {


    Nota nota;
    ImageButton negro;
    ImageButton verde;
    ImageButton azul;
    ImageButton rojo;
    ImageButton amarillo;
    Intent guardar;
    private Lienzo lienzo;
    float ppequenyo;
    float pmediano;
    float pgrande;
    float pdefecto;

    private String nombreImagen;
    private String rutaImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lienzo);


        negro = (ImageButton) findViewById(R.id.lienzo_colornegro);
        verde = (ImageButton) findViewById(R.id.lienzo_colorverde);
        azul = (ImageButton) findViewById(R.id.lienzo_colorazul);
        rojo = (ImageButton) findViewById(R.id.lienzo_colorrojo);
        amarillo = (ImageButton) findViewById(R.id.lienzo_coloramarillo);

        lienzo = (Lienzo) findViewById(R.id.lienzo);
        negro.setOnClickListener(this);
        verde.setOnClickListener(this);
        azul.setOnClickListener(this);
        rojo.setOnClickListener(this);
        amarillo.setOnClickListener(this);

        ppequenyo = 10;
        pmediano = 20;
        pgrande = 30;

        pdefecto = pmediano;

      /*  if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
            String color = savedInstanceState.getString("color");
            float grosor = savedInstanceState.getFloat("grosor");
            lienzo.setColor(color);
            lienzo.setTamanyoPunto(grosor);
        } else {*/
        Bundle b = getIntent().getExtras();
        if (b != null) {
            nota = b.getParcelable("nota");
        } else {
            nota = new Nota();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_lienzo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_lienzo_nuevo:
                DialogoBorrarGeneral fragmentBorrar = DialogoBorrarGeneral.newInstance(getString(R.string.tituloDialogoBorrarDibujo),getString(R.string.textoDialogoBorrarDibujo));
                fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
                return true;


            case R.id.menu_lienzo_grande:
                lienzo.setTamanyoPunto(pgrande);
                Toast.makeText(getApplicationContext(), R.string.seleccionGrosorGrande, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_lienzo_mediano:
                lienzo.setTamanyoPunto(pmediano);
                Toast.makeText(getApplicationContext(), R.string.seleccionGrosorMediano, Toast.LENGTH_SHORT).show();
                return true;

            case R.id.menu_lienzo_pequeno:
                lienzo.setTamanyoPunto(ppequenyo);
                Toast.makeText(getApplicationContext(), R.string.seleccionGrosorpequeno, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_lienzo_goma:
                lienzo.setColor("#FFFFFF");//BLANCO
                return true;
            case android.R.id.home: {
                finish();
                return true;
            }
            case R.id.menu_lienzo_guardar: {

                if (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this)) {
                    DialogoGuardarGeneral fragmentGuardar = DialogoGuardarGeneral.newInstance(getString(R.string.labelDibujo), getString(R.string.mensaje_confirm_guardar_lienzo));
                    fragmentGuardar.show(getSupportFragmentManager(),"Dialogo guardar");
                 }
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onClick(View v) {
        String color;

        switch (v.getId()) {
            case R.id.lienzo_colornegro:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.lienzo_colorverde:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.lienzo_colorazul:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.lienzo_colorrojo:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            case R.id.lienzo_coloramarillo:
                color = v.getTag().toString();
                lienzo.setColor(color);
                break;
            default:

                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("nota", nota);
        outState.putFloat("grosor",lienzo.getTamanyoPunto());
        outState.putString("color",lienzo.getColor());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /**
         * Si tubieramos diferentes permisos solicitando permisos de la aplicacion, aqui habria varios IF
         */
        if (requestCode == Permisos.GUARDAR_DIBUJO) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Realizamos la accion
                //  startActivity(guardar);
                Toast.makeText(this, R.string.permisoConcedido, Toast.LENGTH_SHORT).show();
            } else {
                //1-Seguimos el proceso de ejecucion sin esta accion: Esto lo recomienda Google
                //2-Cancelamos el proceso actual
                //3-Salimos de la aplicacion
                Toast.makeText(this, R.string.permisoNoConcedido, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onGuardarPossitiveButtonClick() { //Guardar el dibujo como un archivo png

        //lienzo.setDrawingCacheEnabled(true);

        final Bitmap dibujoLienzo = lienzo.getBitmap();   //lienzo.getDrawingCache();
        if (dibujoLienzo != null) { //OPTIMIZADO
            new AsyncTask<Bitmap,Void,Intent>() {
                @Override
                protected Intent doInBackground(Bitmap... params) {
                    DirectoriosArchivosQuip.comprobarDirectorioImagenes();
                    String nombreImagen = UtilFecha.fechaActual()+"dibujoQuip.png";
                    rutaImage = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;

                    DirectoriosArchivosQuip.crearImagen(rutaImage,dibujoLienzo,VistaLienzo.this);

                    nota.setTipo(Nota.TIPO_DIBUJO);
                    nota.setImagen(rutaImage);
                    Intent i = new Intent(VistaLienzo.this, VistaNota.class);
                    Bundle b = new Bundle();
                    b.putParcelable("nota", nota);
                    i.putExtras(b);
                    return i;
                }

                @Override
                protected void onPostExecute(Intent intent) {
                    if (VistaNota.NOTA_ACTUAL!=null)
                        VistaNota.NOTA_ACTUAL.finish();
                    Toast.makeText(getApplicationContext(), R.string.dibujoGuardadoCorrectamente, Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                }
            }.execute(dibujoLienzo);
        } else {
            Toast.makeText(getApplicationContext(), R.string.imagenNoGuardada, Toast.LENGTH_SHORT).show();
        }
        lienzo.destroyDrawingCache();


    }

    @Override
    public void onGuardarNegativeButtonClick() {

    }

    @Override
    public void onBorrarPossitiveButtonClick() { // Crear un nuevo dibujo en blanco.
        lienzo.NuevoDibujo();
    }

    @Override
    public void onBorrarNegativeButtonClick() {

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.right_in, R.anim.right_out);
    }
}


