package com.kerencev.notes.ui.dialogFragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.kerencev.notes.R;
import com.kerencev.notes.logic.Callback;
import com.kerencev.notes.logic.Note;
import com.kerencev.notes.logic.memory.Data;
import com.kerencev.notes.logic.memory.Dependencies;
import com.kerencev.notes.ui.NotesFragment;

/**
 * Диалог появляется при нажатии на заметку и узнает восстанавливаем или удаляем заметку
 */

public class TrashDialogFragment extends DialogFragment {

    public static TrashDialogFragment newInstance(Note note) {
        Bundle args = new Bundle();
        args.putParcelable(NotesFragment.ARG_NOTE, note);
        TrashDialogFragment fragment = new TrashDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Note note = getArguments().getParcelable(NotesFragment.ARG_NOTE);
        View customDialog = getLayoutInflater().inflate(R.layout.dialog_fragment_trash, null);
        TextView name = customDialog.findViewById(R.id.name_note);
        name.setText(note.getName());

        customDialog.findViewById(R.id.action_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dependencies.getNotesTrashRepository().deleteNote(note);

                Bundle bundle = new Bundle();
                bundle.putParcelable(Data.KEY_BUNDLE_DELETE_NOTE_FROM_TRASH, note);

                getParentFragmentManager().setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER_FOR_TRASH, bundle);
                dismiss();
            }
        });

        customDialog.findViewById(R.id.action_restore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dependencies.getNotesRepository().addNote(note.getName(), note.getDescription(), note.getColor(), note.getDateForSort(), note.getDate(), new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {

                    }

                    @Override
                    public void onError(Throwable exception) {

                    }
                });

                Dependencies.getNotesTrashRepository().deleteNote(note);

                Bundle bundle = new Bundle();
                bundle.putParcelable(Data.KEY_BUNDLE_DELETE_NOTE_FROM_TRASH, note);

                getParentFragmentManager().setFragmentResult(Data.KEY_RESULT_CHANGE_RECYCLER_FOR_TRASH, bundle);
                dismiss();

                dismiss();
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setView(customDialog);
        return builder.create();
    }
}
