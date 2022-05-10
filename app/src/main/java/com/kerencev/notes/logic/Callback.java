package com.kerencev.notes.logic;

public interface Callback<T> {

    void onSuccess(T data);

    void onError(Throwable exception);
}
