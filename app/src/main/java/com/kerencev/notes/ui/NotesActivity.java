package com.kerencev.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Note;

public class NotesActivity extends AppCompatActivity {

    private static final String EXTRA_NOTE = "EXTRA_NOTE";

    public static void show(Context context, Note note) {
        Intent intent = new Intent(context, NotesActivity.class);
        intent.putExtra(EXTRA_NOTE, note);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        if (savedInstanceState == null) {
            Note note = getIntent().getParcelableExtra(EXTRA_NOTE);

            NotesFragment notesFragment = NotesFragment.newInstance(note);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, notesFragment)
                    .commit();
        }
    }
}