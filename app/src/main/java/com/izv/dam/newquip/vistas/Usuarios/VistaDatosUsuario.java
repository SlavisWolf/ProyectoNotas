package com.izv.dam.newquip.vistas.Usuarios;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TextInputEditText;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.databinding.ActivityVerDatosUsuarioBinding;
import com.izv.dam.newquip.dialogo.DialogoCambiarAvatar;
import com.izv.dam.newquip.dialogo.interfaces.OnCambiarAvatar;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.PreferenciasCompartidas;
import com.izv.dam.newquip.vistas.main.VistaQuip;

import java.io.File;

/**
 * Created by anton on 22/11/2016.
 */

public class VistaDatosUsuario extends AppCompatActivity implements OnCambiarAvatar{

    private  Spinner estadoCivil;
    private TextInputEditText nombre;
    private TextInputLayout til_nombre;
    private TextInputEditText apellidos;
    private TextInputEditText ciudad;
    private TextInputEditText cp;
    private TextInputEditText nacimiento;
    private TextInputEditText tlf;
    private TextInputEditText correo;
    private ImageView avatar;
    private String rutaImagen;
    private ActivityVerDatosUsuarioBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ver_datos_usuario);

        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);


        iniciarComponentes();

        boolean editable = false;
        if (savedInstanceState!=null) {
            nombre.setText(savedInstanceState.getString("nombre"));
            apellidos.setText(savedInstanceState.getString("apellidos"));
            ciudad.setText(savedInstanceState.getString("ciudad"));
            cp.setText(savedInstanceState.getString("cp"));
            nacimiento.setText(savedInstanceState.getString("nacimiento"));
            tlf.setText(savedInstanceState.getString("tlf"));
            correo.setText(savedInstanceState.getString("correo"));
            estadoCivil.setSelection(savedInstanceState.getInt("estado_civil"));
            editable = savedInstanceState.getBoolean("editable");
            rutaImagen = savedInstanceState.getString("avatar");
            if (rutaImagen!=null && !rutaImagen.isEmpty()){
                Bitmap btm = BitmapFactory.decodeFile(rutaImagen);
                avatar.setImageBitmap(btm);
            }
        }else {
            nombre.setText(prefs.getPrefsNombreUsuario());
            apellidos.setText(prefs.getPrefsApellidosUsuario());
            ciudad.setText(prefs.getPrefsCiudadUsuario());
            cp.setText(prefs.getPrefsCPUsuario());
            nacimiento.setText(prefs.getPrefsNacimientoUsuario());
            tlf.setText(prefs.getPrefsTelefonoUsuario());
            correo.setText(prefs.getPrefsCorreoUsuario());
            estadoCivil.setSelection(prefs.getPrefsEstadoCivilUsuario());
            rutaImagen = prefs.getPrefsAvatarUsuario();
            if (rutaImagen!=null && !rutaImagen.isEmpty()){
                Bitmap btm = BitmapFactory.decodeFile(rutaImagen);
                avatar.setImageBitmap(btm);
            }
        }
        binding.setEditable(editable);
    }

    private void  iniciarComponentes(){

        estadoCivil = binding.datosUsuarioEstadoCivil;
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.estadoCivilArray,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        estadoCivil.setAdapter(staticAdapter);

        nombre = binding.datosUsuarioNombre;
        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                til_nombre.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        til_nombre = binding.datosUsuarioTilNombre;
        binding.botonActualizarDatos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });
        //INICIAR
        apellidos = binding.datosUsuarioApellidos;
        ciudad =  binding.datosUsuarioCiudad;
        cp= binding.datosUsuarioCp;
        nacimiento = binding.datosUsuarioFechaNacimiento;
        tlf = binding.datosUsuarioTelefono;
        correo = binding.datosUsuarioCorreo;
        avatar = binding.datosUsuarioFotoPerfil;

        avatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DialogoCambiarAvatar fragmentBorrar = DialogoCambiarAvatar.newInstance();
                fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
                return true;
            }
        });
    }


    private void actualizarDatos(){
        String textoNombre = nombre.getText().toString().trim();
        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);
        if (textoNombre.isEmpty()) {
            til_nombre.setError(getString(R.string.errorNoNombre));
            nombre.requestFocus();
        }
        else {
            //GUARDAMOS LOS CAMPOS EN LAS PREFERENCIAS
            prefs.setPrefsNombreUsuario(textoNombre);
            prefs.setPrefsApellidosUsuario(apellidos.getText().toString().trim());
            prefs.setPrefsCiudadUsuario(ciudad.getText().toString().trim());
            prefs.setPrefsCPUsuario(cp.getText().toString().trim());
            prefs.setPrefsNacimientoUsuario(nacimiento.getText().toString().trim());
            prefs.setPrefsTelefonoUsuario(tlf.getText().toString().trim());
            System.out.println(correo.getText().toString().trim());
            prefs.setPrefsCorreoUsuario(correo.getText().toString().trim());
            prefs.setPrefsEstadoCivilUsuario(estadoCivil.getSelectedItemPosition());
            prefs.setPrefsAvatarUsuario(rutaImagen);
            Toast.makeText(this,R.string.datosUsuarioActualizados, Toast.LENGTH_SHORT).show();
            finish();
            overridePendingTransition(R.anim.right_in, R.anim.right_out);
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("nombre",nombre.getText().toString());
        outState.putString("apellidos",apellidos.getText().toString());
        outState.putString("ciudad",ciudad.getText().toString());
        outState.putString("cp",cp.getText().toString());
        outState.putString("nacimiento",nacimiento.getText().toString());
        outState.putString("tlf",tlf.getText().toString());
        outState.putString("correo",correo.getText().toString());
        outState.putInt("estado_civil",estadoCivil.getSelectedItemPosition());
        outState.putString("avatar",rutaImagen);
        outState.putBoolean("editable",binding.getEditable());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vista_usuario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id==R.id.menu_usuario_editar) {
            binding.setEditable(!binding.getEditable());
            return true;
        }
        else if(id == android.R.id.home){
                finish();
                overridePendingTransition(R.anim.right_in, R.anim.right_out);

        }
        return false;
    }

    @Override
    public void nuevoAvatar() {
        if  (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},this)) {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");//para que busque cualquier tipo de imagen
            startActivityForResult(intent.createChooser(intent,getString(R.string.appImagen) ), Permisos.GALERIA);
        }
    }

    @Override
    public void borrarAvatar() {
            new PreferenciasCompartidas(this).setPrefsAvatarUsuario(null);
            rutaImagen=null;
            avatar.setImageResource(android.R.drawable.ic_menu_camera);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Permisos.GALERIA:
                if(resultCode == RESULT_OK) {
                    new AsyncTask<Object,Void, String>() { //añadido en 2º plano.
                        @Override
                        protected String doInBackground(Object... params) {

                            Intent data = (Intent) params[0];
                            Long timestamp = System.currentTimeMillis() / 1000;
                            String rutaAbsoluta = DirectoriosArchivosQuip.getRutaImagen(data.getData(), VistaDatosUsuario.this);
                            String nombreImagen =  timestamp +rutaAbsoluta.substring(rutaAbsoluta.lastIndexOf("/") + 1, rutaAbsoluta.length());
                            return DirectoriosArchivosQuip.crearImagenAlmacenamientoInterno(VistaDatosUsuario.this, BitmapFactory.decodeFile(rutaAbsoluta), nombreImagen); // le pasas un nombre y un bitmap, y guarda dentro de files el archivo,devuelve la ruta.
                        }
                        @Override
                        protected void onPostExecute(String s) {
                            rutaImagen= s;
                            avatar.setImageBitmap(BitmapFactory.decodeFile(s));
                        }
                    }.execute(data);
                }
                break;
        }
    }
}
