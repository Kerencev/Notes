package com.kerencev.notes.logic.memory;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kerencev.notes.logic.Note;

import java.util.List;

public class Data {

    private static final String KEY_SPREF = "KEY_SPREF";
    private static final String KEY_SPREF_STYLE = "KEY_SPREF_STYLE";

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

    public static void saveStyle(SharedPreferences sharedPreferences, String style) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_SPREF_STYLE, style);
        editor.apply();
    }

    public static void loadStyle(SharedPreferences sharedPreferences, Context context) {
        if (sharedPreferences.contains(KEY_SPREF_STYLE)) {
            StyleOfNotes.getINSTANCE(context).setStyle(sharedPreferences.getString(KEY_SPREF_STYLE, StyleOfNotes.STYLE_1));
        }
    }
}
