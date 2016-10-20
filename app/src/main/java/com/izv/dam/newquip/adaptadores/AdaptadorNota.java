package com.izv.dam.newquip.adaptadores;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.Nota;

import java.util.ArrayList;
import java.util.List;


public class AdaptadorNota  extends RecyclerView.Adapter<AdaptadorNota.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{


    private Cursor cursor; //aqui meteremos todos los items que usara recycler view, en este ejemplo son Strings, pero en el proyecto seran notas
    //private List<Nota> lista;
    private View.OnClickListener clickListener;
    private View.OnLongClickListener longClickListener;

    public AdaptadorNota(Cursor cursor) {
        this.cursor = cursor;
    }

    /*public AdaptadorNota (List<Nota> list){
        this.lista=lista;
    }*/

    public AdaptadorNota(){
        this(null);
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

    public class ViewHolder extends  RecyclerView.ViewHolder {
        private TextView tvTituloNota;
        public ViewHolder(View itemView) { // aqui irian todos los elementos del layout
            super(itemView);
            tvTituloNota = (TextView) itemView.findViewById(R.id.tvTituloNota);
        }
        public TextView getTextView(){
            return tvTituloNota;
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
        /*lista = new ArrayList<>();
        while (cursor.moveToNext()) {
            lista.add(Nota.getNota(cursor));
        }*/
        notifyDataSetChanged();
    }

    @Override
    public AdaptadorNota.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { // esto es el layout por asi decirlo de cada item
        LayoutInflater inflador = LayoutInflater.from(parent.getContext());// el contexto se puede pasar tambien por el constructor.
        View vista = inflador.inflate(R.layout.item,parent,false); // el ultimo parametro no estoy seguro de como funciona, en false evita duplicados, pero hay que invergarlo mas
        vista.setOnClickListener(this);
        vista.setOnLongClickListener(this);
        return new ViewHolder(vista);// devolvemos un objeto de nuestra clase ViewHolder con la vista que acabamos de inflar.
    }

    @Override
    public void onBindViewHolder(AdaptadorNota.ViewHolder holder, int position) { // aqui se le asignan los valores a  los elementos de los item , en este caso solo es 1cur;
        if (cursor.moveToPosition(position)) {
            String titulo = Nota.getNota(cursor).getTitulo();
            holder.getTextView().setText(titulo);
            Log.v("LOG","HA ENTRADO "+titulo);
        }
       /* String titulo = lista.get(position).getTitulo();
        holder.getTextView().setText(titulo);
        Log.v("LOG","HA ENTRADO "+titulo);*/

    }

    @Override
    public int getItemCount() {
        if (cursor==null) {
            return 0;
        }
        return cursor.getCount();
        /*if (lista==null){
            return 0 ;
        }
        return  lista.size();*/
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