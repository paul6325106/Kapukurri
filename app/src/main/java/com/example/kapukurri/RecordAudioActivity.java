package com.example.kapukurri;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.kapukurri.data.Audio;
import com.example.kapukurri.data.DatabaseHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RecordAudioActivity extends ActionBarActivity {

    private static MediaRecorder mediaRecorder;

    private static ImageButton recordButton;

    private boolean isRecording = false;

    protected boolean hasMicrophone() {
        PackageManager pmanager = this.getPackageManager();
        return pmanager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    @Override
    protected void onStart() {
        super.onStart();

        recordButton = (ImageButton) findViewById(R.id.button_record_audio);

        if (!hasMicrophone()) {
            recordButton.setEnabled(false);
        }
    }

    protected static String getAudioFilepath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                + (new SimpleDateFormat("yyyy-MM-dd_hh-mm-ss").format(new Date()))
                + ".3gp";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    //recordButton.setImageDrawable();
                    stopRecording();
                } else {
                    //recordButton.setImageDrawable();
                    startRecording();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_record_audio, menu);
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
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            startActivity(new Intent(this, FamilyTreeActivity.class));
        }
    }

    private void startRecording () {
        if (isRecording) stopRecording();

        isRecording = true;

        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            String filepath = getAudioFilepath();
            mediaRecorder.setOutputFile(filepath);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();

            DatabaseHandler dbh = DatabaseHandler.getInstance(this);
            int storyId = dbh.addStory(0); //XXX using 0 as default personId for now
            dbh.addAudio(new Audio(filepath, storyId));

        } catch (IOException e) {
            e.printStackTrace();
            //TODO indicate malfunction to user
        }
    }

    private void stopRecording() {
        if (isRecording) {
            mediaRecorder.stop();
            //mediaRecorder.reset();
            mediaRecorder.release();
            mediaRecorder = null;
            isRecording = false;
        }
    }


}
