package com.gamecodeschool.c1tappydefender;

import android.graphics.Bitmap;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class PlayerShip {

    protected Bitmap bitmap;
    protected int x, y;
    protected int speed;
    protected boolean boosting;

    private final int GRAVITY = -8;
    //Stop ship leaving the screen
    private int maxY;
    private int minY;
    //limits the bounds of the ship's speed
    private final int MIN_SPEED = 1;
    private final int MAX_SPEED = 12;

    private Rect hitBox;

    private int shieldStrength;


    public PlayerShip(Context context, int screenX, int screenY) {
        shieldStrength = 2;

        x = 50;
        y = 50;
        speed = 1;
        boosting = false;
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ship);
        this.minY = 0;
        this.maxY = screenY - bitmap.getHeight();

        hitBox = new Rect(x,y,bitmap.getWidth(), bitmap.getHeight());
        scaleBitmap(screenX);
    }

    public void update(){
        //System.out.println("speed = " + speed);
        if(boosting){
            speed += 2;
        }
        else {
            speed -= 5;
        }

        //constraint the ship speed
        if(speed > MAX_SPEED) {
            speed = MAX_SPEED;
        }
        //the ship will never stop completely
        if(speed < MIN_SPEED) {
            speed = MIN_SPEED;
        }

        //move the ship up or down
        y -= speed + GRAVITY;

        //but don't let the ship stray off screen
        if( y < minY) {
            y = minY;
        }
        if( y > maxY) {
            y = maxY;
        }

        //Log.d("where are the player", "x = " + x + " - y = " + y + " - speed = " + speed);

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

    public Rect getHitBox(){
        return hitBox;
    }

    public boolean isBoosting() {
        return boosting;
    }

    /**
     * Set boosting to TRUE
     */
    public void setBoosting() {
        this.boosting = true;
    }

    /**
     * Disable boosting
     */
    public void stopBoosting() {
        this.boosting = false;
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

    public void setShieldStrength(int shield){
        this.shieldStrength = shield;
    }

    public int getShieldStrength() {
        return shieldStrength;
    }
}
