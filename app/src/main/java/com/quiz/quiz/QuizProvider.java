package com.quiz.quiz;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by fujiaoyang1 on 10/21/16.
 */
public class QuizProvider extends ContentProvider {
    private static final String AUTHORITY = "com.quiz.quiz.provide_quiz"; //avoid conflicts with other providers
    private static final String BASE_PATH = "quiz";  // table name
    private static final String TAG = "**QuizProvider**";
    // the URI is used to determine which table to use, CONTENT_URI points to notes table
    // identify the content provider
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Creates a UriMatcher object.
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        // maps an authority and path to an integer value
        uriMatcher.addURI(AUTHORITY, BASE_PATH, 1);
        uriMatcher.addURI(AUTHORITY, BASE_PATH +  "/#", 2);
    }

    private SQLiteDatabase database;
    private QuizData helper = null;

    @Override
    public boolean onCreate() {
        helper = new QuizData(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }


    @Override
    public Cursor query (Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case 1:
                // Log.d(TAG, "selection fails  " + uri);
                break;
            case 2:
                selection = QuizData.QUIZ_ID + "=" + uri.getLastPathSegment();
                // Log.d(TAG, "selection works  " + uri);
                break;
        }
        return database.query(QuizData.TABLE_QUIZ, QuizData.ALL_COLUMNS,
                selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        // returned Uri is supposed to have the same pattern as that added into uriMatcher
        // get primary key value
        long id = database.insert(QuizData.TABLE_QUIZ, null, values);
        // parse method lets you put together a string and return the equivalent URI
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return database.delete(QuizData.TABLE_QUIZ, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(QuizData.TABLE_QUIZ, values, selection, selectionArgs);
    }
}
