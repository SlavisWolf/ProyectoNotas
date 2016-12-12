package com.izv.dam.newquip.VinculacionDatos;

import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * Created by anton on 18/11/2016.
 */

public class MetodosBinding {


/*   private  static  String ruta; //RUTA IMAGEN

    @BindingAdapter({"android:src"})
    public static String getImagenConRuta(ImageView view,Drawable vacio) {
        return MetodosBinding.ruta;
    }*/

    @BindingAdapter({"android:src"}) // METODO USADO PARA QUE SE SINCRONICE EL METODO ESTE CON LA IMAGEN DE LA NOTA.
    public static void setImagenConRuta(ImageView view, String ruta) {
        if (ruta==null || ruta.isEmpty()) {
            view.setImageResource(0);
        }
        else {
            WeakReference<Bitmap> reference = new WeakReference<Bitmap>(BitmapFactory.decodeFile(ruta));
            if (reference.get()!=null) {
                view.setImageBitmap(reference.get());
                reference.clear();
            }
            else {
                view.setImageResource(0);
            }
        }
    }
}
