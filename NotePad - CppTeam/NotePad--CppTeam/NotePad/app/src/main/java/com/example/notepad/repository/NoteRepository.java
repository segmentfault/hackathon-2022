package com.example.notepad.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.notepad.Dao.NoteDao;
import com.example.notepad.Database.NoteDataBase;
import com.example.notepad.Entity.Note;
import com.example.notepad.interfaces.SearchCallBack;

import java.util.List;

public class NoteRepository {

    private LiveData<List<Note>> mAllNotes;
    private NoteDao mNoteDao;

    public NoteRepository(Context context) {
        NoteDataBase noteDataBase = NoteDataBase.getInstance(context.getApplicationContext());
        mNoteDao = noteDataBase.getNoteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void insertNote(Note... notes){
        new InsertAsyncTask(mNoteDao).execute(notes);
    }

    public void updateNote(Note... notes){
        new UpdateAsyncTask(mNoteDao).execute(notes);
    }

    public void deleteNote(Note... notes){
        new DeleteAsyncTask(mNoteDao).execute(notes);
    }

    public void queryByCondition(SearchCallBack searchCallBack, String... condition){
        new QueryByConditionAsyncTask(mNoteDao,searchCallBack).execute(condition);
    }

    static class InsertAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDao mNoteDao;

        public InsertAsyncTask(NoteDao noteDao) {
            mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.insertNote(notes);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDao mNoteDao;

        public UpdateAsyncTask(NoteDao noteDao) {
            mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.updateNote(notes);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Note,Void,Void>{

        private NoteDao mNoteDao;

        public DeleteAsyncTask(NoteDao noteDao) {
            mNoteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.deleteNote(notes);
            return null;
        }
    }

    class QueryByConditionAsyncTask extends AsyncTask<String,Void,List<Note>>{

        private NoteDao mNoteDao;
        private SearchCallBack mSearchCallBack;

        public QueryByConditionAsyncTask(NoteDao noteDao,SearchCallBack searchCallBack) {
            mNoteDao = noteDao;
            mSearchCallBack = searchCallBack;
        }

        @Override
        protected List<Note> doInBackground(String... condition) {
            return mNoteDao.queryByCondition(condition);
        }

        @Override
        protected void onPostExecute(List<Note> notes) {
            super.onPostExecute(notes);
            mSearchCallBack.search(notes);
        }
    }

}
