package com.kerencev.notes.logic.memory;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.firestore.Query;

/**
 * Класс для загрузки и сохранения выбранного отображения заметок
 */

public class Data {

    private static final String KEY_SPREF_STYLE = "KEY_SPREF_STYLE";
    private static final String KEY_SPREF_HAS_DATE = "KEY_SPREF_HAS_DATE";

    private static final String KEY_SPREF_SAVE_NOTES = "KEY_SPREF_SAVE_NOTES";

    private static final String KEY_SPREF_DIRECTION_FOR_SORT = "KEY_SPREF_DIRECTION_FOR_SORT";

    public static final String KEY_RESULT_CHANGE_RECYCLER = "KEY_RESULT_CHANGE_RECYCLER";

    public static final String KEY_RESULT_CHANGE_RECYCLER_FOR_TRASH = "KEY_RESULT_CHANGE_RECYCLER_FOR_TRASH";

    public static final String KEY_BUNDLE_ADD_NEW_NOTE = "KEY_BUNDLE_ADD_NEW_NOTE";
    public static final String KEY_BUNDLE_DELETE_NOTE = "KEY_BUNDLE_DELETE_NOTE";
    public static final String KEY_BUNDLE_UPDATE_NOTE = "KEY_BUNDLE_UPDATE_NOTE";
    public static final String KEY_BUNDLE_SHOW_FOR_SELECT = "KEY_BUNDLE_SHOW_FOR_SELECT";
    public static final String KEY_BUNDLE_FIX_NOTE = "KEY_BUNDLE_FIX_NOTE";

    public static final String KEY_BUNDLE_DELETE_NOTE_FROM_TRASH = "KEY_BUNDLE_DELETE_NOTE_FROM_TRASH";

    public static final String KEY_BUNDLE_SHOW_BOTTOM_BAR = "KEY_BUNDLE_SHOW_BOTTOM_BAR";

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

    public static void saveIsHasDate(SharedPreferences sharedPreferences, boolean isHasDate) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SPREF_HAS_DATE, isHasDate);
        editor.apply();
    }

    public static void loadIsHasDate(SharedPreferences sharedPreferences, Context context) {
        if (sharedPreferences.contains(KEY_SPREF_HAS_DATE)) {
            StyleOfNotes.getINSTANCE(context).setIsHasDate(sharedPreferences.getBoolean(KEY_SPREF_HAS_DATE, true));
        }
    }

    public static void saveIsSaveNotes(SharedPreferences sharedPreferences, boolean isSaveNotes) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_SPREF_SAVE_NOTES, isSaveNotes);
        editor.apply();
    }

    public static void loadIsSaveNotes(SharedPreferences sharedPreferences, Context context) {
        if (sharedPreferences.contains(KEY_SPREF_SAVE_NOTES)) {
            StyleOfNotes.getINSTANCE(context).setIsSaveNotes(sharedPreferences.getBoolean(KEY_SPREF_SAVE_NOTES, true));
        }
    }

    public static void saveDirection(SharedPreferences sharedPreferences, Query.Direction direction, Context context) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (StyleOfNotes.getINSTANCE(context).getDirection().equals(Query.Direction.ASCENDING)) {
            editor.putBoolean(KEY_SPREF_DIRECTION_FOR_SORT, false);
        } else {
            editor.putBoolean(KEY_SPREF_DIRECTION_FOR_SORT, true);
        }

        editor.apply();
    }

    public static void loadDirection(SharedPreferences sharedPreferences, Context context) {
        if (sharedPreferences.contains(KEY_SPREF_DIRECTION_FOR_SORT)) {
            if (sharedPreferences.getBoolean(KEY_SPREF_DIRECTION_FOR_SORT, true)) {
                StyleOfNotes.getINSTANCE(context).setDirection(Query.Direction.DESCENDING);
            } else {
                StyleOfNotes.getINSTANCE(context).setDirection(Query.Direction.ASCENDING);
            }
        }
    }
}
