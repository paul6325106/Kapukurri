package com.example.kapukurri.data;

/**
 * Created by tp271429 on 30/01/2015.
 */
public class Tag {
    private final String word;
    private final int storyId;

    public Tag(String word, int storyId) {
        this.word = word;
        this.storyId = storyId;
    }

    public String getWord() {
        return word;
    }

    public int getStoryId() {
        return storyId;
    }
}
