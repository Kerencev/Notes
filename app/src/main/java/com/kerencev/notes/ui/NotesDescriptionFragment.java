package com.kerencev.notes.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.InMemoryNotesRepository;
import com.kerencev.notes.logic.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class NotesDescriptionFragment extends Fragment {

    public static final String ARG_PARAM1 = "param1";
    public static final String ARG_PARAM2 = "param2";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_description, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(view);
    }

    private void initList(View view) {

        List<Note> notes = InMemoryNotesRepository.getINSTANCE(requireContext()).getAll();
        LinearLayout layoutView = view.findViewById(R.id.container);

        for (Note note : notes) {
            View itemView = getLayoutInflater().inflate(R.layout.item_note, layoutView, false);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ARG_PARAM2, note);
                        getParentFragmentManager()
                                .setFragmentResult(ARG_PARAM1, bundle);
                    } else {
                        NotesActivity.show(requireContext(), note);
                    }
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
}