package com.quiz.quiz;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by fujiaoyang1 on 10/21/16.
 */
public class EditorActivity extends Activity {
    private String action; // insert or update action
    private EditText editor;
    private String selection; //where clause that is used in SQLite statement
    private String oldText; //contains the existing text of the selected note
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        editor = (EditText)findViewById(R.id.editText);
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
            oldText = cursor.getString(cursor.getColumnIndex(QuizData.QUIZ_TEXT));
            editor.setText(oldText);
            editor.requestFocus(); //move the cursor to the end of the existing text
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
                deleteNote();
                break;
        }
        return true;
    }

    private void finishEditing(){
        String newText = editor.getText().toString().trim();
        switch (action) {
            case Intent.ACTION_INSERT:
                if (newText.length() == 0) {
                    setResult(RESULT_CANCELED);
                }
                else {// insert data
                    //insertNote("12234");
                    insertNote(newText);
                }
                break;
            case Intent.ACTION_EDIT:
                if (newText.length() == 0) {
                    deleteNote();
                } else if (oldText.equals(newText)) {
                    setResult(RESULT_CANCELED);
                } else {
                    updateNote(newText);
                }
                break;
        }
        finish(); //go back to the parent activity
    }

    private void deleteNote() {
        getContentResolver().delete(QuizProvider.CONTENT_URI, selection, null);
        Toast.makeText(this, "delete a quiz", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    private void updateNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(QuizData.QUIZ_TEXT, noteText);
        getContentResolver().update(QuizProvider.CONTENT_URI, values, selection, null);
        Toast.makeText(this, "update a quiz", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);  // tell the mainActivity that sth has changed, need to update the database
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(QuizData.QUIZ_TEXT, noteText);
        getContentResolver().insert(QuizProvider.CONTENT_URI, values);
        setResult(RESULT_OK);
    }

    @Override
    public void onBackPressed() {
        finishEditing();
    }

}
