package com.example.kapukurri;

import android.os.Environment;

/**
 * Created by tp271429 on 29/01/2015.
 */
public class ExternalStorage {

    /**
     * Checks if external storage is available for read and write.
     * Stolen shamelessly from developer.android.com
     */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Checks if external storage is available to at least read.
     * Stolen shamelessly from developer.android.com
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

}
