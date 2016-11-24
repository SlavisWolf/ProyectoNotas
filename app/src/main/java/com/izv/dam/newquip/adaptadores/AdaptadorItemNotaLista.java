package com.izv.dam.newquip.adaptadores;

import android.database.Cursor;
import android.databinding.DataBindingUtil;
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
import com.izv.dam.newquip.databinding.ItemNotaListaBinding;
import com.izv.dam.newquip.pojo.ItemNotaLista;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alumno on 25/10/2016.
 */

public class AdaptadorItemNotaLista extends RecyclerView.Adapter<AdaptadorItemNotaLista.BindingViewHolder> {

    private ArrayList<ItemNotaLista> lista;
    private ArrayList<ItemNotaLista> borrados;
    private boolean viewsAreEnabled; //indica si los elementos se veran o no;
    private boolean focusUltimo;

    public AdaptadorItemNotaLista(ArrayList<ItemNotaLista> lista){
        this.lista=lista;
        borrados = new ArrayList<>();
        focusUltimo=false;
    }

    public  AdaptadorItemNotaLista(){
        this(new ArrayList<ItemNotaLista>());
    }

    public class BindingViewHolder extends  RecyclerView.ViewHolder {

        private ItemNotaListaBinding binding;
        /*private CheckBox cb;
        private EditText et;
        private ImageButton ib;*/

        public BindingViewHolder(View itemView) {
            super(itemView);

            binding =DataBindingUtil.bind(itemView);
           /* cb= (CheckBox)  itemView.findViewById(R.id.item_nota_lista_marcado);
            et = (EditText) itemView.findViewById(R.id.item_nota_lista_texto);
            ib = (ImageButton) itemView.findViewById(R.id.item_nota_lista_boton_borrar_item);*/


            //EVENTOS

            binding.itemNotaListaBotonBorrarItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = BindingViewHolder.super.getAdapterPosition();
                    borrarItem(position);
                    //notifyDataSetChanged(); REDUNDANTE
                }
            });
            // EVENTOS PARA GUARDAR EN TIEMPO REAL LOS CAMBIOS QUE HACEMOS EN LOS ELEMENTOS DE LA LISTA.



            binding.itemNotaListaMarcado.setOnClickListener(new View.OnClickListener() { // ES MEJOR AGREGAR AQUI LOS EVENTOS(los que afectan a elementos individuales, no a la lista entera), PARA QUE CUANDO HAGAS SCROLL NO TENGA QUE AÑADIR LOS LISTENER OTRA VEZ
                @Override
                public void onClick(View v) {
                    int position = BindingViewHolder.super.getAdapterPosition();
                    lista.get(position).setMarcado((((CheckBox)v).isChecked()));
                }
            });

            // FIN EVENTOS
        }

        public ItemNotaListaBinding getBinding() {
            return binding;
        }
    }

    @Override
    public AdaptadorItemNotaLista.BindingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       // ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_nota_lista, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_nota_lista,parent,false);
        return new BindingViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdaptadorItemNotaLista.BindingViewHolder holder, final int position) {
            ItemNotaLista item = lista.get(position);
            ItemNotaListaBinding binding = holder.getBinding();
            binding.setItemLista(item);
            binding.setEditable(viewsAreEnabled);
            if (position==(lista.size()-1)&&focusUltimo){
                binding.itemNotaListaTexto.requestFocus();
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
        ItemNotaLista item = lista.remove(posicion);
        borrarElementoTrim(item);
        notifyDataSetChanged();
    }

    public void añadirItem(ItemNotaLista item){
        lista.add(item);
        focusUltimo=true;
        notifyDataSetChanged();
    }
    public void setLista(ArrayList<ItemNotaLista> lista){
        this.lista=lista;
        notifyDataSetChanged();
    }

    public ArrayList<ItemNotaLista> getLista() {
        return lista;
    }

    public ArrayList<ItemNotaLista> getBorrados() {
        return borrados;
    }

    public void setBorrados(ArrayList<ItemNotaLista> borrados) {
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
            borrarElementoTrim(item);
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


    private void borrarElementoTrim(ItemNotaLista item) {
        item.setTexto(item.getTexto().trim());
        borrados.add(item);
    }

    public  void trimTextosLista(){
        for (ItemNotaLista item : lista)
                item.setTexto(item.getTexto().trim());
    }



}
