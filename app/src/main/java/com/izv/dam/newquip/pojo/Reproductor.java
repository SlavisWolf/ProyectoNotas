package com.izv.dam.newquip.pojo;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import com.izv.dam.newquip.R;
import com.izv.dam.newquip.util.DirectoriosArchivosQuip;
import com.izv.dam.newquip.util.UtilFecha;

import java.io.File;
import java.io.IOException;

/**
 * Created by dam on 29/11/16.
 */

public class Reproductor  {

    MediaRecorder recorder;
    boolean grabando;
    MediaPlayer player;
    File archivo;
    Context con;

    public Reproductor(Context c,File archivo,MediaPlayer.OnCompletionListener listener) {
        con = c;
        grabando=false;
        this.archivo=archivo;
        inicializarPlayer(listener);
    }

    public Reproductor(Context c) {
        con = c;
        grabando=false;
    }

    public void grabarAudio(String tituloNota){

        if (player!=null) {
            player.release();
            player=null;
        }
        recorder = new MediaRecorder();
        Toast.makeText(con,R.string.audioGrabando,Toast.LENGTH_SHORT).show();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        String nombre = UtilFecha.fechaActual()+tituloNota+".3gp";
        DirectoriosArchivosQuip.comprobarDirectorioAudio();
        archivo = new File(Environment.getExternalStorageDirectory() + File.separator +DirectoriosArchivosQuip.AUDIO_DIRECTORY+File.separator+nombre);
        recorder.setOutputFile(archivo.getAbsolutePath());
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();
        grabando=true;
    }


    public  void detenerGrabacion(MediaPlayer.OnCompletionListener listener) {
        Toast.makeText(con,R.string.audioParar,Toast.LENGTH_SHORT).show();
        recorder.stop();
        recorder.release();
        recorder=null;
        grabando=false;
        inicializarPlayer(listener);
    }

    public void reproducir() {
        if (player.isPlaying()) {
            player.pause();
        }
        else {
            player.start();
        }
    }
    public boolean estaReproduciendo() {
        return player.isPlaying();
    }

    public boolean isGrabando() {
        return grabando;
    }
    public String  getRutaArchivo() {
        try {
            return archivo.getAbsolutePath();
        }catch (NullPointerException e) {
            return null;
        }
    }


    public  void inicializarPlayer(MediaPlayer.OnCompletionListener listener) {

        player = new MediaPlayer();
        player.setOnCompletionListener(listener);
        try {
            player.setDataSource(archivo.getAbsolutePath());
        } catch (IOException e) {
        }

        player.prepareAsync();
    }
    public  void detenerReproductor(){

        try {
            if (grabando) {
                recorder.stop();
                recorder.release();
                recorder=null;
            }
            else if (player.isPlaying())
                player.stop();
            player.release();
            player=null;
            archivo = null;
        }
        catch (NullPointerException e) {

        }
    }
    public  void detener(MediaPlayer.OnCompletionListener listener) {
        try {
            player.pause();
            player.release();
            player = null;
            inicializarPlayer(listener);
        }
        catch (NullPointerException e) {}
    }

    public  void liberarRecursos(){
        try {
            if (player.isPlaying())
                player.pause();
            player.release();
            player=null;
            if (grabando)
                recorder.stop();
            recorder.release();
            recorder=null;
        }
        catch (NullPointerException e){}
    }
}

