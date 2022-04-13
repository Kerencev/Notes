package com.kerencev.notes.logic;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InMemoryNotesRepository {

    private static InMemoryNotesRepository INSTANCE;

    private Context context;

    private ArrayList<Note> result;

    private InMemoryNotesRepository(Context context) {
        this.context = context;
    }

    public static InMemoryNotesRepository getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryNotesRepository(context);
        }
        return INSTANCE;
    }

    public List<Note> getAll() {
        ArrayList<Note> result = new ArrayList<>();
        SimpleDateFormat formater = new SimpleDateFormat("dd.MM.yy HH:mm");
        Date date = new Date();

        for (int i = 1; i <= 5; i++) {
            result.add(new Note("Заметка" + i, "Описание" + i, formater.format(date)));
        }

        return result;
    }

    public void add(Note note) {

    }
}
