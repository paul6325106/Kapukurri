package com.example.kapukurri;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.IOException;


public class StoriesActivity extends ActionBarActivity {

    private static MediaPlayer mediaPlayer;

//    private static Button stopButton;
//    private static Button playButton;

    private boolean isPlaying = false;

    @Override
    protected void onStart() {
        super.onStart();

//        playButton = (Button) findViewById(R.id.playButton);
//        stopButton = (Button) findViewById(R.id.stopButton);

//        playButton.setEnabled(true);
//        stopButton.setEnabled(false);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            startActivity(new Intent(this, RecordAudioActivity.class));
        }
    }

    private void startPlaying(String filePath) {
//        playButton.setEnabled(false);
//        stopButton.setEnabled(true);

        if (isPlaying) stopPlaying();

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isPlaying = true;
        } catch (IOException e) {
            e.printStackTrace();
            //TODO indicate malfunction to user
        }
    }

    private void stopPlaying() {
//        stopButton.setEnabled(false);
//        playButton.setEnabled(true);

        if (isPlaying) {
            mediaPlayer.stop();
            //mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }
}
