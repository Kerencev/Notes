package com.kerencev.notes.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.kerencev.notes.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotesDescriptionFragment notesDescriptionFragment = new NotesDescriptionFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, notesDescriptionFragment)
                .commit();
    }
}