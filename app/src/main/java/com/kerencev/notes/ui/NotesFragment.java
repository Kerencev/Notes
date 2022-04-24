package com.kerencev.notes.ui;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.InMemoryNotesRepository;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.NoteName;

public class NotesFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

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

        setMyCallback();

        getParentFragmentManager()
                .setFragmentResultListener(NotesDescriptionFragment.ARG_PARAM1, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = result.getParcelable(NotesDescriptionFragment.ARG_PARAM2);

                        showNote(note);
                    }
                });
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

                        saveNewNote();
                        hideKeyboard();
                        getParentFragmentManager().popBackStack();
                        return true;

                    case R.id.action_delete:

                        if (note.getDescription() != null) {
                            InMemoryNotesRepository.getINSTANCE(requireContext()).delete(note);

                            Toast.makeText(requireContext(), "Заметка " + " '" + note.getName() + "' " + "удалена", Toast.LENGTH_SHORT).show();
                        }

                        hideKeyboard();
                        getParentFragmentManager().popBackStack();
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

    private void saveNewNote() {
        if (text.getText().length() > 0 && !InMemoryNotesRepository.getINSTANCE(requireContext()).contains(note)) {
            Note note;

            if (title.getText().length() > 0) {
                note = new Note(title.getText().toString(), text.getText().toString(), MyDate.getDate());
            } else {
                note = new Note(NoteName.setDefaultName(text), text.getText().toString(), MyDate.getDate());
            }

            InMemoryNotesRepository.getINSTANCE(requireContext()).add(note);
        }

        if (text.getText().length() > 0) {
            note.setDescription(text.getText().toString());
        }
    }

    private void setMyCallback() {
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                saveNewNote();
                getParentFragmentManager().popBackStack();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(requireActivity(), callback);
    }
}