package com.kerencev.notes.logic;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InMemoryNotesRepository {

    private static InMemoryNotesRepository INSTANCE;

    private Context context;

    private static ArrayList<Note> result;

    private InMemoryNotesRepository(Context context) {
        this.context = context;
    }

    public static InMemoryNotesRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            result = new ArrayList<>();
            INSTANCE = new InMemoryNotesRepository(context);
        }
        return INSTANCE;
    }

    public List<Note> getAll() {
        return result;
    }

    public void add(Note note) {
        result.add(0, note);
    }

    public void delete(Note note) {
        result.remove(note);
    }

    public boolean contains(Note note) {
        return result.contains(note);
    }
}
