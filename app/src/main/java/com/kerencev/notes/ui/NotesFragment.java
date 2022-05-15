package com.kerencev.notes.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;

import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Keyboard;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.logic.MyDate;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.NoteName;
import com.kerencev.notes.logic.memory.StyleOfNotes;

import java.util.Date;

/**
 * Фрагмент для отрисовки выбранной заметки
 */

public class NotesFragment extends Fragment {

    public static final String ARG_NOTE = "ARG_NOTE";

    private TextView title;
    private EditText text;
    private Toolbar toolbar;
    private Note note;

    private Toolbar bottomToolbar;

    private ConstraintLayout constraintLayout;

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
        bottomToolbar = view.findViewById(R.id.bottom_toolbar);
        constraintLayout = view.findViewById(R.id.container3);

        setClicksToolbar();

        setClicksBottomToolbar(view);

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {
            note = getArguments().getParcelable(ARG_NOTE);
            showNote(note);
        }

        if (note.getDescription() == null) {
            Keyboard.showKeyBoard(requireActivity(), text);
        }

        setRightColor(view);
    }

    /**
     * Устанавливаем правильный цвет фона
     */
    private void setRightColor(View view) {
        switch (note.getColor()) {
            case R.color.yellow:
                constraintLayout.setBackgroundResource(R.color.yellow);
                break;

            case R.color.blue:
                constraintLayout.setBackgroundResource(R.color.blue);
                break;

            case R.color.green:
                constraintLayout.setBackgroundResource(R.color.green);
                break;

            case R.color.red:
                constraintLayout.setBackgroundResource(R.color.red);
                break;
        }
    }

    private void setClicksToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Keyboard.hideKeyBoard(requireActivity(), text);
                getParentFragmentManager().popBackStack();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_save:
                        //Проверка является ли заметка новой или уже имеющейся
                        if (note.getDescription() != null) {
                            updateNote();
                        } else {
                            saveNewNote();
                        }

                        Keyboard.hideKeyBoard(requireActivity(), text);
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

                            //Проверяем нужно ли сохранять в корзину
                            if (StyleOfNotes.getINSTANCE(requireContext()).getIsSaveNotes()) {
                                Dependencies.getNotesTrashRepository().addNote(note);
                            }
                        }

                        getParentFragmentManager().popBackStack();
                        Keyboard.hideKeyBoard(requireActivity(), text);
                        return true;
                }
                return false;
            }
        });
    }

    /**
     * Метод для установки слушателя при нажатии на изменение цвета
     *
     * @param view
     */
    private void setClicksBottomToolbar(View view) {

        bottomToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.action_yellow:
                        view.findViewById(R.id.container3).setBackgroundResource(R.color.yellow);
                        note.setColor(StyleOfNotes.COLOR_YELLOW);
                        return true;

                    case R.id.action_blue:
                        view.findViewById(R.id.container3).setBackgroundResource(R.color.blue);
                        note.setColor(StyleOfNotes.COLOR_BLUE);
                        return true;

                    case R.id.action_green:
                        view.findViewById(R.id.container3).setBackgroundResource(R.color.green);
                        note.setColor(StyleOfNotes.COLOR_GREEN);
                        return true;

                    case R.id.action_red:
                        view.findViewById(R.id.container3).setBackgroundResource(R.color.red);
                        note.setColor(StyleOfNotes.COLOR_RED);
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

    private void updateNote() {

        FragmentManager fragmentManager = getParentFragmentManager();

        Dependencies.getNotesRepository().updateNote(note, note.getName(), text.getText().toString(), new Callback<Note>() {
            @Override
            public void onSuccess(Note data) {
                note.setDescription(text.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putParcelable(Data.KEY_BUNDLE_UPDATE_NOTE, note);

                fragmentManager.setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
            }

            @Override
            public void onError(Throwable exception) {

            }
        });
    }

    private void saveNewNote() {

        FragmentManager fragmentManager = getParentFragmentManager();

        if (title.getText().length() == 0 && text.getText().length() == 0) {
            fragmentManager.popBackStack();
            return;
        }

        Dependencies.getNotesRepository().addNote(title.getText().toString(), text.getText().toString(), note.getColor(), new Date(),
                MyDate.getDate(),
                new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {
                        String id = data.getId();

                        note.setID(id);

                        if (title.getText().length() == 0) {
                            note.setName(NoteName.setDefaultName(text.getText().toString()));
                        } else {
                            note.setName(title.getText().toString());
                        }

                        note.setDescription(text.getText().toString());

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Data.KEY_BUNDLE_ADD_NEW_NOTE, note);

                        fragmentManager.setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });
    }

}