package com.kerencev.notes.logic.memory;

import com.kerencev.notes.logic.memory.todo.TodoRepository;
import com.kerencev.notes.logic.memory.todo.TodoRepositoryImpl;

public class Dependencies {

    private static final NotesRepository NOTES_REPOSITORY = new FireStoreNotesRepository();
    private static final FireStoreNotesTrashRepository NOTES_TRASH_REPOSITORY = new FireStoreNotesTrashRepository();
    private static final TodoRepository TODO_REPOSITORY = new TodoRepositoryImpl();

    public static NotesRepository getNotesRepository() {
        return NOTES_REPOSITORY;
    }

    public static FireStoreNotesTrashRepository getNotesTrashRepository() {
        return NOTES_TRASH_REPOSITORY;
    }

    public static TodoRepository getTodoRepository() {
        return TODO_REPOSITORY;
    }
}
