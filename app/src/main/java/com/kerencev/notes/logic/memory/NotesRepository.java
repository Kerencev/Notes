package com.kerencev.notes.logic.memory;

import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Note;

import java.util.List;

public interface NotesRepository {

    void getAll(Callback<List<Note>> callback);

    void addNote(String title, String message, int color, Callback<Note> callback);

    void removeNote(Note note, Callback<Void> callback);

    void updateNote(Note note, String title, String message, Callback<Note> callback);
}
