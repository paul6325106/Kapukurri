package com.example.kapukurri.data;

/**
 * Created by Paul on 29/01/2015.
 */
public class Story {
    private final int storyId;
    private final int personId;

    public Story(int storyId, int personId) {
        this.storyId = storyId;
        this.personId = personId;
    }

    public int getStoryId() {
        return storyId;
    }

    public int getPersonId() {
        return personId;
    }
}
