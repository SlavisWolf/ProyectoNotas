package com.izv.dam.newquip.vistas;

/**
 * Created by alumno on 09/11/2016.
 */

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.izv.dam.newquip.R;

import com.izv.dam.newquip.pojo.Lienzo;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.vistas.notas.VistaNota;

import java.io.File;
import java.util.UUID;

public class VistaLienzo extends AppCompatActivity implements View.OnClickListener {


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

        if (savedInstanceState != null) {
            nota = savedInstanceState.getParcelable("nota");
            String color = savedInstanceState.getString("color");
            float grosor = savedInstanceState.getFloat("grosor");
            lienzo.setColor(color);
            lienzo.setTamanyoPunto(grosor);
        } else {
            Bundle b = getIntent().getExtras();
            if (b != null) {
                nota = b.getParcelable("nota");
            }
            else {
                nota= new Nota();
            }
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
                AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
                newDialog.setTitle(R.string.tituloNuevoDibujo);
                newDialog.setMessage(R.string.textoDialogoNuevoDibujo);
                newDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        lienzo.NuevoDibujo();
                        dialog.dismiss();
                    }
                });
                newDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                newDialog.show();
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
            }


                return true;

            case R.id.menu_lienzo_guardar:


                if (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, this)) {
                    AlertDialog.Builder salvarDibujo = new AlertDialog.Builder(this);
                    salvarDibujo.setTitle(R.string.tituloGuardarDibujo);
                    salvarDibujo.setMessage(R.string.textoDialogoGuardarDibujo);
                    salvarDibujo.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {


                            lienzo.setDrawingCacheEnabled(true);

                            String imgSaved = MediaStore.Images.Media.insertImage(
                                    getContentResolver(), lienzo.getDrawingCache(),
                                    UUID.randomUUID().toString() + ".png", "drawing");

                            if (imgSaved != null) {

                                DirectoriosArchivosQuip.comprobarDirectorioImagenes();
                                Long timestamp = System.currentTimeMillis() / 1000;
                                String rutaAbsolutaImagen = DirectoriosArchivosQuip.getRutaImagen(Uri.parse(imgSaved),VistaLienzo.this);
                                Log.v("VARIABLES RARAS",rutaAbsolutaImagen);
                                String nombreImagen = timestamp+rutaAbsolutaImagen.substring(rutaAbsolutaImagen.lastIndexOf("/") + 1, rutaAbsolutaImagen.length());
                                rutaImage = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;

                                DirectoriosArchivosQuip.crearImagen(rutaImage, BitmapFactory.decodeFile(rutaAbsolutaImagen),VistaLienzo.this);
                                DirectoriosArchivosQuip.borrarArchivo(rutaAbsolutaImagen);
                                Toast.makeText(getApplicationContext(), R.string.dibujoGuardadoCorrectamente, Toast.LENGTH_SHORT).show();

                                nota.setTipo(Nota.TIPO_DIBUJO);
                                nota.setImagen(rutaImage);
                                Intent i = new Intent(VistaLienzo.this, VistaNota.class);
                                Bundle b = new Bundle();
                                b.putParcelable("nota", nota);
                                i.putExtras(b);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.imagenNoGuardada, Toast.LENGTH_SHORT).show();
                            }
                            lienzo.destroyDrawingCache();


                        }

                    });
                    salvarDibujo.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    salvarDibujo.show();
                }

                return true;

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


}


