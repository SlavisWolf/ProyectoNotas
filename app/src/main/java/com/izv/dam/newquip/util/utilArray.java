package com.izv.dam.newquip.util;

import com.izv.dam.newquip.pojo.ItemNotaLista;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anton on 05/11/2016.
 */

public class UtilArray {

    public static ArrayList<ItemNotaLista> listToArrayList(List<ItemNotaLista> list)  {
        ArrayList<ItemNotaLista> array = new ArrayList<>();
        for (int i=0 ;i<list.size();i++) {
            array.add(list.get(i));
        }
        return array;
    }
}
