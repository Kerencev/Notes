package com.kerencev.notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.InMemoryNotesRepository;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.ui.dialogFragments.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class NotesDescriptionFragment extends Fragment {

    private static final String INDEX_NOTE = "INDEX_NOTE";
    private static final String NEW_NAME = "NEW_NAME";

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";
    private Toolbar toolbar;
    private static SharedPreferences sharedPreferences;
    private String notesJson;
    List<Note> notes;

    /**
     * Метод для передачи фрагменту заметки и ее индекса для восстановления после удаления
     *
     * @param note        - заметка, удаление которой можно отменить
     * @param indexOfNote - индекс заметки, для восстановления в том же месте
     */
    public static NotesDescriptionFragment newInstance(Note note, int indexOfNote) {
        Bundle args = new Bundle();
        args.putParcelable(NotesFragment.ARG_NOTE, note);
        args.putInt(INDEX_NOTE, indexOfNote);
        NotesDescriptionFragment fragment = new NotesDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Метод для передачи фрагменту заметки и нового названия
     *
     * @param note - заметка, у которой меняем название
     * @param name - новое название
     */
    public static NotesDescriptionFragment newInstance(Note note, String name) {
        Bundle args = new Bundle();
        args.putParcelable(NotesFragment.ARG_NOTE, note);
        args.putString(NEW_NAME, name);
        NotesDescriptionFragment fragment = new NotesDescriptionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPreferences = requireActivity().getSharedPreferences("Store_notes", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
        toolbar = view.findViewById(R.id.bar_main_add);
        setOnClickNewNote(view);

        if (requireActivity() instanceof ToolbarHolder) {
            ((ToolbarHolder) requireActivity()).setToolbar(toolbar);
        }

        if (getArguments() != null && getArguments().containsKey(NotesFragment.ARG_NOTE)) {
            showSnackBartoCancelRemove(view);
        }
    }

    @Override
    public void onStop() {
        notesJson = new Gson().toJson(notes);
        Data.save(sharedPreferences, notesJson);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        notesJson = new Gson().toJson(notes);
        Data.save(sharedPreferences, notesJson);
        super.onDestroy();
    }

    private void initList(View view) {

        notes = InMemoryNotesRepository.getINSTANCE(requireContext()).getAll();

        if (getArguments() != null && getArguments().containsKey(NEW_NAME)) {
            changeNoteName(view);
        }

        LinearLayout layoutView = view.findViewById(R.id.container);

        for (Note note : notes) {

            View itemView = getLayoutInflater().inflate(R.layout.item_note_delete, layoutView, false);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("ResourceType")
                @Override
                public boolean onLongClick(View view) {
                    BottomSheetDialogFragment.newInstance(note).show(getParentFragmentManager(), "");
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showNote(note);
                }
            });

            TextView name = itemView.findViewById(R.id.title);
            TextView text = itemView.findViewById(R.id.text);
            TextView date = itemView.findViewById(R.id.date);

            name.setText(note.getName());
            text.setText(note.getDescription());
            date.setText(note.getDate());

            layoutView.addView(itemView);
        }
    }

    private void setOnClickNewNote(View view) {
        Note note = new Note(null, null, MyDate.getDate());

        view.findViewById(R.id.action_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNote(note);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.action_search) {
                    Toast.makeText(requireContext(), "search", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }

    private void showNote(Note note) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(ARG_PARAM2, note);
            getParentFragmentManager()
                    .setFragmentResult(ARG_PARAM1, bundle);
        } else {
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, NotesFragment.newInstance(note))
                    .addToBackStack("details")
                    .commit();
        }
    }

    public static SharedPreferences getMySharedPreferences() {
        return sharedPreferences;
    }

    private void showSnackBartoCancelRemove(View view) {
        Note note = getArguments().getParcelable(NotesFragment.ARG_NOTE);
        int indexOfNote = getArguments().getInt(INDEX_NOTE);

        getArguments().clear();

        Snackbar.make(view, "Заметка '" + note.getName() + "' удалена", Snackbar.LENGTH_SHORT)
                .setAction(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InMemoryNotesRepository.getINSTANCE(requireContext()).add(indexOfNote, note);
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, new NotesDescriptionFragment())
                                .commit();
                    }
                }).show();
    }

    private void changeNoteName(View view) {
        Note note = getArguments().getParcelable(NotesFragment.ARG_NOTE);
        String name = getArguments().getString(NEW_NAME);
        for (Note n : notes) {
            if (note.equals(n)) {
                n.setName(name);
            }
        }
        getArguments().clear();
    }
}