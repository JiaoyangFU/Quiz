package com.quiz.quiz;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by fujiaoyang1 on 10/20/16.
 */
public class QuizCursorAdapter extends CursorAdapter {
    public QuizCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //find layout
        return LayoutInflater.from(context).inflate(
                R.layout.question_item, parent, false
        );
    }
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //reuse layout and show data
        String quiz_text = cursor.getString(
                cursor.getColumnIndex(QuizData.QUIZ_TEXT));

        long time=cursor.getInt(
                cursor.getColumnIndex(QuizData.TIME));

        Log.d("QuizCursorAdapter", "the content #: " + quiz_text);

        TextView question_view = (TextView) view.findViewById(R.id.question_note);
        TextView time_view = (TextView) view.findViewById(R.id.time_show);

        question_view.setText(quiz_text);
        time_view.setText("" + String.format("%02d", time) + "s");
    }
}
