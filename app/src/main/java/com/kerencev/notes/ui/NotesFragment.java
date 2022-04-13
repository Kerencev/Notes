package com.kerencev.notes.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.InMemoryNotesRepository;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.Note;

public class NotesFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    private TextView title;
    private EditText text;
    private ImageView toolbarBack;
    private ImageView toolbarSave;
    private ImageView toolbarDelete;
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
        toolbarBack = view.findViewById(R.id.bar_back);
        toolbarSave = view.findViewById(R.id.bar_save);
        toolbarDelete = view.findViewById(R.id.bar_delete);

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {

            note = getArguments().getParcelable(ARG_NOTE);
            showNote(note);
        } else {
            toolbarBack.setVisibility(View.GONE);
        }

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().popBackStack();
            }
        });

        toolbarSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (text.getText().length() > 0 && !InMemoryNotesRepository.getINSTANCE(requireContext()).contains(note)) {
                    Note note;

                    if (title.getText().length() > 0) {
                        note = new Note(title.getText().toString(), text.getText().toString(), MyDate.getDate());
                    } else {
                        note = new Note("Без названия", text.getText().toString(), MyDate.getDate());
                    }

                    InMemoryNotesRepository.getINSTANCE(requireContext()).add(note);

                } else if (text.getText().length() > 0) {
                    note.setDescription(text.getText().toString());
                }

                getParentFragmentManager().popBackStack();
            }
        });

        toolbarDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (note.getDescription() != null) {
                    InMemoryNotesRepository.getINSTANCE(requireContext()).delete(note);

                    Toast.makeText(requireContext(), "Заметка " + " '" + note.getName() + "' " + "удалена", Toast.LENGTH_SHORT).show();
                }

                getParentFragmentManager().popBackStack();
            }
        });

        getParentFragmentManager()
                .setFragmentResultListener(NotesDescriptionFragment.ARG_PARAM1, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = result.getParcelable(NotesDescriptionFragment.ARG_PARAM2);

                        showNote(note);
                    }
                });
    }

    private void showNote(Note note) {
        title.setText(note.getName());
        text.setText(note.getDescription());
    }
}