package com.gamecodeschool.c1tappydefender;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SoundPool soundPool;
    private int intro = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonPlay = (Button) findViewById(R.id.playButton);
        buttonPlay.setOnClickListener(this);

        AssetManager assetManager = this.getAssets();
        AssetFileDescriptor descriptor;

        try {
            descriptor = assetManager.openFd("intro.ogg");
            intro = soundPool.load(descriptor, 0);
            soundPool.play(intro, 1,1,0, -1, 1);
        } catch (IOException e) {
            Log.d("Game IOException", "failed on load intro.ogg file");
        }
    }


    @Override
    public void onClick(View view) {
        Intent i = new Intent(this, GameActivity.class);
        startActivity(i);
        //finish();
        soundPool.stop(intro);
    }
}
