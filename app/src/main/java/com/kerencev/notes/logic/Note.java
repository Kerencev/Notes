package com.kerencev.notes.logic;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable {

    private String name;
    private String Description;
    private String date;

    public Note(String name, String description, String date) {
        this.name = name;
        Description = description;
        this.date = date;
    }

    protected Note(Parcel in) {
        name = in.readString();
        Description = in.readString();
        date = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(Description);
        dest.writeString(date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
