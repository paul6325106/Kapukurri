package com.example.kapukurri;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.kapukurri.data.Audio;
import com.example.kapukurri.data.DatabaseHandler;
import com.example.kapukurri.data.Story;

import java.io.IOException;
import java.util.List;


public class StoriesActivity extends ActionBarActivity {

    private static MediaPlayer mediaPlayer;

    private ImageButton playButton;

    private boolean isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stories);

        playButton = (ImageButton) findViewById(R.id.button_play_audio);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPlaying) {
                    stopPlaying();
                } else {
                    startPlaying(getFirstAudioFilepath());
                }
            }
        });
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

    /**
     * No time to actually load the data into a layout properly.
     * Just a wireframe and a button for demonstration purposes.
     * To this end, this method retrieves the first available audio filepath.
     */
    private String getFirstAudioFilepath() {
        DatabaseHandler dbh = DatabaseHandler.getInstance(this);
        List<Story> stories = dbh.getAllStories(0); //XXX using id 0 as placeholder, as in RecordAudioActivity
        int storyId = stories.get(0).getStoryId();
        List<Audio> audios = dbh.getAllAudio(storyId);
        Log.d("WUBWUBWUB", "Audio filepath: " + audios.get(0).getFilePath());
        return audios.get(0).getFilePath();
    }

    private void stopPlaying() {
        if (isPlaying) {
            mediaPlayer.stop();
            //mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
            isPlaying = false;
        }
    }
}
