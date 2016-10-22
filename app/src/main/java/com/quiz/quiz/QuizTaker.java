package com.quiz.quiz;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by fujiaoyang1 on 10/19/16.
 */

public class QuizTaker extends Activity {
    private int time, score, quizCnt, quizTotalCnt;
    private String question_text, opt_a_text, opt_b_text, opt_c_text, correct_answer;
    private TextView question_view, opt_a_view, opt_b_view, opt_c_view;
    private RadioButton opt_a, opt_b, opt_c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_taker);
        opt_a = (RadioButton)findViewById(R.id.option_a_id);
        opt_b = (RadioButton)findViewById(R.id.option_b_id);
        opt_c = (RadioButton)findViewById(R.id.option_c_id);

        question_view = (TextView)findViewById(R.id.question_text_id);
        opt_a_view = (TextView)findViewById(R.id.option_a_text_id);
        opt_b_view = (TextView)findViewById(R.id.option_b_text_id);
        opt_c_view = (TextView)findViewById(R.id.option_c_text_id);
        Cursor cursor = getContentResolver().query(QuizProvider.CONTENT_URI, null, null,
                null, null);
        quizTotalCnt = cursor.getCount();
        setNewRound();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_taker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.qt_action_next) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setNewRound() {
        time = 0;
        score = 0;
        quizCnt = 5;
        getNextQuiz();
    }

    private void getNextQuiz(){
        Random randomGenerator = new Random();
        int id = randomGenerator.nextInt(quizTotalCnt);
        String selection = QuizData.QUIZ_ID + "=" + Integer.toString(id);
        Uri uri = Uri.parse(QuizProvider.CONTENT_URI + "/" + id);
        // retrieve one row in the database
        Cursor cursor = getContentResolver().query(uri, QuizData.ALL_COLUMNS, selection,
                null, null);
        cursor.moveToFirst();
        question_text = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_TEXT));
        opt_a_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_A));
        opt_b_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_B));
        opt_c_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_C));
        correct_answer = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_ANSWER));
        String time_text = cursor.getString(cursor.getColumnIndex(QuizData.TIME));
        time = Integer.parseInt(time_text.substring(0,2));
    }
}
