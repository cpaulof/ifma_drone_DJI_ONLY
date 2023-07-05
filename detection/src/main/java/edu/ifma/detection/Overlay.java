package edu.ifma.detection;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BlendMode;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.widget.ImageView;

public class Overlay {
    private ImageView img;
    private Bitmap bitmap;
    private Paint paint;
    private Canvas canvas;
    public Overlay(ImageView img){
        this.img = img;
        Log.v("[OVERLAY]", "settingup overlay");
        bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas = new Canvas(bitmap);
    }
    public void drawPositive(float conf){
        drawText("POSITIVE ("+(int)conf*100+"%)", Color.GREEN, 60f, 30, 75);
    }
    public void drawNegative(float conf){
        drawText("NEGATIVE ("+(int)conf*100+"%)", Color.RED, 60f, 30, 75);
    }
    private void drawText(String text, int color, float size, float x, float y){
        clear();
        int oldColor = paint.getColor();
        float oldSize = paint.getTextSize();

        paint.setColor(color);
        paint.setTextSize(size);
        canvas.drawText(text, x, y, paint);

        paint.setTextSize(oldSize);
        paint.setColor(oldColor);
    }

    private void clear(){
        canvas.drawColor(Color.TRANSPARENT, BlendMode.CLEAR);
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void updateView(Activity app){
        app.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                img.setImageBitmap(bitmap);
            }
        });
    }
}
