package com.kerencev.notes.logic;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.card.MaterialCardView;
import com.kerencev.notes.R;
import com.kerencev.notes.ui.NotesAdapter;
import com.kerencev.notes.ui.NotesDescriptionFragment;

import java.util.ArrayList;
import java.util.List;

public class Search {

    private final FragmentManager fragmentManager;
    private final NotesAdapter adapter;
    private final AppCompatEditText editSearch;
    private final MaterialCardView ok;

    private final List<Note> actualData;

    private final Activity activity;


    public Search(Activity activity, FragmentManager fragmentManager, View view, NotesAdapter adapter) {
        this.activity = activity;
        this.fragmentManager = fragmentManager;
        this.adapter = adapter;
        this.editSearch = view.findViewById(R.id.search);
        this.ok = view.findViewById(R.id.action_confirm_search);

        this.actualData = adapter.getData();
    }

    public void initSearch() {

        Keyboard.showKeyBoard(activity, editSearch);

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                ArrayList<Note> filterList = new ArrayList<>();

                for (Note note : actualData) {
                    if (note.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterList.add(note);
                    }
                }

                adapter.filter((ArrayList<Note>) filterList);
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Keyboard.hideKeyBoard(activity, editSearch);

                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, new NotesDescriptionFragment())
                        .commit();
            }
        });
    }
}
