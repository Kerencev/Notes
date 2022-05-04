package com.kerencev.notes.logic.memory;

public class Dependencies {

    private static final NotesRepository NOTES_REPOSITORY = new FireStoreNotesRepository();

    public static NotesRepository getNotesRepository() {
        return NOTES_REPOSITORY;
    }
}
