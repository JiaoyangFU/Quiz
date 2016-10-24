package com.quiz.quiz;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = "** MainActivity ** ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button quiz_maker_button = (Button) findViewById(R.id.quiz_maker_button);
        quiz_maker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, QuizMaker.class);
                startActivity(intent);
            }
        });
        Button quiz_taker_button = (Button) findViewById(R.id.quiz_taker_button);
        quiz_taker_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Cursor cursor = getContentResolver().query(QuizProvider.CONTENT_URI, null, null,
                        null, null);
                int quizTotalCnt = cursor.getCount();
                Log.d(TAG, "quizTotalCnt:   " + quizTotalCnt);
                if (quizTotalCnt < 5) {
                    Toast.makeText(MainActivity.this, "Not enough quiz", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, QuizTaker.class);
                    startActivity(intent);
                }
            }
        });
    }
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */
    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
