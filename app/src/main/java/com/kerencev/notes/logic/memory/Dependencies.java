package com.kerencev.notes.logic.memory;

public class Dependencies {

    private static final NotesRepository NOTES_REPOSITORY = new FireStoreNotesRepository();
    private static final FireStoreNotesTrashRepository NOTES_TRASH_REPOSITORY = new FireStoreNotesTrashRepository();

    public static NotesRepository getNotesRepository() {
        return NOTES_REPOSITORY;
    }

    public static FireStoreNotesTrashRepository getNotesTrashRepository() {
        return NOTES_TRASH_REPOSITORY;
    }
}
