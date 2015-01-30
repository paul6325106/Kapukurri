package com.example.kapukurri.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 29/01/2015.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "storyDatabase";

    private static final String TABLE_PEOPLE = "people";
    private static final String TABLE_STORIES = "stories";
    private static final String TABLE_AUDIO = "audio";
    private static final String TABLE_PICTURES = "pictures";
    private static final String TABLE_VIDEOS = "videos";

    private static final String KEY_PERSON_ID = "personid";
    private static final String KEY_PERSON_NAME = "name";
    private static final String KEY_PERSON_RELATIONSHIP = "relationship";
    private static final String KEY_PERSON_ENABLED = "enabled";
    private static final String KEY_STORY_ID = "storyid";
    private static final String KEY_AUDIO_FILEPATH = "filepath";
    private static final String KEY_PICTURE_FILEPATH = "filepath";
    private static final String KEY_VIDEO_FILEPATH = "filepath";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_PEOPLE_TABLE = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT," +
                        "%s TEXT," +
                        "%s INTEGER" +
                        ")",
                TABLE_PEOPLE,
                KEY_PERSON_ID,
                KEY_PERSON_NAME,
                KEY_PERSON_RELATIONSHIP,
                KEY_PERSON_ENABLED
        );
        db.execSQL(CREATE_PEOPLE_TABLE);

        String CREATE_STORIES_TABLE = String.format(
                "CREATE TABLE %s(" +
                        "%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s INTEGER" +
                        ")",
                TABLE_STORIES,
                KEY_STORY_ID,
                KEY_PERSON_ID
        );
        db.execSQL(CREATE_STORIES_TABLE);

        String CREATE_AUDIO_TABLE = String.format(
                "CREATE TABLE %s(" +
                        "%s TEXT PRIMARY KEY," +
                        "%s INTEGER" +
                        ")",
                TABLE_AUDIO,
                KEY_AUDIO_FILEPATH,
                KEY_STORY_ID
        );
        db.execSQL(CREATE_AUDIO_TABLE);

        String CREATE_PICTURES_TABLE = String.format(
                "CREATE TABLE %s(" +
                        "%s TEXT PRIMARY KEY," +
                        "%s INTEGER" +
                        ")",
                TABLE_PICTURES,
                KEY_PICTURE_FILEPATH,
                KEY_STORY_ID
        );
        db.execSQL(CREATE_PICTURES_TABLE);

        String CREATE_VIDEO_TABLE = String.format(
                "CREATE TABLE %s(" +
                        "%s TEXT PRIMARY KEY," +
                        "%s INTEGER" +
                        ")",
                TABLE_VIDEOS,
                KEY_VIDEO_FILEPATH,
                KEY_STORY_ID
        );
        db.execSQL(CREATE_VIDEO_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_AUDIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PICTURES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEOS);
        onCreate(db);
    }

    /**
     * Add a new person to the database.
     * @param name name of the person
     * @param relationship relationship/title of the person
     * @param isEnabled 1 if enabled, 0 if disabled
     *                  (deceased people may need to be disabled)
     * @return personId of added person
     */
    public int addPerson(String name, String relationship, boolean isEnabled) {
        SQLiteDatabase dbw = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PERSON_NAME, name);
        if (!relationship.isEmpty())
            values.put(KEY_PERSON_RELATIONSHIP, relationship);
        values.put(KEY_PERSON_ENABLED, (isEnabled) ? 1 : 0);

        dbw.insert(TABLE_PEOPLE, null, values);
        dbw.close();

        //after inserting the new entry, we retrieve the autoid

        String selectQuery = String.format(
                "SELECT %s FROM %s order by %s DESC limit 1",
                KEY_PERSON_ID, TABLE_PEOPLE, KEY_PERSON_ID
        );

        SQLiteDatabase dbr = this.getReadableDatabase();
        Cursor cursor = dbr.rawQuery(selectQuery, null);

        if (cursor != null) cursor.moveToFirst();

        return Integer.parseInt(cursor.getString(0));
    }

    /**
     * Adds a story to the database.
     * @param personId person to who the stories corresponds
     * @return storyId of added story
     */
    public int addStory(int personId) {
        SQLiteDatabase dbw = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PERSON_ID, personId);

        dbw.insert(TABLE_STORIES, null, values);
        dbw.close();

        //after inserting the new entry, we retrieve the autoid

        String selectQuery = String.format(
                "SELECT %s FROM %s order by %s DESC limit 1",
                KEY_STORY_ID, TABLE_STORIES, KEY_STORY_ID
        );

        SQLiteDatabase dbr = this.getReadableDatabase();
        Cursor cursor = dbr.rawQuery(selectQuery, null);

        if (cursor != null) cursor.moveToFirst();

        return Integer.parseInt(cursor.getString(0));

    }

    /**
     * Adds a piece of audio media to the database.
     * @param audio the audio to be added
     */
    public void addAudio(Audio audio) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_ID, audio.getStoryId());
        values.put(KEY_AUDIO_FILEPATH, audio.getFilePath());

        db.insert(TABLE_AUDIO, null, values);
        db.close();
    }

    /**
     * Adds a piece of picture media to the database.
     * @param picture the picture to be added
     */
    public void addPicture(Picture picture) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_ID, picture.getStoryId());
        values.put(KEY_PICTURE_FILEPATH, picture.getFilePath());

        db.insert(TABLE_PICTURES, null, values);
        db.close();
    }

    /**
     * Adds a piece of audio media to the database.
     * @param video the video to be added
     */
    public void addVideo(Video video) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STORY_ID, video.getStoryId());
        values.put(KEY_VIDEO_FILEPATH, video.getFilePath());

        db.insert(TABLE_VIDEOS, null, values);
        db.close();
    }

    /**
     * Gets a person from the database
     * @param personId the person's autoid
     * @return the Person
     */
    public Person getPerson(int personId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PEOPLE,
                new String[] {
                        KEY_PERSON_NAME,
                        KEY_PERSON_RELATIONSHIP,
                        KEY_PERSON_ENABLED
                },
                KEY_PERSON_ID + "=?",
                new String[] { String.valueOf(personId) },
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new Person(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)) == 1
        );
    }

    /**
     * Disables a deceased person.
     * @param personId the person to disable
     */
    public void disablePerson(int personId) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PERSON_ENABLED, String.valueOf(0));

        db.update(
                TABLE_PEOPLE,
                values,
                KEY_PERSON_ID + " = ?",
                new String[] { String.valueOf(personId) }
        );
    }

    /**
     * Retrieves all enabled people from the database.
     * @return an arraylist of enabled people
     */
    public List<Person> getAllEnabledPeople() {
        List<Person> personList = new ArrayList<Person>();

        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_PEOPLE, KEY_PERSON_ENABLED, 1
        );

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Person person = new Person(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        cursor.getString(2),
                        Integer.parseInt(cursor.getString(3)) == 1
                );
                personList.add(person);
            } while (cursor.moveToNext());
        }

        return personList;
    }

    /**
     * Gets a story from the database.
     * @param storyId the story id
     * @return the story
     */
    public Story getStory(int storyId) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_STORIES,
                new String[] {
                        KEY_STORY_ID,
                        KEY_PERSON_ID
                },
                KEY_STORY_ID + "=?",
                new String[] { String.valueOf(storyId) },
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new Story(
                Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1))
        );
    }

    /**
     * Retrieves all stories for a specific person from the database.
     * @param personId the person's id
     * @return an arraylist of the stories
     */
    public List<Story> getAllStories(int personId) {
        List<Story> storyList = new ArrayList<Story>();

        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_STORIES, KEY_PERSON_ID, personId
        );

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Story story = new Story(
                        Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1))
                );
                storyList.add(story);
            } while (cursor.moveToNext());
        }

        return storyList;
    }

    /**
     * Retrieves the details for a piece of audio.
     * @param audioFilepath the filepath of the audio
     * @return the audio
     */
    public Audio getAudio(String audioFilepath) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AUDIO,
                new String[] {
                        KEY_AUDIO_FILEPATH,
                        KEY_STORY_ID
                },
                KEY_AUDIO_FILEPATH + "=?",
                new String[] { audioFilepath },
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new Audio(
                cursor.getString(0),
                Integer.parseInt(cursor.getString(1))
        );
    }

    /**
     * Retrieves all audio associated with a story
     * @param storyId the story id
     * @return an arraylist of the audio
     */
    public List<Audio> getAllAudio(int storyId) {
        List<Audio> audioList = new ArrayList<Audio>();

        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_AUDIO, KEY_STORY_ID, storyId
        );

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Audio audio = new Audio(
                        cursor.getString(0),
                        Integer.parseInt(cursor.getString(1))
                );
                audioList.add(audio);
            } while (cursor.moveToNext());
        }

        return audioList;
    }

    /**
     * Retrieves the details for a picture.
     * @param pictureFilepath the filepath of the picture
     * @return the picture
     */
    public Picture getPicture(String pictureFilepath) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PICTURES,
                new String[] {
                        KEY_PICTURE_FILEPATH,
                        KEY_STORY_ID
                },
                KEY_PICTURE_FILEPATH + "=?",
                new String[] { pictureFilepath },
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new Picture(
                cursor.getString(0),
                Integer.parseInt(cursor.getString(1))
        );
    }

    /**
     * Retrieves all pictures associated with a story
     * @param storyId the story id
     * @return an arraylist of the pictures
     */
    public List<Picture> getAllPictures(int storyId) {
        List<Picture> pictureList = new ArrayList<Picture>();

        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_PICTURES, KEY_STORY_ID, storyId
        );

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Picture picture = new Picture(
                        cursor.getString(0),
                        Integer.parseInt(cursor.getString(1))
                );
                pictureList.add(picture);
            } while (cursor.moveToNext());
        }

        return pictureList;
    }
    /**
     * Retrieves the details for a video.
     * @param videoFilepath the filepath of the video
     * @return the video
     */
    public Video getVideo(String videoFilepath) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VIDEOS,
                new String[] {
                        KEY_VIDEO_FILEPATH,
                        KEY_STORY_ID
                },
                KEY_VIDEO_FILEPATH + "=?",
                new String[] { videoFilepath },
                null, null, null, null);

        if (cursor != null) cursor.moveToFirst();

        return new Video(
                cursor.getString(0),
                Integer.parseInt(cursor.getString(1))
        );
    }

    /**
     * Retrieves all videos associated with a story
     * @param storyId the story id
     * @return an arraylist of the videos
     */
    public List<Video> getAllVideos(int storyId) {
        List<Video> videoList = new ArrayList<Video>();

        String selectQuery = String.format(
                "SELECT * FROM %s WHERE %s = %d",
                TABLE_VIDEOS, KEY_STORY_ID, storyId
        );

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Video video = new Video(
                        cursor.getString(0),
                        Integer.parseInt(cursor.getString(1))
                );
                videoList.add(video);
            } while (cursor.moveToNext());
        }

        return videoList;
    }
}