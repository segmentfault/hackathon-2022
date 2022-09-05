package com.example.notepad.interfaces;

import com.example.notepad.Entity.Note;

import java.util.List;

public interface SearchCallBack {
    void search(List<Note> notes);
}
