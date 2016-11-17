package com.izv.dam.newquip.adaptadores;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.ItemNotaLista;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alumno on 25/10/2016.
 */

public class AdaptadorItemNotaLista extends RecyclerView.Adapter<AdaptadorItemNotaLista.ViewHolder> {

    private List<ItemNotaLista> lista;
    private List<ItemNotaLista> borrados;
    private boolean viewsAreEnabled; //indica si los elementos se veran o no;
    private boolean focusUltimo;

    public AdaptadorItemNotaLista(List<ItemNotaLista> lista){
        this.lista=lista;
        borrados = new ArrayList<>();
        focusUltimo=false;
    }

    public  AdaptadorItemNotaLista(){
        this(new ArrayList<ItemNotaLista>());
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {

        private CheckBox cb;
        private EditText et;
        private ImageButton ib;
        public ViewHolder(View itemView) {
            super(itemView);
            cb= (CheckBox)  itemView.findViewById(R.id.item_nota_lista_marcado);
            et = (EditText) itemView.findViewById(R.id.item_nota_lista_texto);
            ib = (ImageButton) itemView.findViewById(R.id.item_nota_lista_boton_borrar_item);


            //EVENTOS

            ib.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.super.getAdapterPosition();
                    borrarItem(position);
                    notifyDataSetChanged();
                }
            });
            // EVENTOS PARA GUARDAR EN TIEMPO REAL LOS CAMBIOS QUE HACEMOS EN LOS ELEMENTOS DE LA LISTA.



            cb.setOnClickListener(new View.OnClickListener() { // ES MEJOR AGREGAR AQUI LOS EVENTOS(los que afectan a elementos individuales, no a la lista entera), PARA QUE CUANDO HAGAS SCROLL NO TENGA QUE AÑADIR LOS LISTENER OTRA VEZ
                @Override
                public void onClick(View v) {
                    int position = ViewHolder.super.getAdapterPosition();
                    lista.get(position).setMarcado((((CheckBox)v).isChecked()));
                }
            });

            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    int position = ViewHolder.super.getAdapterPosition();
                    lista.get(position).setTexto(s.toString().trim());
                }
            });

            // FIN EVENTOS
        }

        public CheckBox getCb() {
            return cb;
        }

        public EditText getEt() {
            return et;
        }

        public ImageButton getIb() {
            return ib;
        }
    }

    @Override
    public AdaptadorItemNotaLista.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_lista,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorItemNotaLista.ViewHolder holder, final int position) {
            ItemNotaLista item = lista.get(position);
            CheckBox cb = holder.getCb();
            cb.setChecked(item.isMarcado());
            cb.setEnabled(viewsAreEnabled);



            if (viewsAreEnabled)
                holder.getIb().setVisibility(View.VISIBLE);
            else
                holder.getIb().setVisibility(View.GONE);

            EditText et = holder.getEt();
            et.setText(item.getTexto());
            et.setEnabled(viewsAreEnabled);


            if (position==(lista.size()-1)&&focusUltimo){
                et.requestFocus();
                focusUltimo=false;
            }
            //Log.v("ITEM",item.toString());

    }

    @Override
    public int getItemCount() {
        if(lista!=null)
            return lista.size();
        return 0;
    }


    public void borrarItem(int posicion){
        borrados.add(lista.remove(posicion));
        notifyDataSetChanged();
    }

    public void añadirItem(ItemNotaLista item){
        lista.add(item);
        focusUltimo=true;
        notifyDataSetChanged();
    }
    public void setLista(List<ItemNotaLista> lista){
        this.lista=lista;
        notifyDataSetChanged();
    }

    public List<ItemNotaLista> getLista() {
        return lista;
    }

    public List<ItemNotaLista> getBorrados() {
        return borrados;
    }

    public void setBorrados(List<ItemNotaLista> borrados) {
        this.borrados = borrados;
    }

    public boolean isViewAreEnabled() {
        return viewsAreEnabled;
    }

    public void setViewAreEnabled(boolean viewAreEnabled) {
        this.viewsAreEnabled = viewAreEnabled;
        notifyDataSetChanged();
    }

    public  void borrarListaCompleta(){
        for (ItemNotaLista item : lista) {
            borrados.add(item);
            //lista.remove(item); NO HACER ASI, EXPLOTA XD
        }
        lista = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void  vaciarContenitoTodosItems(){
        for (ItemNotaLista item : lista) {
            item.vaciarContenido(); //mantenemos la id, y la id de la lista.
           // item= new ItemNotaLista(id); NO FUNCIONA
        }
        notifyDataSetChanged();
    }

    public  void marcarDesmarcarTodosCheckBox(boolean nuevoValor){
        for (ItemNotaLista item : lista) {
            item.setMarcado(nuevoValor);
        }
        notifyDataSetChanged();
    }

    public  void invertirCheckBox(){
        for (ItemNotaLista item : lista) {
            item.setMarcado(!item.isMarcado());
        }
        notifyDataSetChanged();
    }

    public boolean comprobarTodosMarcados(){
        for (ItemNotaLista item : lista) {
            if (!item.isMarcado())
                return false;
        }
        return true;
    }
    public boolean comprobarTodosDesmarcados(){
        for (ItemNotaLista item : lista) {
            if (item.isMarcado())
                return false;
        }
        return true;
    }



}
