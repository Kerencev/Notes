package com.kerencev.notes.logic.memory;

import android.content.Context;

import com.kerencev.notes.R;

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
    private static boolean isHasDate = true;
    public static final String STYLE_1 = "STYLE_1";
    public static final String STYLE_2 = "STYLE_2";

    public static final int COLOR_YELLOW = R.color.yellow;
    public static final int COLOR_BLUE = R.color.blue;
    public static final int COLOR_GREEN = R.color.green;
    public static final int COLOR_RED = R.color.red;

    public void setStyle(String st) {
        style = st;
    }

    public String getStyle() {
        return style;
    }

    public boolean isIsHasDate() {
        return isHasDate;
    }

    public void setIsHasDate(boolean isHasDate) {
        StyleOfNotes.isHasDate = isHasDate;
    }
}
