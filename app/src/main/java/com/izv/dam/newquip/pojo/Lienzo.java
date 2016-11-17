package com.izv.dam.newquip.pojo;

/**
 * Created by alumno on 09/11/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Miguel Ángel on 21/10/2016.
 */

public class Lienzo extends View {

    private Path drawPath;
    private static Paint drawPaint;
    private Paint canvasPaint;
    private int paintColor = Color.parseColor("#000000");;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    //private float TamanyoPunto;
    private String colorString; // miguel, no he tenido cojones a hacerlo de otra manera, si encuentras otra manera menos chapuza cambialo xD


    public Lienzo(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath,drawPaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX,touchY);
                break;
            case MotionEvent.ACTION_UP:
                drawPath.lineTo(touchX,touchY);
                drawCanvas.drawPath(drawPath, drawPaint);
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return true;

    }

    public void setColor(String newColor){
        invalidate();
        colorString=newColor;
        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public String getColor(){
        return colorString;
    }

    public  void setTamanyoPunto(float nuevoTamanyo){
        drawPaint.setStrokeWidth(nuevoTamanyo);
    }

    public void NuevoDibujo(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public  float getTamanyoPunto() {
        return drawPaint.getStrokeWidth();
    }


}

