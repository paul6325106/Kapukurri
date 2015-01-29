package com.example.kapukurri.data;

/**
 * Created by Paul on 30/01/2015.
 */
public class Picture {
    private final String filePath;
    private final int storyId;

    public Picture(String filePath, int storyId) {
        this.filePath = filePath;
        this.storyId = storyId;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getStoryId() {
        return storyId;
    }

}
