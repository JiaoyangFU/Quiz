package com.quiz.quiz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

/**
 * Created by fujiaoyang1 on 10/21/16.
 */
public class EditorActivity extends Activity {

    private EditText question_edit, opt_a_edit, opt_b_edit, opt_c_edit;
    private String question_text, opt_a_text, opt_b_text, opt_c_text, answer_text;
    private String action, selection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        question_edit = (EditText)findViewById(R.id.question_text);
        opt_a_edit = (EditText)findViewById(R.id.option_a_text);
        opt_b_edit = (EditText)findViewById(R.id.option_b_text);
        opt_c_edit = (EditText)findViewById(R.id.option_c_text);

        Intent intent = getIntent();  //the intent that launch this activity
        Uri uri = intent.getParcelableExtra(QuizMaker.URL_DATA);

        if (uri == null) {//add button is pressed and add a new quiz to database
            action = Intent.ACTION_INSERT;
            setTitle("add");
        } else {// item exist already and need to update existed contend and time
            action = Intent.ACTION_EDIT;
            setTitle("modify");
            selection = QuizData.QUIZ_ID + "=" + uri.getLastPathSegment();
            // retrieve one row in the database
            Cursor cursor = getContentResolver().query(uri, QuizData.ALL_COLUMNS, selection,
                    null, null);
            cursor.moveToFirst();
            question_text = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_TEXT));
            opt_a_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_A));
            opt_b_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_B));
            opt_c_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_C));

            question_edit.setText(question_text);
            opt_a_edit.setText(opt_a_text);
            opt_b_edit.setText(opt_b_text);
            opt_c_edit.setText(opt_c_text);

            question_edit.requestFocus(); //move the cursor to the end of the existing text
            opt_a_edit.requestFocus();
            opt_b_edit.requestFocus();
            opt_c_edit.requestFocus();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(action.equals(Intent.ACTION_INSERT)) {
            getMenuInflater().inflate(R.menu.menue_editor, menu);
            menu.findItem(R.id.action_delete).setVisible(false);
        } else {
            getMenuInflater().inflate(R.menu.menue_editor, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finishEditing();
                break;
            case R.id.action_save:
                finishEditing();
                break;
            case R.id.action_delete:
                deleteQuiz();
                break;
        }
        return true;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.option_a:
                if (checked)
                    answer_text = "A";
                    break;
            case R.id.option_b:
                if (checked)
                    answer_text = "B";
                    break;
            case R.id.option_c:
                if (checked)
                    answer_text = "C";
                    break;
        }
    }

    private void finishEditing(){
        question_text = question_edit.getText().toString().trim();
        opt_a_text = opt_a_edit.getText().toString().trim();
        opt_b_text = opt_b_edit.getText().toString().trim();
        opt_c_text = opt_c_edit.getText().toString().trim();

        switch (action) {
            case Intent.ACTION_INSERT:
                if (question_text.length() == 0) {
                    setResult(RESULT_CANCELED);
                }
                else {// insert data
                    insertQuiz();
                }
                break;
            case Intent.ACTION_EDIT:
                if (question_text.length() == 0) {
                    deleteQuiz();
                } else {
                    updateQuiz();
                }
                break;
        }
        finish(); //go back to the parent activity
    }

    private void deleteQuiz() {
        getContentResolver().delete(QuizProvider.CONTENT_URI, selection, null);
        Toast.makeText(this, "delete a quiz", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateQuiz() {
        ContentValues values = new ContentValues();
        values.put(QuizData.QUIZ_TEXT, question_text);
        values.put(QuizData.OPTION_A, opt_a_text);
        values.put(QuizData.OPTION_B, opt_b_text);
        values.put(QuizData.OPTION_C, opt_c_text);
        values.put(QuizData.QUIZ_ANSWER, answer_text);

        getContentResolver().update(QuizProvider.CONTENT_URI, values, selection, null);
        Toast.makeText(this, "update a quiz", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);  // tell the mainActivity that sth has changed, need to update the database
    }

    private void insertQuiz() {
        ContentValues values = new ContentValues();
        values.put(QuizData.QUIZ_TEXT, question_text);
        values.put(QuizData.OPTION_A, opt_a_text);
        values.put(QuizData.OPTION_B, opt_b_text);
        values.put(QuizData.OPTION_C, opt_c_text);
        values.put(QuizData.QUIZ_ANSWER, answer_text);
        getContentResolver().insert(QuizProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

}
