package com.izv.dam.newquip.vistas.main;
import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.adaptadores.AdaptadorNota;
import com.izv.dam.newquip.contrato.ContratoBaseDatos;
import com.izv.dam.newquip.contrato.ContratoMain;
import com.izv.dam.newquip.dialogo.DialogoPreferenciasNota;
import com.izv.dam.newquip.dialogo.interfaces.DialogoRecuperarNota;
import com.izv.dam.newquip.dialogo.interfaces.OnBorrarDialogListener;
import com.izv.dam.newquip.dialogo.interfaces.OnRecuperarDialogListener;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.dialogo.DialogoBorrar;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.Permisos;
import com.izv.dam.newquip.util.PreferenciasCompartidas;
import com.izv.dam.newquip.util.UtilFecha;
import com.izv.dam.newquip.vistas.Usuarios.VistaDatosUsuario;
import com.izv.dam.newquip.vistas.Usuarios.VistaMapa;
import com.izv.dam.newquip.vistas.Usuarios.VistaRegistro;
import com.izv.dam.newquip.vistas.VistaLienzo;
import com.izv.dam.newquip.vistas.notas.VistaNota;
import com.izv.dam.newquip.vistas.notas_lista.VistaNotaLista;
import java.io.File;

public class VistaQuip extends AppCompatActivity implements ContratoMain.InterfaceVista , OnBorrarDialogListener,LoaderManager.LoaderCallbacks<Cursor>,OnRecuperarDialogListener { //PREGUNTAR A CARMELO EL ERROR DEL ID 0 EN EL LOADER

    private PresentadorQuip presentador;

    private String rutaFoto;
    private RecyclerView rv;
    private TextView textoTipo;
    private DrawerLayout drawerLayout;
    public static ContratoMain.InterfaceVista REFERENCIA_MENU_PRINCIPAL; // ESTA INTERFAZ SE USARA PARA QUE EL PROVEEDOR ASINCRONO PUEDA ACTUALIZAR LOS DATOS CUANDO TERMINE SU TRABAJO

    public static final int ID_CURSOR_TODO = 1; // este es público porque lo vamos a llamar desde el Proveedor Asincrono.
    private static final int ID_CURSOR_NOTA = 2;
    private static final int ID_CURSOR_LISTA = 3;
    private static final int ID_CURSOR_MULTIMEDIA = 4;
    private static final int ID_CURSOR_PAPELERA = 5;
    public static int ID_CURSOR_ACTUAL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_quip);



        presentador = new PresentadorQuip(this);

        textoTipo = (TextView) findViewById(R.id.tv_quip_tipo_cursor);
        crearNavigationView(); // AÑADIDO PARA EL NAVIGATION DRAWER
        rv = (RecyclerView) findViewById(R.id.lvListaNotas);
        rv.setHasFixedSize(false); // true, la lista es estatica, false, los datos de la lista pueden variar.
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)); // forma en que se visualizan los elementos, en este caso en vertical.
        setUpItemTouchHelper();
        AdaptadorNota adaptador = new AdaptadorNota(getString(R.string.notaVacia), getString(R.string.listaVacia));
        adaptador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presentador.onClickNota(rv.getChildAdapterPosition(v));
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
       final FloatingActionsMenu fam = (FloatingActionsMenu) findViewById(R.id.vista_principal_menuFloat);

        FloatingActionButton anadirNota = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_normal);
        anadirNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.collapse();
                presentador.onAddNota();
            }
        });

        FloatingActionButton anadirNotaLista = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_lista);
        anadirNotaLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.collapse();
                presentador.onAddNotaLista();
            }
        });

        FloatingActionButton anadirImagen = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_imagen);
        anadirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.collapse();
                if (Permisos.solicitarPermisos(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, VistaQuip.this)) { //AÑADIDA SOLICITUD DE PERMISOS, PORQUE AHORA CREA UN ARCHIVO NUEVO.
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");//para que busque cualquier tipo de imagen
                    startActivityForResult(intent.createChooser(intent, getString(R.string.appImagen)), Permisos.GALERIA);
                    Toast.makeText(VistaQuip.this, R.string.nuevaNotaImagen, Toast.LENGTH_SHORT).show();
                }
            }
        });

        FloatingActionButton anadirDibujo = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_dibujo);
        anadirDibujo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fam.collapse();
                Toast.makeText(VistaQuip.this, R.string.nuevaNotaDibujo, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(VistaQuip.this, VistaLienzo.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.left_out);
            }
        });

        FloatingActionButton sacarFoto = (FloatingActionButton) findViewById(R.id.vista_principal_anadir_nota_camara);
        sacarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fam.collapse();
                if (Permisos.solicitarPermisos(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},VistaQuip.this)) {
                    new AsyncTask<Void,Void,Intent>() {  // añadido el proceso en 2º plano.
                        @Override
                        protected Intent doInBackground(Void... params) {
                            Long timestamp = System.currentTimeMillis() / 1000;
                            DirectoriosArchivosQuip.comprobarDirectorioImagenes();
                            String nombreImagen = timestamp.toString() + ".jpg";
                            rutaFoto = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;
                            File newFile = new File(rutaFoto);
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
            }
        });
        //FIN MENU DE FLOAT BUTTONS


        if (savedInstanceState != null) {
            System.out.println(savedInstanceState.getInt("cursor_actual"));
            VistaQuip.ID_CURSOR_ACTUAL = savedInstanceState.getInt("cursor_actual"); // coge del bundle el cursor actual, sino existe le asocia el de todos.
            textoTipo.setText(savedInstanceState.getString("texto"));
        } else {
            VistaQuip.ID_CURSOR_ACTUAL = ID_CURSOR_TODO;
        }

        AdaptadorNota.ANIMACIONES_ADAPTADOR=true;//LA 1ª VEZ SI HAY ANIMACIONES.
        getSupportLoaderManager().initLoader(VistaQuip.ID_CURSOR_ACTUAL, null, this); // LO 1º ES ID DEL MANAGER, , LO 2º SON LOS ARGUMENTOS.

    }




    @Override
    protected void onPause() {
        presentador.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void mostrarAgregarNota() {
        Toast.makeText(VistaQuip.this, getResources().getString(R.string.nuevaNota), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNota.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void mostrarAgregarNotaLista() {
        Toast.makeText(VistaQuip.this, getResources().getString(R.string.nuevaNotaLista), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, VistaNotaLista.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void mostrarDatos(Cursor c) {
        ((AdaptadorNota) rv.getAdapter()).setCursor(c);
        // System.out.println(adaptador.getItemCount());
    }

    @Override
    public void mostrarEditarNota(Nota n) {
        String textoTostada;
        Intent i;
        if (n.getTipo() == Nota.TIPO_LISTA) { // AÑADIDO ELEGIR QUE TIPO DE NOTA VAMOS A ABRIR
            textoTostada = getResources().getString(R.string.editarNotaLista);
            i = new Intent(this, VistaNotaLista.class);
        } else {
            textoTostada = getResources().getString(R.string.editarNota);
            i = new Intent(this, VistaNota.class);
        }

        Toast.makeText(VistaQuip.this, textoTostada, Toast.LENGTH_SHORT).show();
        Bundle b = new Bundle();
        b.putParcelable("nota", n);
        i.putExtras(b);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
    }

    @Override
    public void mostrarConfirmarBorrarNota(Nota n) {
        DialogoBorrar fragmentBorrar = DialogoBorrar.newInstance(n);
        fragmentBorrar.show(getSupportFragmentManager(), "Dialogo borrar");
    }


    @Override
    public void onBorrarPossitiveButtonClick(Nota n) {
        AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
        presentador.onDeleteNota(n);
        //getSupportLoaderManager().restartLoader(0,null,this);
    }

    @Override
    public void onBorrarNegativeButtonClick() {
        //añadido por el deslizador, para que vuelva a pintar los items...xd
        AdaptadorNota.ANIMACIONES_ADAPTADOR=false;
        rv.getAdapter().notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Permisos.GALERIA: {
                if (resultCode == RESULT_OK) {

                    new AsyncTask<Object, Void, String>() { //añadido en 2º plano.

                        @Override
                        protected String doInBackground(Object... params) {

                            Intent data = (Intent) params[0];
                            String rutaAbsoluta = DirectoriosArchivosQuip.getRutaImagen(data.getData(), VistaQuip.this);
                            String nombreImagen = UtilFecha.fechaActual() + rutaAbsoluta.substring(rutaAbsoluta.lastIndexOf("/") + 1, rutaAbsoluta.length());
                            String rutaNuevaImagen = Environment.getExternalStorageDirectory() + File.separator + DirectoriosArchivosQuip.IMAGE_DIRECTORY + File.separator + nombreImagen;

                            DirectoriosArchivosQuip.crearImagen(rutaNuevaImagen, BitmapFactory.decodeFile(rutaAbsoluta), VistaQuip.this); // le pasas una ruta,una imagen, y te crea un nuevo archivo en esa ruta con esa imagen
                            return rutaNuevaImagen;
                        }

                        @Override
                        protected void onPostExecute(String s) {
                            Nota nota = new Nota(Nota.TIPO_IMAGEN);
                            nota.setImagen(s);
                            Intent i = new Intent(VistaQuip.this, VistaNota.class);
                            Bundle b = new Bundle();
                            b.putParcelable("nota", nota);
                            i.putExtras(b);

                            startActivity(i);
                            overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                        }
                    }.execute(data);
                }
                break;
            }

            case Permisos.HACER_FOTO: {
                if (resultCode == RESULT_OK) {
                    MediaScannerConnection.scanFile(this, new String[]{rutaFoto}, null, new MediaScannerConnection.OnScanCompletedListener() {
                        @Override
                        public void onScanCompleted(String path, Uri uri) {
                            Log.i("ExternalStorage", "Scanned " + path + ":");
                            Log.i("ExternalStorage", "-> Uri = " + uri);
                        }
                    });
                    Nota n = new Nota(Nota.TIPO_IMAGEN);
                    n.setImagen(rutaFoto);
                    Intent i = new Intent(VistaQuip.this, VistaNota.class);
                    Bundle b = new Bundle();

                    b.putParcelable("nota", n);
                    i.putExtras(b);
                    startActivity(i);
                    overridePendingTransition(R.anim.zoom_in, R.anim.zoom_out);
                }
                rutaFoto=null;
                break;
            }
        }
    }


    //LOAD MANAGER
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) { // HACE LA CONSULTA CON LOS DATOS,el ID ES EL ID A USAR DEL MANAGER
        switch (id) {
            case VistaQuip.ID_CURSOR_TODO: {
                String where = ContratoBaseDatos.TablaNota.PAPELERA + "= ?";
                String[] valores = new String[]{String.valueOf(0)};
                textoTipo.setText(getString(R.string.cursorTipoTodo));
                return new CursorLoader(this, ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, where, valores, ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
            }
            case ID_CURSOR_NOTA: {
                String where = ContratoBaseDatos.TablaNota.TIPO + "<>? and " + ContratoBaseDatos.TablaNota.PAPELERA + "=?";
                String[] valores = new String[]{String.valueOf(Nota.TIPO_LISTA), String.valueOf(0)};
                textoTipo.setText(getString(R.string.cursorTipoSoloNota));
                return new CursorLoader(this, ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, where, valores, ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
            }
            case ID_CURSOR_LISTA: {
                String where = ContratoBaseDatos.TablaNota.TIPO + "=? and " + ContratoBaseDatos.TablaNota.PAPELERA + "=?";
                String[] valores = new String[]{String.valueOf(Nota.TIPO_LISTA), String.valueOf(0)};
                textoTipo.setText(getString(R.string.cursorTipoSoloListas));
                return new CursorLoader(this, ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, where, valores, ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
            }
            case ID_CURSOR_MULTIMEDIA: {
                String where = "(" + ContratoBaseDatos.TablaNota.IMAGEN + " is not null or " + ContratoBaseDatos.TablaNota.AUDIO + " is not null) and " + ContratoBaseDatos.TablaNota.PAPELERA + "=?";
                String[] valores = new String[]{String.valueOf(0)};
                textoTipo.setText(getString(R.string.cursorTipoSoloNotaMultimedia));
                return new CursorLoader(this, ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, where, valores, ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
            }

            case VistaQuip.ID_CURSOR_PAPELERA: {
                String where = ContratoBaseDatos.TablaNota.PAPELERA + "=?";
                String[] valores = new String[]{String.valueOf(1)};
                textoTipo.setText(getString(R.string.cursorTipoPapelera));
                return new CursorLoader(this, ContratoBaseDatos.TablaNota.CONTENT_URI_NOTA, ContratoBaseDatos.TablaNota.PROJECTION_ALL, where, valores, ContratoBaseDatos.TablaNota.SORT_ORDER_DEFAULT);
            }
            default: {
                return null;
            }
        }

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) { //ASIGNA EL CURSOR A EL ADAPTADOR

        if (data != null) {
            ((AdaptadorNota) rv.getAdapter()).setCursor(data);
            presentador.cargarCursorModelo(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) { // ELIMINA LA REFERENCIA DE DATOS Y DEVUELVE EL ADAPTADOR A SU ESTADO INICIAL
        ((AdaptadorNota) rv.getAdapter()).setCursor(null);
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
            localThrowable.printStackTrace();
        }
    }

    //FIN LOAD MANAGER

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quip, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_quip_configuracion: {
                DialogoPreferenciasNota dialogo = DialogoPreferenciasNota.newInstance();
                dialogo.show(getSupportFragmentManager(), "Dialogo Preferencias");
                return true;
            }
            case R.id.menu_quip_borrar_notas_no_titulo: {
                String where = "length(titulo)=0 and length(nota)=0 and " + ContratoBaseDatos.TablaNota.TIPO + "<>" + Nota.TIPO_LISTA;
                presentador.borrarConjunto(where, false);
                return true;
            }
            case R.id.menu_quip_borrar_listas_no_titulo: {
                String where = "length(titulo)=0 and " + ContratoBaseDatos.TablaNota.TIPO + "=" + Nota.TIPO_LISTA;
                presentador.borrarConjunto(where, false);
                return true;
            }

            case R.id.menu_quip_vaciar_papelera: {
                presentador.borrarConjunto(null, true);
                return true;
            }
            case  android.R.id.home :{ //BOTÓN DEL NAVIGATION DRAWER
                NavigationView nav = (NavigationView) findViewById(R.id.nav_view);
                if(drawerLayout.isDrawerOpen(nav))
                    drawerLayout.closeDrawers();
                else
                    drawerLayout.openDrawer(nav);
            }
            default:
                return false;
        }
    }


    @Override
    public void reiniciarDatos(int id) { //USADO PARA LAS CONSULTAS A LA BASE DE DATOS EN 2º PLANO
        getSupportLoaderManager().restartLoader(id, null, this);
    }

    @Override
    protected void onStart() {
        VistaQuip.REFERENCIA_MENU_PRINCIPAL = this;
        inicializarValoresUsuarioNavHeader(); // Para cuando volvamos de otro layout, que se actualic en por si han cambiado.
        super.onStart();
    }

    private void crearNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        crearDrawerToggle();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.closeDrawers();
                switch (item.getItemId()) {
                    case R.id.nav_todas_notas: {
                        ID_CURSOR_ACTUAL = ID_CURSOR_TODO;
                        AdaptadorNota.ANIMACIONES_ADAPTADOR=true;
                        reiniciarDatos(ID_CURSOR_TODO);
                        return true;
                    }
                    case R.id.nav_solo_notas_normales: {
                        ID_CURSOR_ACTUAL = ID_CURSOR_NOTA;
                        AdaptadorNota.ANIMACIONES_ADAPTADOR=true;
                        reiniciarDatos(ID_CURSOR_NOTA);
                        return true;
                    }
                    case R.id.nav_solo_notas_multimedia: {
                        ID_CURSOR_ACTUAL = ID_CURSOR_MULTIMEDIA;
                        AdaptadorNota.ANIMACIONES_ADAPTADOR=true;
                        reiniciarDatos(ID_CURSOR_MULTIMEDIA);
                        return true;
                    }
                    case R.id.nav_solo_listas: {
                        ID_CURSOR_ACTUAL = ID_CURSOR_LISTA;
                        AdaptadorNota.ANIMACIONES_ADAPTADOR=true;
                        reiniciarDatos(ID_CURSOR_LISTA);
                        return true;
                    }
                    case R.id.nav_view_datos_usuario: {
                        Intent i = new Intent(VistaQuip.this, VistaDatosUsuario.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        return true;
                    }
                    case R.id.nav_view_desconectar: {
                        presentador.desconectarUsuario(VistaQuip.this);
                        Intent i = new Intent(VistaQuip.this, VistaRegistro.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.right_in, R.anim.right_out);
                        finish();
                        return true;
                    }

                    case R.id.nav_view_papelera: {
                        ID_CURSOR_ACTUAL = ID_CURSOR_PAPELERA;
                        AdaptadorNota.ANIMACIONES_ADAPTADOR=true;
                        reiniciarDatos(ID_CURSOR_PAPELERA);
                        return true;
                    }
                    case R.id.nav_map: {
                        if (Permisos.solicitarPermisos(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, VistaQuip.this)) {
                            Intent i = new Intent(VistaQuip.this, VistaMapa.class);
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in, R.anim.left_out);
                        }
                        return true;
                    }
                    default:
                        return false;
                }
            }
        });
    }


    private void inicializarValoresUsuarioNavHeader(){
        PreferenciasCompartidas prefs = new PreferenciasCompartidas(this);

        NavigationView nav = (NavigationView) findViewById(R.id.nav_view); // los ids no estan en quip, sino dentro del header del navigation.
        View header = nav.getHeaderView(0); // 0 es porque es el primero, si estuviera el 2º en el layout seria el 1
        TextView tvNombre = (TextView) header.findViewById(R.id.nav_header_usuario_name);
        tvNombre.setText(prefs.getPrefsNombreUsuario());
        String correo = prefs.getPrefsCorreoUsuario();
        TextView tvCorreo = (TextView) header.findViewById(R.id.nav_header_user_mail);
        tvCorreo.setText(correo);

        ImageView avatar= (de.hdodenhof.circleimageview.CircleImageView) header.findViewById(R.id.nav_header_avatar);
        String rutaAvatar = prefs.getPrefsAvatarUsuario();
        if (rutaAvatar!=null && !rutaAvatar.isEmpty()) {
            avatar.setImageBitmap(BitmapFactory.decodeFile(rutaAvatar));
        }
        else
            avatar.setImageDrawable(getDrawable(R.drawable.ic_no_avatar));
    }
    private  void crearDrawerToggle() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        final ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.empty,R.string.empty) {


            @Override
            public void onDrawerClosed(View drawerView) {
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu();
            }
        };



        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });

        drawerLayout.addDrawerListener(drawerToggle);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) { //cursor actual.
        super.onSaveInstanceState(outState);
        outState.putInt("cursor_actual", VistaQuip.ID_CURSOR_ACTUAL);
        outState.putString("texto",textoTipo.getText().toString());
    }


    private void setUpItemTouchHelper() {

        ItemTouchHelper.SimpleCallback leftItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            // we want to cache these and not allocate anything repeatedly in the onChildDraw method
            Drawable xMark;
            int xMarkMargin;
            boolean initiated;

            private void init() {
                // background = new ColorDrawable(Color.RED);
                xMark = ContextCompat.getDrawable(VistaQuip.this, R.drawable.ic_action_borrar_item);
                xMark.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP);
                xMarkMargin = (int) VistaQuip.this.getResources().getDimension(R.dimen.ic_clear_margin);
                initiated = true;
            }

            // not important, we don't want drag & drop
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
                return super.getSwipeDirs(recyclerView, viewHolder);
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                presentador.onShowBorrarNota(viewHolder.getAdapterPosition());
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                View itemView = viewHolder.itemView;

                // not sure why, but this method get's called for viewholder that are already swiped away
                if (viewHolder.getAdapterPosition() == -1) {
                    // not interested in those
                    return;
                }

                if (!initiated) {
                    init();
                }

                // draw x mark
                int itemHeight = itemView.getBottom() - itemView.getTop();
                int intrinsicWidth = xMark.getIntrinsicWidth();
                int intrinsicHeight = xMark.getIntrinsicWidth();

                int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;
                int xMarkRight = itemView.getRight() - xMarkMargin;
                int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
                int xMarkBottom = xMarkTop + intrinsicHeight;
                xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);

                xMark.draw(c);

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

        };
        ItemTouchHelper leftItemTouchHelper = new ItemTouchHelper(leftItemTouchCallback);
        leftItemTouchHelper.attachToRecyclerView(rv);
    }


   public void mostrarConfirmarRecuperarNota(Nota n) {
       DialogoRecuperarNota fragmentBorrar = DialogoRecuperarNota.newInstance(n);
       fragmentBorrar.show(getSupportFragmentManager(), "Dialogo recuperar nota");
   }

    @Override
    public void onRecuperarNotaPossitiveButtonClick(Nota n) {
        presentador.recuperarNota(n);
    }

    @Override
    public void onRecuperarNotaNegativeButtonClick() {

    }
}