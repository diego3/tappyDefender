package com.gamecodeschool.c1tappydefender;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

import java.util.Random;

public class EnemyShip {

    private Bitmap bitmap;
    private int x, y;
    private int speed = 1;

    private int minX;
    private int maxX;

    private int minY;
    private int maxY;

    private Rect hitBox;

    public EnemyShip(Context context, int screenX, int screenY){
        Random generator = new Random();
        int which = generator.nextInt(3);
        switch (which) {
            case 0:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
                break;
            case 1:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy2);
                break;
            case 2:
                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy3);
                break;
        }


        maxX = screenX;
        maxY = screenY;

        minX = 0;
        minY = 0;

        speed = generator.nextInt(6) + 7;

        x = screenX;
        y = generator.nextInt(maxY) - bitmap.getHeight();

        hitBox = new Rect(x, y, bitmap.getWidth(), bitmap.getHeight());

        scaleBitmap(screenX);
    }


    public void update(int playerSpeed) {
        //move to the left
        x -= (playerSpeed / 3);
        x -= speed;

        //respawn when off screen
        if(x < minX - bitmap.getWidth()){
            Random generator = new Random();
            speed = generator.nextInt(10) + 10;

            x = maxX;
            y = generator.nextInt(maxY) - bitmap.getHeight();
        }

        hitBox.left   = x;
        hitBox.top    = y;
        hitBox.right  = x + bitmap.getWidth();
        hitBox.bottom = y + bitmap.getHeight();
    }


    public void scaleBitmap(int screenX) {
        if(screenX < 1000 ){
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 3,  bitmap.getHeight() /3, false);
        }
        else if(screenX < 1200) {
            bitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 2,  bitmap.getHeight() /2, false);
        }
    }

    public Rect getHitBox() {
        return hitBox;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }
}
