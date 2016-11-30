package com.izv.dam.newquip.adaptadores;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Nota;
import com.izv.dam.newquip.util.UtilBoolean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.List;
import java.util.Random;


public class AdaptadorNota  extends RecyclerView.Adapter<AdaptadorNota.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{



    public  static  boolean ANIMACIONES_ADAPTADOR; //EN EL CONTENT ASINCRONO, LOS CAMBIOS EN LOS DATOS DESACTIVAN LAS ANIMACIONES, LOS CAMBIOS DE FILTRO LAS PONEN A
                                                    // VERDADERO, Y LA PRIMERA CARGA, TAMBIEN SERA VERDADERO
    private Cursor cursor;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    private String textoNotasVacias;
    private String textoListasVacias;


    public AdaptadorNota(Cursor cursor,String textoNotasVacias,String textoListasVacias) {
        this.cursor = cursor;
        this.textoNotasVacias=textoNotasVacias;
        this.textoListasVacias=textoListasVacias;
    }


    public AdaptadorNota(String textoNotasVacias,String textoListasVacias){
        this(null,textoNotasVacias,textoListasVacias);
    }



    @Override
    public void onClick(View v) {
        if(clickListener != null)
            clickListener.onClick(v);
    }

    @Override
    public boolean onLongClick(View v) {
        if(longClickListener != null) {
            longClickListener.onLongClick(v);
            return true;
        }
        return false;
    }

    public class ViewHolder extends  RecyclerView.ViewHolder { //USADO PARA LAS NOTAS NORMALES
        private TextView tvTituloNota;
        private TextView tvRecordatorio;
        private ImageView ivTipoNota;
        private ImageView ivRecordatorio;
        public ViewHolder(View itemView) { // aqui irian todos los elementos del layout
            super(itemView);
            tvTituloNota = (TextView) itemView.findViewById(R.id.tvTituloNota);
            //tvRecordatorio = (TextView) itemView.findViewById(R.id.tvFechaRecordatorioNota);
            ivTipoNota = (ImageView) itemView.findViewById(R.id.imagenTipoNota);
            //ivRecordatorio = (ImageView) itemView.findViewById(R.id.imagenRecordatorio);
        }
        public TextView getTextView(){
            return tvTituloNota;
        }
        public ImageView getIvTipoNota() {
            return ivTipoNota;
        }
    }



    public void setOnClickListener(View.OnClickListener listener) {
        this.clickListener = listener;
    }

    public void setLongClickListener(View.OnLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public AdaptadorNota.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // esto es el layout por asi decirlo de cada item
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());// el contexto se puede pasar tambien por el constructor.
        View vista = inflador.inflate(R.layout.item,parent,false); // el ultimo parametro no estoy seguro de como funciona, en false evita duplicados, pero hay que investigarlo mas
        vista.setOnClickListener(this);
        vista.setOnLongClickListener(this);
        return new ViewHolder(vista);// devolvemos un objeto de nuestra clase ViewHolder con la vista que acabamos de inflar.
    }

    @Override
    public void onBindViewHolder(AdaptadorNota.ViewHolder holder, int position) { // aqui se le asignan los valores a  los elementos de los item , en este caso solo es 1cur;
        if (cursor.moveToPosition(position)) {
            Nota nota = Nota.getNota(cursor);
            TextView tv = holder.getTextView();
            //TEXTVIEW
            if  (nota.getTitulo()!=null && !nota.getTitulo().isEmpty()) {
                    tv.setText(nota.getTitulo());
            }
            else {
                if (nota.getNota()!=null && !nota.getNota().isEmpty()) {
                        tv.setText(nota.getNota());
                }
                else {
                    if (nota.getTipo()==Nota.TIPO_LISTA) {
                        tv.setText(textoListasVacias);
                    }
                    else {
                        tv.setText(textoNotasVacias);
                    }
                }
            }

            Log.v("TIPO",nota.getTipo()+"");
            switch (nota.getTipo()) { //AQUI CAMBIAREMOS LA IMAGEN QUE SE MUESTRA PARA QUE EL USUARIO SEPA A 1ª VISTA QUE TIPO DE NOTA ES.
                case Nota.TIPO_DEFECTO: {
                    holder.getIvTipoNota().setImageResource(R.drawable.ic_tipo_defecto48px);
                    break;
                }
                case Nota.TIPO_IMAGEN: {
                    holder.getIvTipoNota().setImageResource(R.drawable.ic_tipo_imagen48px);
                    break;
                }
                case Nota.TIPO_DIBUJO: {
                    holder.getIvTipoNota().setImageResource(R.drawable.ic_tipo_dibujo48px);
                    break;
                }
                case Nota.TIPO_AUDIO: {
                    break;
                }
                case Nota.TIPO_LISTA: {
                    holder.getIvTipoNota().setImageResource(R.drawable.ic_tipo_lista48px);
                    break;
                }
            }

            if (AdaptadorNota.ANIMACIONES_ADAPTADOR) {
                setScaleAnimation(holder.itemView);
            }
            //ESPACIO PARA RECORDATORIO, IMPLEMENTAR MÁS ADELANTE
        }
    }




    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(3000);
        view.startAnimation(anim);
    }

    private void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(new Random().nextInt(501));//to make duration random number between [0,501)
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        if (cursor==null) {
            return 0;
        }
        return cursor.getCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (cursor.moveToPosition(position)) {
            return UtilBoolean.booleanToInt(Nota.getNota(cursor).isPapelera()); //devolvemos el tipo que tiene guardado la nota.
        }
        return -1;
    }
}



//VERSION ANTIGUA ADAPTADOR PARA LISTVIEW
/*public class AdaptadorNota extends CursorAdapter {

    public AdaptadorNota(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv = (TextView) view.findViewById(R.id.tvTituloNota);
        Nota n = Nota.getNota(cursor);
        tv.setText(n.getTitulo());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item, parent, false);
    }
}*/