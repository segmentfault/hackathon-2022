package com.example.smartos;

import android.app.Application;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import java.util.List;

public class NoteViewModel extends AndroidViewModel {
    private NoteDao noteDao;
    private groupDao groupDao;
    private NoteRepository noteRepository;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        NotesDatabase notesDatabase = NotesDatabase.getDatebase(application);
        groupDao= notesDatabase.getGroupDao();
        noteDao = notesDatabase.getNoteDao();
        noteRepository = new NoteRepository(application);
    }
    public String getNotesbyID(int ID){
        return noteRepository.getNotesbyID(ID);
    }
    public String getGroupsbyID(int ID){
        return noteRepository.getGroupsbyID(ID);
    }
    public String getDatabyID(int ID){
        return noteRepository.getDatasbyID(ID);
    }
    public LiveData<List<Notes>> getAllNotesLive(){
        return noteRepository.getAllNotesLive();
    }
    public LiveData<List<Notes>> findNotesByPatten(String patten){
        return noteRepository.findNotesByPatten(patten);
    }
    public LiveData<List<Notes>> findNotesByGroup(String patten){
        return noteRepository.findNotesByGroup(patten);
    }
    public LiveData<List<Notes>>findNotesInDelete(){
        return noteDao.findNotesInDelete();
    };
    public List<Integer> getDeleteID(){return noteDao.getDeleteID();}
    public Notes findAllNotesByID(int op){return noteRepository.findAllNotesByID(op);}
    public String getGroupNameById(int ID){return noteRepository.getGroupNameById(ID);}
    public LiveData<List<groupClass>>getAllGroupName(){return  noteRepository.getAllGroupName();}
    void insertNotes(Notes...notes){
        new NoteRepository.InsertAsyncTask(noteDao).execute(notes);
    }
    void updatetNotes(Notes...notes){
        new NoteRepository.UpdateAsyncTask(noteDao).execute(notes);
    }
    void deleteNotes(Notes...notes){
        new NoteRepository.DeleteAsyncTask(noteDao).execute(notes);
    }

    void insertGroup(groupClass...groupClasses){new NoteRepository.InsertAsyncTaskForGroup(groupDao).execute(groupClasses);}
    void updateGroup(groupClass...groupClasses){new NoteRepository.UpdateAsyncTaskForGroup(groupDao).execute(groupClasses);}
    void deleteGroup(groupClass...groupClasses){new NoteRepository.DeleteAsyncTaskForGroup(groupDao).execute(groupClasses);}


}
