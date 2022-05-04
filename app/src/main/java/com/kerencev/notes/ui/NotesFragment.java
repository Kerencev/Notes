package com.kerencev.notes.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.NoteName;

public class NotesFragment extends Fragment {

    public static final String ARG_NOTE = "ARG_NOTE";

    private TextView title;
    private EditText text;
    private Toolbar toolbar;
    private Note note;

    public static NotesFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);

        NotesFragment fragment = new NotesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public NotesFragment() {
        super(R.layout.fragment_notes);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        title = view.findViewById(R.id.title);
        text = view.findViewById(R.id.text);
        toolbar = view.findViewById(R.id.toolbar);
        setClicksToolbar();

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {
            note = getArguments().getParcelable(ARG_NOTE);
            showNote(note);
        }
    }

    private void setClicksToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                getParentFragmentManager().popBackStack();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_save:
                        if (note.getDescription() != null) {
                            updateNote();
                        } else {
                            saveNewNote();
                        }

                        hideKeyboard();
                        getParentFragmentManager().popBackStack();
                        return true;

                    case R.id.action_delete:
                        if (note.getDescription() != null) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(Data.KEY_BUNDLE_DELETE_NOTE, note);
                            getParentFragmentManager().setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);

                            Dependencies.getNotesRepository().removeNote(note, new Callback<Void>() {
                                @Override
                                public void onSuccess(Void data) {

                                }

                                @Override
                                public void onError(Throwable exception) {

                                }
                            });
                        }

                        getParentFragmentManager().popBackStack();
                        hideKeyboard();
                        return true;
                }
                return false;
            }
        });
    }

    private void showNote(Note note) {
        if (note.getName() != null) {
            toolbar.setTitle(note.getName());
            title.setVisibility(View.GONE);
        } else {
            title.setText(note.getName());
        }
        text.setText(note.getDescription());
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
    }

    private void updateNote() {
        Dependencies.getNotesRepository().updateNote(note, note.getName(), text.getText().toString(), new Callback<Note>() {
            @Override
            public void onSuccess(Note data) {

            }

            @Override
            public void onError(Throwable exception) {

            }
        });

        note.setDescription(text.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putParcelable(Data.KEY_BUNDLE_UPDATE_NOTE, note);

        getParentFragmentManager().setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
    }

    private void saveNewNote() {

        Dependencies.getNotesRepository().addNote(title.getText().toString(), text.getText().toString(), new Callback<Note>() {
            @Override
            public void onSuccess(Note data) {

            }

            @Override
            public void onError(Throwable exception) {

            }
        });

        if (title.getText().length() == 0) {
            note.setName(NoteName.setDefaultName(text.getText().toString()));
        } else {
            note.setName(title.getText().toString());
        }

        note.setDescription(text.getText().toString());

        Bundle bundle = new Bundle();
        bundle.putParcelable(Data.KEY_BUNDLE_ADD_NEW_NOTE, note);

        getParentFragmentManager().setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
    }

}