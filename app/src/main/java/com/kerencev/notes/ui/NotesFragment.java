package com.kerencev.notes.ui;

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

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Note;

public class NotesFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";

    private TextView title;
    private EditText text;
    private ImageView toolbarBack;

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

        toolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {

            Note note = getArguments().getParcelable(ARG_NOTE);
            showNote(note);
        } else {
            toolbarBack.setVisibility(View.GONE);
        }
    }

    private void showNote(Note note) {
        title.setText(note.getName());
        text.setText(note.getDescription());
    }
}