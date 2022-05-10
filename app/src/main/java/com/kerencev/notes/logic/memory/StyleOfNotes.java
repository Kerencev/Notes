package com.kerencev.notes.logic.memory;

import android.content.Context;

import com.google.firebase.firestore.Query;
import com.kerencev.notes.R;

/**
 * Класс для хранения стилей отображения заметок
 */

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

    private static boolean isSaveNotes = true;

    public static final int COLOR_YELLOW = R.color.yellow;
    public static final int COLOR_BLUE = R.color.blue;
    public static final int COLOR_GREEN = R.color.green;
    public static final int COLOR_RED = R.color.red;

    private static Query.Direction direction = Query.Direction.DESCENDING;

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

    public boolean getIsSaveNotes() {
        return isSaveNotes;
    }

    public void setIsSaveNotes(boolean isSaveNotes) {
        StyleOfNotes.isSaveNotes = isSaveNotes;
    }

    public Query.Direction getDirection() {
        return direction;
    }

    public void setDirection(Query.Direction direction) {
        StyleOfNotes.direction = direction;
    }
}
