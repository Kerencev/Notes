package com.kerencev.notes.logic.memory;

import android.content.Context;
import android.content.SharedPreferences;

public class Data {

    private static final String KEY_SPREF_STYLE = "KEY_SPREF_STYLE";
    private static final String KEY_SPREF_HAS_DATE = "KEY_SPREF_HAS_DATE";

    public static final String KEY_RESULT_CHANGE_RECYCLER = "KEY_RESULT_CHANGE_RECYCLER";

    public static final String KEY_BUNDLE_ADD_NEW_NOTE = "KEY_BUNDLE_ADD_NEW_NOTE";
    public static final String KEY_BUNDLE_DELETE_NOTE = "KEY_BUNDLE_DELETE_NOTE";
    public static final String KEY_BUNDLE_UPDATE_NOTE = "KEY_BUNDLE_UPDATE_NOTE";

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
}
