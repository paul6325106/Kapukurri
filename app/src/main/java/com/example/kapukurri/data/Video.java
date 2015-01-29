package com.example.kapukurri.data;

/**
 * Created by Paul on 30/01/2015.
 */
public class Video {
    private final String filePath;
    private final int storyId;

    public Video(String filePath, int storyId) {
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
