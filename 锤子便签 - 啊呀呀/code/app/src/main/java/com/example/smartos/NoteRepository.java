package com.example.smartos;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private LiveData<List<Notes>>allNotesLive;
    private NoteDao noteDao;
    private Notes notes;
    private LiveData<List<groupClass>>allGroup;
    private groupDao groupDao;
    public NoteRepository(Context context) {

        NotesDatabase notesDatabase = NotesDatabase.getDatebase(context.getApplicationContext());
        noteDao = notesDatabase.getNoteDao();
        groupDao = notesDatabase.getGroupDao();
        allGroup = groupDao.getAllGroupName();
        allNotesLive = noteDao.getAllnotesLive();
    }
    void insertNotes(Notes...notes){
        new InsertAsyncTask(noteDao).execute(notes);
    }
    void updatetNotes(Notes...notes){
        new UpdateAsyncTask(noteDao).execute(notes);
    }
    void deleteNotes(Notes...notes){
        new DeleteAsyncTask(noteDao).execute(notes);
    }
    public LiveData<List<Notes>> getAllNotesLive(){
        return allNotesLive;
    }
    public Notes findAllNotesByID(int op){return noteDao.findAllNotesByID(op);}
    public LiveData<List<Notes>> findNotesByPatten(String patten){
        return noteDao.findNotesByPatten("%"+patten+"%");
    }
    public LiveData<List<Notes>>findNotesByGroup(String patten){
        return noteDao.findNotesByGroup("%"+patten+"%");
    };
    public List<Integer> getDeleteID(){return noteDao.getDeleteID();}
    public LiveData<List<Notes>>findNotesInDelete(){
        return noteDao.findNotesInDelete();
    };
    public String getGroupsbyID(int id){
        return noteDao.getGroupbyID(id);
    }
    public String getNotesbyID(int id){
        return noteDao.getNotesbyID(id);
    }
    public String getDatasbyID(int id){
        return noteDao.getDatabyID(id);
    }

    //表二方法
    void insertGroup(groupClass...groupClasses){new InsertAsyncTaskForGroup(groupDao).execute(groupClasses);}
    void updateGroup(groupClass...groupClasses){new UpdateAsyncTaskForGroup(groupDao).execute(groupClasses);}
    void deleteGroup(groupClass...groupClasses){new DeleteAsyncTaskForGroup(groupDao).execute(groupClasses);}
    public LiveData<List<groupClass>>getAllGroupName(){return allGroup;}
    public String getGroupNameById(int id){return groupDao.getGroupNameById(id);}
    static class InsertAsyncTaskForGroup extends AsyncTask<groupClass,Void,Void>{
        private groupDao groupDao;
        public InsertAsyncTaskForGroup(groupDao groupDao){
            this.groupDao = groupDao;
        }
        @Override
        protected Void doInBackground(groupClass... groupClasses) {
            groupDao.insertGroup(groupClasses);
            return null;
        }
    }

    static class UpdateAsyncTaskForGroup extends AsyncTask<groupClass,Void,Void>{
        private groupDao groupDao;

        public UpdateAsyncTaskForGroup(groupDao groupDao){
            this.groupDao = groupDao;
        }
        @Override
        protected Void doInBackground(groupClass... groupClasses) {
            groupDao.updateGroup(groupClasses);
            return null;
        }
    }

    static class DeleteAsyncTaskForGroup extends AsyncTask<groupClass,Void,Void>{
        private groupDao groupDao;

        public DeleteAsyncTaskForGroup(groupDao groupDao){
            this.groupDao = groupDao;
        }
        @Override
        protected Void doInBackground(groupClass...groupClasses) {
            groupDao.deleteGroup(groupClasses);
            return null;
        }
    }

    static class InsertAsyncTask extends AsyncTask<Notes,Void,Void> {
        private NoteDao noteDao;

        public InsertAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Notes... notes) {
            noteDao.insertnotes(notes);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Notes,Void,Void>{
        private NoteDao noteDao;

        public UpdateAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Notes... notes) {
            noteDao.updatenotes(notes);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Notes,Void,Void>{
        private NoteDao noteDao;

        public DeleteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Notes... notes) {
            noteDao.deletenote(notes);
            return null;
        }
    }


}
