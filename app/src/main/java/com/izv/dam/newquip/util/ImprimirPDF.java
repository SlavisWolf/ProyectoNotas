package com.izv.dam.newquip.util;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.izv.dam.newquip.R;
import com.izv.dam.newquip.pojo.ItemNotaLista;
import com.izv.dam.newquip.pojo.Nota;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ImprimirPDF {

    private static Nota nota;
    private static List<ItemNotaLista> lista;


    public static void imprimirNota(Context c, Nota nota) {

        ImprimirPDF.nota = nota;

        Document documento = new Document(PageSize.LETTER);
        String nombreArchivo = UtilFecha.fechaActual()+".pdf";


        if (!(ImprimirPDF.nota.getTitulo().isEmpty())){
            nombreArchivo = ImprimirPDF.nota.getTitulo()+".pdf";
        }

        DirectoriosArchivosQuip.comprobarDirectorioPDF();
        final String nombre_completo = Environment.getExternalStorageDirectory() + File.separator +DirectoriosArchivosQuip.PDF_DIRECTORY + File.separator + nombreArchivo;



        File outPutFile = new File(nombre_completo);

        if(outPutFile.exists()){
            outPutFile.delete();
        }

        new AsyncTask<Object, Void, Context>(){

            @Override
            protected Context doInBackground(Object... params) {

                Context c               = (Context)params[0];
                Document documento      = (Document)params[1];
                String nombre_completo  = (String)params[2];

                try {

                    PdfWriter pdf = PdfWriter.getInstance(documento, new FileOutputStream(nombre_completo));

                    documento.open();
                    documento.addAuthor("Quip");
                    documento.addCreator("Quip");
                    documento.addSubject("Pdf nota");
                    documento.addCreationDate();
                    documento.addTitle("Nota");
                    XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

                    String htmlPDF = "";

                    if(ImprimirPDF.nota.getImagen() != null){
                        htmlPDF = "<html>" +
                                "   <head></head>" +
                                "   <body>" +
                                "       <p><h1>"+ImprimirPDF.nota.getTitulo()+"</h1></p>" +
                                "       <p>"+ImprimirPDF.nota.getNota()+"</p>" +
                                "       <p><img src='"+ImprimirPDF.nota.getImagen()+"' height='300' width='300'/></p>" +
                                "   </body>" +
                                "</html>";
                    }
                    else{
                        htmlPDF = "<html><head></head><body><h1>"+ImprimirPDF.nota.getTitulo()+"</h1> <p>"+ImprimirPDF.nota.getNota()+"</p><p></p></body></html>";

                    }
                    try {
                        worker.parseXHtml(pdf, documento, new StringReader(htmlPDF));
                        documento.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                return c;
            }

            @Override
            protected void onPostExecute(Context c) {
                crearNotificacion(c, nombre_completo);
            }
        }.execute( c, documento, nombre_completo);


    }

    public static void imprimirLista(Context c, Nota nota, final List<ItemNotaLista> lista) {

        ImprimirPDF.nota = nota;
        ImprimirPDF.lista = lista;

        Document documento = new Document(PageSize.LETTER);

        String nombreArchivo = UtilFecha.fechaActual()+".pdf";

        if (!(ImprimirPDF.nota.getTitulo().isEmpty())){
            nombreArchivo = ImprimirPDF.nota.getTitulo()+".pdf";
        }

        DirectoriosArchivosQuip.comprobarDirectorioPDF();
        final String nombre_completo = Environment.getExternalStorageDirectory() + File.separator +DirectoriosArchivosQuip.PDF_DIRECTORY + File.separator + nombreArchivo;


        File outPutFile = new File(nombre_completo);

        if(outPutFile.exists()){
            outPutFile.delete();
        }


        new AsyncTask<Object, Void, Context>(){

            @Override
            protected Context doInBackground(Object... params) {

                Context c               = (Context)params[0];
                Document documento      = (Document)params[1];
                String nombre_completo  = (String)params[2];

                try {

                    PdfWriter pdf = PdfWriter.getInstance(documento, new FileOutputStream(nombre_completo));

                    documento.open();
                    documento.addAuthor("Quip");
                    documento.addCreator("Quip");
                    documento.addSubject("Pdf lista");
                    documento.addCreationDate();
                    documento.addTitle("Lista");

                    //image asset
                    //Image imagen = Image.getInstance("android_asset/bot_b.png");

                    Bitmap bmp = BitmapFactory.decodeResource(c.getResources(), R.drawable.checkon);
                    //bmp = Bitmap.createScaledBitmap( bmp, (int) (0.1* bmp.getWidth()), (int) (0.1* bmp.getHeight()), true);
                    String rutaon = DirectoriosArchivosQuip.crearImagenAlmacenamientoInterno(c, bmp, "checkBoxOn.png");

                    Bitmap bmp1 = BitmapFactory.decodeResource(c.getResources(), R.drawable.checkoff);
                    String rutaoff = DirectoriosArchivosQuip.crearImagenAlmacenamientoInterno(c, bmp1, "checkBoxOff.png");

                    Log.v("bitmap", bmp+"");

                    XMLWorkerHelper worker = XMLWorkerHelper.getInstance();

                    String htmlPDF = "";

                    if (ImprimirPDF.nota.getTitulo() == null || ImprimirPDF.nota.getTitulo().isEmpty() ){
                        htmlPDF += "<html><head></head><body><h1>Sin titulo</h1> ";
                    }
                    else{
                        htmlPDF += "<html><head></head><body><h1>"+ImprimirPDF.nota.getTitulo()+"</h1> ";
                    }

                    for (ItemNotaLista item : ImprimirPDF.lista){
                        if (item.isMarcado()) {
                            htmlPDF += "<img src='"+rutaon+"' />&nbsp;&nbsp;&nbsp;"+item.getTexto() + "<br /><br />";
                        }
                        else{

                            htmlPDF += "<img src='"+rutaoff+"' />&nbsp;&nbsp;&nbsp;"+item.getTexto() + "<br /><br />";
                        }
                    }

                    htmlPDF += "</body></html>";
                    System.out.println(htmlPDF);
                    try {
                        worker.parseXHtml(pdf, documento, new StringReader(htmlPDF));
                        documento.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (DocumentException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } /*catch (IOException e) {
                    e.printStackTrace();
                }*/

                return c;
            }

            @Override
            protected void onPostExecute(Context c) {

                crearNotificacion(c, nombre_completo);
            }
        }.execute( c, documento, nombre_completo);


    }

    private static void crearNotificacion(Context c, String n){

        Intent intent = mostrarPDF(n,c);
        PendingIntent pIntent = PendingIntent.getActivity(c, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(c);
        Toast.makeText(c, "Pdf generado correctamente", Toast.LENGTH_SHORT).show();

        notificacion.setContentTitle(ImprimirPDF.nota.getTitulo()+".pdf");
        if (ImprimirPDF.nota.getTitulo().isEmpty() ){
            notificacion.setContentTitle(UtilFecha.fechaActual()+".pdf");
        }
        notificacion.setSmallIcon(R.drawable.ic_imprimir_pdf);
        notificacion.setAutoCancel(true);
        notificacion.setContentText("Creado correctamente");
        notificacion.setContentIntent(pIntent).build();

        NotificationManager manager = (NotificationManager) c.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(1, notificacion.build());

    }
    private static Intent mostrarPDF(String archivo, Context c){
        File file = new File(archivo);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        try{
            return intent;
        }catch (ActivityNotFoundException e){
            Toast.makeText(c, "No tiene una app para abrir este archivo", Toast.LENGTH_LONG).show();
        }

        return null;
    }
}