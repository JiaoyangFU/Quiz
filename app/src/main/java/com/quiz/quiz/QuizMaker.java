package com.quiz.quiz;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by fujiaoyang1 on 10/19/16.
 */
public class QuizMaker extends Activity implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final String URL_DATA = "com.quiz.quiz.maker_data";

    private CursorAdapter cursorAdapter;
    private static final int EDITOR_REQUEST_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_make);
        getLoaderManager().initLoader(0, null, this);

        cursorAdapter = new QuizCursorAdapter(this, null, 0);

        ListView list = (ListView)findViewById(R.id.list);
        list.setAdapter(cursorAdapter);
        list.setClickable(true);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QuizMaker.this, EditorActivity.class);
                Uri uri = Uri.parse(QuizProvider.CONTENT_URI + "/" + id);
                intent.putExtra(URL_DATA, uri);
                startActivityForResult(intent, EDITOR_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_quiz_maker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.action_delete_all:
                deleteAllQuiz();
                break;

            case R.id.action_add:
                quizAdd();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void restartLoader() {
        getLoaderManager().restartLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, QuizProvider.CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }
    void deleteAllQuiz(){
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            String selection = QuizData.TABLE_QUIZ + "=" + QuizData.TABLE_QUIZ;
                            getContentResolver().delete(
                                    QuizProvider.CONTENT_URI, selection, null
                            );
                            restartLoader();  // show refreshed database
                            Toast.makeText(QuizMaker.this,"Deleted All Quizs",Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure ?")
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();
    }

    void quizAdd(){
        Intent intent = new Intent(QuizMaker.this, EditorActivity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDITOR_REQUEST_CODE ){
            restartLoader();
        }
    }
}
