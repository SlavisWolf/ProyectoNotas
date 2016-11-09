package com.izv.dam.newquip.util;

/**
 * Created by alumno on 20/10/2016.
 */

public class UtilBoolean {

    public static Byte booleanToByte(Boolean booleano) {
        if (booleano) {
            return 1;
        }
        return 0;
    }
    public static boolean byteToBoolean(Byte b) {
        if (b==1) {
            return true;
        }
        return false;
    }

    public static boolean intToBoolean(int n){
        if (n==1) {
            return true;
        }
        return false;
    }
    public static int booleanToInt(Boolean booleano){
        if (booleano){
            return 1;
        }
        return 0;
    }
}
