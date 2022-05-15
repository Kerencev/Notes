package com.kerencev.notes.ui.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Keyboard;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.ui.NotesDescriptionFragment;
import com.kerencev.notes.ui.NotesFragment;

/**
 * Диалог появляется, если нажата кнопка сменить имя
 */

public class ChangeNameDialogFragment extends DialogFragment {

    public static ChangeNameDialogFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(NotesFragment.ARG_NOTE, note);
        ChangeNameDialogFragment fragment = new ChangeNameDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Note note = getArguments().getParcelable(NotesFragment.ARG_NOTE);
        View customDialog = getLayoutInflater().inflate(R.layout.dialog_change_name, null);
        EditText newName = customDialog.findViewById(R.id.edit);
        newName.setText(note.getName());

        customDialog.findViewById(R.id.action_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        customDialog.findViewById(R.id.action_confirm).setOnClickListener(new View.OnClickListener() {

            FragmentManager fragmentManager = getParentFragmentManager();

            @Override
            public void onClick(View view) {

                String name = note.getName();

                if (newName.getText().length() > 0) {
                    name = newName.getText().toString();
                }

                String finalName = name;
                Dependencies.getNotesRepository().updateNote(note, finalName, note.getDescription(), new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {
                        note.setName(finalName);

                        Bundle bundle = new Bundle();
                        bundle.putParcelable(Data.KEY_BUNDLE_UPDATE_NOTE, note);

                        fragmentManager.setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER, bundle);
                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(customDialog);
        return builder.create();
    }
}
