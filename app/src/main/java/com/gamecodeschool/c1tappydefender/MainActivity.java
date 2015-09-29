package com.gamecodeschool.c1tappydefender;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;
    private int intro = -1;
    private boolean loaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPlay = (Button) findViewById(R.id.playButton);
        buttonPlay.setOnClickListener(this);

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        prefs = getSharedPreferences("highScore", MODE_PRIVATE);

        TextView textFastestTime = (TextView) findViewById(R.id.textHighScore);
        long fastestTime = prefs.getLong("fastestTime", 0);
        textFastestTime.setText("Fastest Time : " + fastestTime);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                Log.d("SOUND", "intro.ogg was loaded and is ready for use");
                loaded = true;
                //soundPool.play(intro, 1, 1, 0, -1, 1);
            }
        });

        AssetManager assetManager = this.getAssets();
        AssetFileDescriptor descriptor;

        try {
            descriptor = assetManager.openFd("intro.ogg");
            intro = soundPool.load(descriptor, 0);
        } catch (IOException e) {
            Log.d("Game IOException", "failed on load intro.ogg file");
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);



    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        //finish();
        if(loaded) {
            soundPool.stop(intro);
        }
    }
}
