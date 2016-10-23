package com.quiz.quiz;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by fujiaoyang1 on 10/23/16.
 */
public class QuizFinishActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_taker_finish);

        final int score = getIntent().getIntExtra("score_quiz_taker", 0);

        TextView tv = (TextView) findViewById(R.id.taker_performace);
        tv.setText("performance: " + " " + score + "/" + 5);

        Button newRoundBtn = (Button) findViewById(R.id.qt_new_round_button);
        newRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnIntent = new Intent(QuizFinishActivity.this, QuizTaker.class);
                returnIntent.putExtra("start_new_round","1");
                setResult(Activity.RESULT_OK,returnIntent);
                finish();
            }
        });
    }
}
