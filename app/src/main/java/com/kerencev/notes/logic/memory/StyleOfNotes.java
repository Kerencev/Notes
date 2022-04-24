package com.kerencev.notes.logic.memory;

import android.content.Context;

public class StyleOfNotes {

    private static StyleOfNotes INSTANCE;

    private Context context;

    private StyleOfNotes(Context context) {
        this.context = context;
    }

    public static StyleOfNotes getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new StyleOfNotes(context);
        }
        return INSTANCE;
    }

    private static String style = "STYLE_1";
    public static final String STYLE_1 = "STYLE_1";
    public static final String STYLE_2 = "STYLE_2";

    public void setStyle(String st) {
        style = st;
    }

    public String getStyle() {
        return style;
    }
}
