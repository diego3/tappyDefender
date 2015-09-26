package com.gamecodeschool.c1tappydefender;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;

public class TDView extends SurfaceView implements Runnable {
    /**
     * Used to control when the user is playing the game or not
     */
    public volatile boolean playing = true;
    /**
     * Used to control process of this class to another thread
     */
    public Thread gameThread = null;

    //characters to the game
    protected PlayerShip player = null;
    protected EnemyShip enemy1 = null;
    protected EnemyShip enemy2 = null;
    protected EnemyShip enemy3 = null;

    protected Paint paint;
    protected Canvas canvas;
    protected SurfaceHolder ourHolder;

    public ArrayList<SpaceDust> dustList = new ArrayList<SpaceDust>();

    protected int screenX;
    protected int screenY;

    private float distanceRemaining;
    /**
     * How log has the player been flying
     */
    private long timeTaken;
    private long timeStarted;
    private long fastestTime;

    private Context context;

    private boolean gameEnded;

    /**
     * The Context parameter that is passed into our constructor is a reference to the
     * current state of our application within the Android system that is held by our
     * GameActivity class.
     *
     * @param context
     * @param x     The max with   screen resolution
     * @param y     The max height screen resolution
     */
    public TDView(Context context, int x, int y) {
        super(context);
        this.context = context;

        screenX = x;
        screenY = y;

        ourHolder = getHolder();
        paint = new Paint();

        startGame();
    }

    private void startGame() {
        player = new PlayerShip(context, screenX, screenY);
        enemy1 = new EnemyShip(context, screenX, screenY);
        enemy2 = new EnemyShip(context, screenX, screenY);
        enemy3 = new EnemyShip(context, screenX, screenY);

        int numSpecs = 40;
        for (int i=0; i < numSpecs; i++) {
            SpaceDust spec = new SpaceDust(context, screenX, screenY);
            dustList.add( spec );
        }

        //Reset distance and time
        distanceRemaining = 10000; // 10KM
        timeTaken = 0;

        //Get start time
        timeStarted = System.currentTimeMillis();

        gameEnded = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_UP:
                player.stopBoosting();

                break;
            case MotionEvent.ACTION_DOWN:
                player.setBoosting();
                if(gameEnded){
                    startGame();
                }
                break;
        }
        return true;
    }

    @Override
    public void run() {
        while(playing){
            //Log.d("game loop", "playing so much heim kkk");
            update();
            draw();
            control();
        }
    }

    private void update() {
        player.update();
        enemy1.update(player.getSpeed());
        enemy2.update(player.getSpeed());
        enemy3.update(player.getSpeed());

        for(SpaceDust dust : dustList){
           dust.update(player.getSpeed());
        }

        // Collision detection on new positions
        // Before move because we are testing last frames
        // position which has just been drawn
        boolean hitDetected = false;
        if(Rect.intersects(player.getHitBox(), enemy1.getHitBox())){
            hitDetected = true;
            enemy1.setX(-150);
        }
        if(Rect.intersects(player.getHitBox(), enemy2.getHitBox())){
            hitDetected = true;
            enemy2.setX(-150);
        }
        if(Rect.intersects(player.getHitBox(), enemy3.getHitBox())){
            hitDetected = true;
            enemy3.setX(-150);
        }

        if(hitDetected) {
            player.reduceShieldStrength();
            if(player.getShieldStrength() < 0){
                //game over or something
                gameEnded = true;
            }
        }

        if(!gameEnded) {
            //subtract distance to home planet based on the current speed
            distanceRemaining -= player.getSpeed();
            //How long has the player been flying
            timeTaken = System.currentTimeMillis() - timeStarted;
        }

        if(distanceRemaining < 0){
            //update the fastestTime
            if(timeTaken < fastestTime){
                fastestTime = timeTaken;
            }
            //avoid negative numbers
            distanceRemaining = 0;

            //end of game
            gameEnded = true;
        }
    }

    private void control() {
        try {
            gameThread.sleep(17);//(1000(milliseconds)/60(FPS))
        } catch (InterruptedException e) {
            Log.e("control", "InterruptedException in TdView control method");
        }
    }

    private void draw() {
        if(ourHolder.getSurface().isValid()) {
            canvas = ourHolder.lockCanvas();

            //Rub out the last frame
            canvas.drawColor(Color.argb(255, 0,0,0));

            //change the color to show the stars
            paint.setColor(Color.argb(255, 255, 255, 255));
            for(SpaceDust sd : dustList){
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            //show all game players
            canvas.drawBitmap(player.getBitmap(), player.getSpeed(), player.getY(), paint);
            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(enemy3.getBitmap(), enemy3.getX(), enemy3.getY(), paint);

            if(!gameEnded) {
                //Draw the HUD
                paint.setTextAlign(Paint.Align.LEFT);
                paint.setColor(Color.argb(255, 255, 255, 255));
                paint.setTextSize(25);
                canvas.drawText("Fastest:" + fastestTime + "s", 10, 20, paint);
                canvas.drawText("TimeTaken:" + timeTaken + "s", screenX / 2, 20, paint);
                canvas.drawText("Distance:" + (distanceRemaining / 1000) + "Km", screenX / 3, screenY - 20, paint);
                canvas.drawText("Shield:" + player.getShieldStrength(), 10, screenY - 20, paint);
                canvas.drawText("Speed:" + player.getSpeed() * 60 + "MPS", (screenX / 3) * 2, screenY - 20, paint);
            }
            else {
                //this happens when the game is ended
                paint.setTextSize(80);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText("Game Over", screenX / 2, 100, paint);

                paint.setTextSize(25);
                canvas.drawText("Fastest Time: "+fastestTime+"s", screenX /2, 160, paint);
                canvas.drawText("Distance Remaining: "+ distanceRemaining/1000 + "KM", screenX / 2, 200, paint);

                paint.setTextSize(80);
                canvas.drawText("Tap to REPLAY", screenX / 2, 250, paint);

            }
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    public void pause() {
        playing = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {

        }
    }

    public void resume(){
        playing = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

}
