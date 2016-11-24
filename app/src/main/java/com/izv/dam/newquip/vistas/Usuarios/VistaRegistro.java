package com.izv.dam.newquip.vistas.Usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.util.PreferenciasCompartidas;
import com.izv.dam.newquip.vistas.main.VistaQuip;

/**
 * Created by anton on 22/11/2016.
 */

public class VistaRegistro extends AppCompatActivity {

    private  Spinner estadoCivil;
    private TextInputEditText nombre;
    private TextInputLayout til_nombre;
    private TextInputEditText apellidos;
    private TextInputEditText ciudad;
    private TextInputEditText cp;
    private TextInputEditText nacimiento;
    private TextInputEditText tlf;
    private TextInputEditText correo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);

        if (prefs.getPrefsNombreUsuario()!=null) {
            goToQuip();
        }

        iniciarComponentes();

        if (savedInstanceState!=null) {
            nombre.setText(savedInstanceState.getString("nombre"));
            apellidos.setText(savedInstanceState.getString("apellidos"));
            ciudad.setText(savedInstanceState.getString("ciudad"));
            cp.setText(savedInstanceState.getString("cp"));
            nacimiento.setText(savedInstanceState.getString("nacimiento"));
            tlf.setText(savedInstanceState.getString("tlf"));
            correo.setText(savedInstanceState.getString("correo"));
            estadoCivil.setSelection(savedInstanceState.getInt("estado_civil"));
        }
    }

    private void  iniciarComponentes(){

        estadoCivil = (Spinner) findViewById(R.id.registro_estado_civil);
        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter.createFromResource(this, R.array.estadoCivilArray,android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        estadoCivil.setAdapter(staticAdapter);

        nombre = (TextInputEditText) findViewById(R.id.registro_nombre);
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
        til_nombre = (TextInputLayout) findViewById(R.id.registro_til_nombre);


        findViewById(R.id.boton_registro).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarRegistro();
            }
        });


        //INICIAR
        apellidos = (TextInputEditText) findViewById(R.id.registro_apellidos);
        ciudad = (TextInputEditText) findViewById(R.id.registro_ciudad);
        cp= (TextInputEditText) findViewById(R.id.registro_cp);
        nacimiento = (TextInputEditText) findViewById(R.id.registro_fecha_nacimiento);
        tlf = (TextInputEditText) findViewById(R.id.registro_telefono);
        correo = (TextInputEditText) findViewById(R.id.registro_correo);
    }


    private void enviarRegistro(){
        String textoNombre = nombre.getText().toString().trim();
        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);
        if (textoNombre.isEmpty()) {
            til_nombre.setError(getString(R.string.errorNoNombre));
        }
        else {
            //GUARDAMOS LOS CAMPOS EN LAS PREFERENCIAS
            prefs.setPrefsNombreUsuario(textoNombre);
            prefs.setPrefsApellidosUsuario(apellidos.getText().toString().trim());
            prefs.setPrefsCiudadUsuario(ciudad.getText().toString().trim());
            prefs.setPrefsCPUsuario(cp.getText().toString().trim());
            prefs.setPrefsNacimientoUsuario(nacimiento.getText().toString().trim());
            prefs.setPrefsTelefonoUsuario(tlf.getText().toString().trim());
            prefs.setPrefsCorreoUsuario(correo.getText().toString().trim());
            prefs.setPrefsEstadoCivilUsuario(estadoCivil.getSelectedItemPosition());
            goToQuip();
        }
    }

    private void goToQuip(){
        Intent i = new Intent(this,VistaQuip.class);
        startActivity(i);
        finish();
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
    }
}
