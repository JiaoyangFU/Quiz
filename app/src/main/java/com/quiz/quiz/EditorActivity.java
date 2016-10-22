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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

/**
 * Created by fujiaoyang1 on 10/21/16.
 */
public class EditorActivity extends Activity implements OnItemSelectedListener{

    private EditText question_edit, opt_a_edit, opt_b_edit, opt_c_edit;
    private String question_text, opt_a_text, opt_b_text, opt_c_text, answer_text;
    private String action, selection, time_seleted, time_text;
    private Spinner spinner;
    private RadioButton opt_a, opt_b, opt_c;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        spinner = (Spinner) findViewById(R.id.time_spinner);
        spinner.setOnItemSelectedListener(this);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter); // Apply the adapter to the spinner
        ArrayAdapter<String> array_spinner=(ArrayAdapter<String>)spinner.getAdapter();

        opt_a = (RadioButton)findViewById(R.id.option_a);
        opt_b = (RadioButton)findViewById(R.id.option_b);
        opt_c = (RadioButton)findViewById(R.id.option_c);

        question_edit = (EditText)findViewById(R.id.question_text);
        opt_a_edit = (EditText)findViewById(R.id.option_a_text);
        opt_b_edit = (EditText)findViewById(R.id.option_b_text);
        opt_c_edit = (EditText)findViewById(R.id.option_c_text);
        answer_text = "A";
        Intent intent = getIntent();  //the intent that launch this activity
        Uri uri = intent.getParcelableExtra(QuizMaker.URL_DATA);

        if (uri == null) {//add button is pressed and add a new quiz to database
            action = Intent.ACTION_INSERT;
            setTitle("add a quiz");
        } else {// item exist already and need to update existed contend and time
            action = Intent.ACTION_EDIT;
            setTitle("modify a quiz");
            selection = QuizData.QUIZ_ID + "=" + uri.getLastPathSegment();
            // retrieve one row in the database
            Cursor cursor = getContentResolver().query(uri, QuizData.ALL_COLUMNS, selection,
                    null, null);
            cursor.moveToFirst();
            question_text = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_TEXT));
            opt_a_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_A));
            opt_b_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_B));
            opt_c_text = cursor.getString(cursor.getColumnIndex(QuizData.OPTION_C));
            answer_text = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_ANSWER));
            time_text = cursor.getString(cursor.getColumnIndex(QuizData.TIME));

            switch(answer_text) {
                case "A":
                    opt_a.setChecked(true);
                    break;
                case "B":
                    opt_b.setChecked(true);
                    break;
                case "C":
                    opt_c.setChecked(true);
                    break;
            }

            question_edit.setText(question_text);
            opt_a_edit.setText(opt_a_text);
            opt_b_edit.setText(opt_b_text);
            opt_c_edit.setText(opt_c_text);
            spinner.setSelection(array_spinner.getPosition(time_text));

            question_edit.requestFocus(); //move the cursor to the end of the existing text
           // opt_a_edit.requestFocus();
           // opt_b_edit.requestFocus();
           // opt_c_edit.requestFocus();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        time_seleted = parent.getItemAtPosition(pos).toString();
        //Toast.makeText(this, time_seleted, Toast.LENGTH_SHORT).show();
        //time = Integer.parseInt(time_seleted.substring(0,2));
        //time_text = String.valueOf(time) + "s";
        //Toast.makeText(this, time_text, Toast.LENGTH_SHORT).show();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
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
        Toast.makeText(this, answer_text, Toast.LENGTH_SHORT).show();
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
        values.put(QuizData.TIME, time_seleted);

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
        values.put(QuizData.TIME, time_seleted);
        getContentResolver().insert(QuizProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

}
