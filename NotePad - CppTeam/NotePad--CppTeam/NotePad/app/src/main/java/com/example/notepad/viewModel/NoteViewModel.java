package com.example.notepad.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.notepad.Entity.Note;
import com.example.notepad.interfaces.SearchCallBack;
import com.example.notepad.repository.NoteRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mNoteRepository;
    private MutableLiveData<Boolean> checkBoxAllAppear;

    public MutableLiveData<Boolean> getCheckBoxAllAppear() {
        if (checkBoxAllAppear==null){
            checkBoxAllAppear = new MutableLiveData<>();
            checkBoxAllAppear.setValue(false);
        }
        return checkBoxAllAppear;
    }

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mNoteRepository = new NoteRepository(application);
    }

    public LiveData<List<Note>> getAllNotes() {
        return mNoteRepository.getAllNotes();
    }

    public void insertNote(Note... notes){
        mNoteRepository.insertNote(notes);
    }

    public void updateNote(Note... notes){
        mNoteRepository.updateNote(notes);
    }

    public void deleteNote(Note... notes){
        mNoteRepository.deleteNote(notes);
    }

    public void queryByCondition(SearchCallBack searchCallBack, String... condition){
        mNoteRepository.queryByCondition(searchCallBack,condition);
    }
}
