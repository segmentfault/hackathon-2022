package com.example.notepad.interfaces;

public interface DeleteButtonCallBack {
    void deleteButtonAppear();
    void deleteButtonDisappear();
    void deleteNotes(int... ids);
    void editable();
}
