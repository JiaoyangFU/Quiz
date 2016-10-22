package com.quiz.quiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fujiaoyang1 on 10/20/16.
 */
public class QuizData  extends SQLiteOpenHelper {
    //Constants for db name and version
    private static final String DATABASE_NAME = "quiz.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_QUIZ = "quiz";
    public static final String QUIZ_ID = "_id";
    public static final String QUIZ_TEXT = "quiz_text";
    public static final String OPTION_A = "option_a";
    public static final String OPTION_B = "option_b";
    public static final String OPTION_C = "option_c";
    public static final String OPTION_D = "option_d";
    public static final String QUIZ_ANSWER = "answer";
    public static final String TIME = "time";


    public static final String[] ALL_COLUMNS = { QUIZ_ID, QUIZ_TEXT, OPTION_A,
            OPTION_B, OPTION_C, OPTION_D, QUIZ_ANSWER, TIME};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_QUIZ + " (" +
                    QUIZ_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    QUIZ_TEXT + " TEXT, " +
                    OPTION_A + " TEXT, " +
                    OPTION_B + " TEXT, " +
                    OPTION_C + " TEXT, " +
                    OPTION_D + " TEXT, " +
                    QUIZ_ANSWER + " TEXT, " +
                    TIME + " TEXT" +
                    ")";

    public QuizData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUIZ);
        onCreate(db);
    }
}
