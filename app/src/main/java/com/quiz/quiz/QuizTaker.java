package com.quiz.quiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by fujiaoyang1 on 10/19/16.
 */

public class QuizTaker extends Activity {
    private int time, score, quizCnt, quizTotalCnt;
    private String question_text, opt_a_text, opt_b_text,
            opt_c_text, correct_answer, selected_answer;
    private TextView question_view, opt_a_view, opt_b_view, opt_c_view, time_view;
    private RadioButton opt_a_btn, opt_b_btn, opt_c_btn;
    private CountDownTimer count_down_timer;
    private boolean isSelected, hasTime;
    ArrayList<Integer> list_random_id = new ArrayList<Integer>();
    private static final String TAG = "** QuizTaker ** ";
    private static final int EDITOR_REQUEST_CODE = 20;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_taker);
        opt_a_btn = (RadioButton)findViewById(R.id.option_a_id);
        opt_b_btn = (RadioButton)findViewById(R.id.option_b_id);
        opt_c_btn = (RadioButton)findViewById(R.id.option_c_id);

        time_view = (TextView)findViewById(R.id.remain_time_id);
        question_view = (TextView)findViewById(R.id.question_text_id);
        opt_a_view = (TextView)findViewById(R.id.option_a_text_id);
        opt_b_view = (TextView)findViewById(R.id.option_b_text_id);
        opt_c_view = (TextView)findViewById(R.id.option_c_text_id);
        Cursor cursor = getContentResolver().query(QuizProvider.CONTENT_URI, null, null,
                null, null);
        quizTotalCnt = cursor.getCount();

        for (int i=1; i<= quizTotalCnt; i++) {
            list_random_id.add(new Integer(i)); // used to get unique random number
        }
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
            if (isSelected || !hasTime) {
                count_down_timer.cancel();
                getNextQuiz();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE ){
            //Log.d(TAG, "result:   " + data.getStringExtra("start_new_round"));
            if (data.getStringExtra("start_new_round").equals("1") && resultCode == RESULT_OK) {
                //Log.d(TAG, "new round:   " + "1");
                setNewRound();
            }
            else {
                //Log.d(TAG, "on Press Back :   " + "0");
                finish();
            }
        }
    }

    private void setNewRound() {
        time = 0;
        score = 0;
        quizCnt = 5;
        isSelected = false;
        hasTime = true;
        count_down_timer = null;
        Collections.shuffle(list_random_id);
        getNextQuiz();
    }

    private void getNextQuiz(){
        if (quizCnt-- == 0) {
            super.onResume();
            Intent intent = new Intent(this, QuizFinishActivity.class);
            intent.putExtra("score_quiz_taker", score);
            startActivityForResult(intent, EDITOR_REQUEST_CODE);
            return;
        }
        isSelected = false;
        hasTime = true;
        count_down_timer = null;
        opt_a_view.setTextColor(getResources().getColor(R.color.general));
        opt_b_view.setTextColor(getResources().getColor(R.color.general));
        opt_c_view.setTextColor(getResources().getColor(R.color.general));
        opt_a_btn.setChecked(false);
        opt_b_btn.setChecked(false);
        opt_c_btn.setChecked(false);


        setTitle("Score: " + score + "/" + 5 + "   NO." + (5 - quizCnt));
        //Log.d(TAG, "random id is:   " + id)
        int id = list_random_id.get(quizCnt);
        //Log.d(TAG, "random id is:   " + id);
        String selection = QuizData.QUIZ_ID + "=" + Integer.toString(id);

        // retrieve one row in the database
        Cursor cursor = getContentResolver().query(QuizProvider.CONTENT_URI, QuizData.ALL_COLUMNS, selection,
                null, null);
        cursor.moveToFirst();
        question_text = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_TEXT));

        opt_a_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_A));
        opt_b_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_B));
        opt_c_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_C));
        correct_answer = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_ANSWER));
        //Log.d(TAG, "correct answer is:   " + correct_answer);
        //Log.d(TAG, "origin isSelected:   " + isSelected);

        String time_text = cursor.getString(cursor.getColumnIndex(QuizData.TIME));
        time = Integer.parseInt(time_text.substring(0, 2));

        time_view.setText("remain time: " + time_text);
        question_view.setText(question_text);
        opt_a_view.setText(opt_a_text);
        opt_b_view.setText(opt_b_text);
        opt_c_view.setText(opt_c_text);
        count_down_timer = new CountDownTimer(time * 1000 + 300, 1000) {// 300 is used to minimize error when go next
            @Override
            public void onTick(long millisUntilFinished) {
                time_view.setText("remain time" + " " + millisUntilFinished / 1000);
            }
            @Override
            public void onFinish() {
                hasTime = false;
                time_view.setText("remain time" + " " + 0);
                if(!isSelected)loseScore();
            }
        }.start();
    }

    private void loseScore() {
        int [] opt_array = {1, 1, 1};
        TextView [] opt_views = {opt_a_view, opt_b_view, opt_c_view};
        TextView blink_text_view;
        opt_array[correct_answer.charAt(0) - 'A'] = 0;
        for (int i = 0; i < opt_array.length; i++) {
            if (opt_array[i] == 1) {
                // set incorrect text view option to red
                opt_views[i].setTextColor(getResources().getColor(R.color.incorrect));
            }
            else {
                // set correct text view option to green and blink
                opt_views[i].setTextColor(getResources().getColor(R.color.correct));

                blink_text_view = opt_views[i];
                Animation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(50); //You can manage the blinking time with this parameter
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(15);
                blink_text_view.startAnimation(anim);
            }
        }
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();
        // count_down_timer.cancel();
        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.option_a_id:
                if (checked)
                    selected_answer = "A";
                break;
            case R.id.option_b_id:
                if (checked)
                    selected_answer = "B";
                break;
            case R.id.option_c_id:
                if (checked)
                    selected_answer = "C";
                break;
        }
        //Log.d(TAG, "select answer:   " + selected_answer);
        //Log.d(TAG, "radio button isSelected:   " + isSelected);
        if (!isSelected && hasTime) {
            if (selected_answer.equals(correct_answer)) {
                score++;
                count_down_timer.cancel();
                //Log.d(TAG, "choose correct:   " + correct_answer);
                getNextQuiz(); // go to  next quiz automatically
            }
            else {
                loseScore();
                isSelected = true;
            }
        }
        // Toast.makeText(this, selected_answer, Toast.LENGTH_SHORT).show();
    }

}
