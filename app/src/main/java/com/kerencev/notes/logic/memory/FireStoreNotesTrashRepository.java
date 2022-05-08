package com.kerencev.notes.logic.memory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kerencev.notes.logic.Note;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Класс заметок в корзине
 */

public class FireStoreNotesTrashRepository {

    public static final String TRASH = "trash";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public void addNote(Note note) {
        HashMap<String, Object> data = new HashMap<>();

        data.put(FireStoreNotesRepository.KEY_TITLE, note.getName());
        data.put(FireStoreNotesRepository.KEY_MESSAGE, note.getDescription());
        data.put(FireStoreNotesRepository.KEY_CREATED_AT, note.getDate());
        data.put(FireStoreNotesRepository.KEY_COLOR, String.valueOf(note.getColor()));
        data.put("DATE", note.getDateForSort());

        firestore.collection(TRASH).add(data);
    }

    public void deleteNote(Note note) {
        firestore.collection(TRASH)
                .document(note.getId())
                .delete();
    }

    public void deleteAll() {
        firestore.collection(TRASH)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            firestore.collection(TRASH)
                                    .document(documentSnapshot.getId())
                                    .delete();
                        }
                    }
                });
    }
}
