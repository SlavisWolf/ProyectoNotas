package com.izv.dam.newquip.util;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompatSideChannelService;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by anton on 08/11/2016.
 */

public class DirectoriosArchivosQuip {

   // public static String APP_DIRECTORY = "MyPicture"+ File.separator;
    public static String MEDIA_DIRECTORY = /*APP_DIRECTORY +*/"QuipData";//carpeta que me crea
    public static String  IMAGE_DIRECTORY = MEDIA_DIRECTORY+File.separator+"images";
    public static String  AUDIO_DIRECTORY = MEDIA_DIRECTORY+File.separator+"audios";
    public static String  PDF_DIRECTORY = MEDIA_DIRECTORY+File.separator+"pdfs";

    public static  void comprobarDirectorioImagenes() {
        File dir1 = new File(Environment.getExternalStorageDirectory() + File.separator+IMAGE_DIRECTORY);

        if (!dir1.exists()) {
            dir1.mkdirs();
        }
    }

    public static void comprobarDirectorioAudio() {
        File dir1 = new File(Environment.getExternalStorageDirectory() + File.separator+AUDIO_DIRECTORY);
        if (!dir1.exists()) {
            dir1.mkdirs();
        }
    }

    public static String getRutaImagen(Uri uri, Context c) {
        String[] projection = { MediaStore.Images.Media.DATA };
        ContentResolver cr = c.getContentResolver();
        Cursor cursor = cr.query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public  static  void crearImagen(String ruta, Bitmap bmp, Context c) {
        try {
            comprobarDirectorioImagenes();
            File file_imagen = new File(ruta);
            file_imagen.createNewFile();

            FileOutputStream file = new FileOutputStream(ruta);
            bmp.compress(Bitmap.CompressFormat.PNG, 0, file);
            MediaScannerConnection.scanFile(c,
                    new String[]{file_imagen.toString()},
                    null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                        }
                    });
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public  static void  borrarArchivo(String ruta) {
        File f = new File(ruta);
        if (f.delete()) { // da true, pero no borra...xd
            Log.v("BORROOO","BORROOO");
        }
        else {
            Log.v("FALLOOOO","FALOOO");
        }
    }
}
