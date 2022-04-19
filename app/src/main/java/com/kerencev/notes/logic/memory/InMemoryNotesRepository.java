package com.kerencev.notes.logic.memory;

import android.content.Context;

import com.kerencev.notes.logic.Note;

import java.util.ArrayList;
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

    public void loadAll(List<Note> notes) {
        if (result.size() == 0) {
            result.addAll(notes);
        }
    }

    public void add(int index, Note note) {
        result.add(index, note);
    }

    public int delete(Note note) {
        int index = result.indexOf(note);
        result.remove(note);
        return index;
    }

    public boolean contains(Note note) {
        return result.contains(note);
    }

}
