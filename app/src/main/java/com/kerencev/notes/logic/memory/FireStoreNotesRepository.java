package com.kerencev.notes.logic.memory;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.NoteName;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class FireStoreNotesRepository implements NotesRepository {

    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_COLOR = "KEY_COLOR";
    public static final String KEY_IS_FIXED = "KEY_IS_FIXED";

    public static final String NOTES = "notes";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public void getAll(String repository, Query.Direction direction, Callback<List<Note>> callback) {

        firestore.collection(repository)
                .orderBy("DATE", direction)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        ArrayList<Note> result = new ArrayList<>();

                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                            String id = documentSnapshot.getId();

                            String title = documentSnapshot.getString(KEY_TITLE);
                            String message = documentSnapshot.getString(KEY_MESSAGE);
                            String date = documentSnapshot.getString(KEY_CREATED_AT);
                            String color = documentSnapshot.getString(KEY_COLOR);
                            Date dateForSort = documentSnapshot.getDate("DATE");
                            boolean isFixed = documentSnapshot.getBoolean(KEY_IS_FIXED);

                            Note note = new Note(id, title, message, date, Integer.parseInt(color), dateForSort);
                            note.setFixed(isFixed);

                            result.add(note);
                        }

                        callback.onSuccess(result);
                    }
                });
    }

    @Override
    public void addNote(String title, String message, int color, Date date, String myDate, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();

        String name = title;
        if (title.length() == 0) {
            name = NoteName.setDefaultName(message);
        }

        data.put(KEY_TITLE, name);
        data.put(KEY_MESSAGE, message);
        data.put(KEY_CREATED_AT, myDate);
        data.put(KEY_COLOR, String.valueOf(color));
        data.put("DATE", date);
        data.put(KEY_IS_FIXED, false);

        String finalName = name;

        firestore.collection(NOTES)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess(new Note(documentReference.getId(), finalName, message, MyDate.getDate(), color, new Date()));
                    }
                });
    }

    @Override
    public void removeNote(Note note, Callback<Void> callback) {

        firestore.collection(NOTES)
                .document(note.getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        callback.onSuccess(unused);
                    }
                });
    }

    @Override
    public void updateNote(Note note, String title, String message, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();
        data.put(KEY_TITLE, title);
        data.put(KEY_MESSAGE, message);
        data.put(KEY_COLOR, String.valueOf(note.getColor()));
        data.put(KEY_IS_FIXED, note.isFixed());

        firestore.collection(NOTES)
                .document(note.getId())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Note result = new Note(note.getId(), title, message, note.getDate(), note.getColor(), note.getDateForSort());
                        callback.onSuccess(result);
                    }
                });
    }
}
