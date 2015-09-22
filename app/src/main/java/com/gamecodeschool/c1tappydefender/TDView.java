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

    /**
     * The Context parameter that is passed into our constructor is a reference to the
     * current state of our application within the Android system that is held by our
     * GameActivity class.
     *
     * @param context
     * @param x
     * @param y
     */
    public TDView(Context context, int x, int y) {
        super(context);

        ourHolder = getHolder();
        paint = new Paint();

        player = new PlayerShip(context, x, y);
        enemy1 = new EnemyShip(context, x, y);
        enemy2 = new EnemyShip(context, x, y);
        enemy3 = new EnemyShip(context, x, y);

        int numSpecs = 40;
        for (int i=0; i < numSpecs; i++) {
            SpaceDust spec = new SpaceDust(context, x, y);
            dustList.add( spec );
        }

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

            // Collision detection on new positions
            // Before move because we are testing last frames
            // position which has just been drawn
            if(Rect.intersects(player.getHitBox(), enemy1.getHitBox())){
                enemy1.setX(-150);
            }
            if(Rect.intersects(player.getHitBox(), enemy2.getHitBox())){
                enemy2.setX(-150);
            }
            if(Rect.intersects(player.getHitBox(), enemy3.getHitBox())){
                enemy3.setX(-150);
            }

            canvas.drawColor(Color.argb(255,255,255,255));
            for(SpaceDust sd : dustList){
                canvas.drawPoint(sd.getX(), sd.getY(), paint);
            }

            //Rub out the last frame
            canvas.drawColor(Color.argb(255, 0,0,0));

            canvas.drawBitmap(player.getBitmap(), player.getSpeed(), player.getY(), paint);

            canvas.drawBitmap(enemy1.getBitmap(), enemy1.getX(), enemy1.getY(), paint);
            canvas.drawBitmap(enemy2.getBitmap(), enemy2.getX(), enemy2.getY(), paint);
            canvas.drawBitmap(enemy3.getBitmap(), enemy3.getX(), enemy3.getY(), paint);

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
