package com.gamecodeschool.c1tappydefender;


import android.content.Context;

import java.util.Random;

public class SpaceDust {

    private int x, y;
    private int speed;

    private int maxX, maxY, minX, minY;

    public SpaceDust(Context context, int screenX, int screenY){
        maxX = screenX;
        maxY = screenY;

        minX = 0;
        minY = 0;

        //set a speed between 0 and 9
        Random generator = new Random();
        speed = generator.nextInt(10);

        //set the starting coordinates
        x = generator.nextInt(maxX);
        y = generator.nextInt(maxY);


    }


    public void update(int playerSpeed){
        x -= playerSpeed;
        x -= speed;

        if( x < 0) {
            x = maxX;
            Random generator = new Random();
            y = generator.nextInt(maxY);// 0 to device height
            speed = generator.nextInt(15);// 0 to 14
        }
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

    public int getMaxX() {
        return maxX;
    }

    public void setMaxX(int maxX) {
        this.maxX = maxX;
    }

    public int getMaxY() {
        return maxY;
    }

    public void setMaxY(int maxY) {
        this.maxY = maxY;
    }

    public int getMinX() {
        return minX;
    }

    public void setMinX(int minX) {
        this.minX = minX;
    }

    public int getMinY() {
        return minY;
    }

    public void setMinY(int minY) {
        this.minY = minY;
    }
}
