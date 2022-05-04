package com.kerencev.notes.logic.memory;

import android.os.Handler;
import android.os.Looper;

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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FireStoreNotesRepository implements NotesRepository {

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler handler = new Handler(Looper.getMainLooper());

    private static final String KEY_TITLE = "title";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CREATED_AT = "createdAt";

    private static final String NOTES = "notes";

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    @Override
    public void getAll(Callback<List<Note>> callback) {

        executor.execute(new Runnable() {
            @Override
            public void run() {

                firestore.collection(NOTES)
                        .orderBy("DATE", Query.Direction.DESCENDING)
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

                                    result.add(new Note(id, title, message, date));
                                }

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onSuccess(result);
                                    }
                                });
                            }
                        });
            }
        });
    }

    @Override
    public void addNote(String title, String message, Callback<Note> callback) {

        HashMap<String, Object> data = new HashMap<>();

        String name = title;
        if (title.length() == 0) {
            name = NoteName.setDefaultName(message);
        }

        data.put(KEY_TITLE, name);
        data.put(KEY_MESSAGE, message);
        data.put(KEY_CREATED_AT, MyDate.getDate());
        data.put("DATE", new Date());

        String finalName = name;
        firestore.collection(NOTES)
                .add(data)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        callback.onSuccess(new Note(documentReference.getId(), finalName, message, MyDate.getDate()));
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

        firestore.collection(NOTES)
                .document(note.getId())
                .update(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Note result = new Note(note.getId(), title, message, note.getDate());
                        callback.onSuccess(result);
                    }
                });
    }
}
