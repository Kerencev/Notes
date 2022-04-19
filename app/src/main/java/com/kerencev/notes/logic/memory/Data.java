package com.kerencev.notes.logic.memory;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kerencev.notes.R;
import com.kerencev.notes.logic.Note;

import java.util.List;

public class Data {

    private static final String KEY_SPREF = "KEY_SPREF";

    public static void save(SharedPreferences sharedPreferences, String notesJson) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SPREF, notesJson);
        editor.apply();
    }

    public static void load(SharedPreferences sharedPreferences, Context context) {

        if (sharedPreferences.contains(KEY_SPREF)) {
            String httpParamJSONList = sharedPreferences.getString(KEY_SPREF, "");

            List<Note> notes1 =
                    new Gson().fromJson(httpParamJSONList, new TypeToken<List<Note>>() {
                    }.getType());

            InMemoryNotesRepository.getINSTANCE(context).loadAll(notes1);
        }
    }


}
