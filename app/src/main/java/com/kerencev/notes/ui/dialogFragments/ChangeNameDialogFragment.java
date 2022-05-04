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

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.ui.NotesDescriptionFragment;
import com.kerencev.notes.ui.NotesFragment;

public class ChangeNameDialogFragment extends DialogFragment {

    public static ChangeNameDialogFragment newInstance(Note note) {
        Bundle args = new Bundle();
//        args.putParcelable(NotesFragment.ARG_NOTE, note);
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
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, NotesDescriptionFragment.newInstance(note, newName.getText().toString()))
                        .commit();
                dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(customDialog);
        return builder.create();
    }
}
