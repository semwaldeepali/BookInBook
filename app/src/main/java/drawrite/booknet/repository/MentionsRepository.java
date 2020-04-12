package drawrite.booknet.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import drawrite.booknet.dataAccessObject.MentionsDao;

import drawrite.booknet.database.BookNetDatabase;
import drawrite.booknet.entity.Mentions;

public class MentionsRepository {

    private MentionsDao mentionsDao;

    private LiveData<List<Mentions>> allMentions;

    public MentionsRepository(Application application){
        BookNetDatabase database = BookNetDatabase.getInstance(application);
        mentionsDao = database.mentionsDao();
        allMentions = mentionsDao.getAllMentions();
    }

    // Approach 3 : Using Callable and Futures ; Source https://stackoverflow.com/questions/51013167/room-cannot-access-database-on-the-main-thread-since-it-may-potentially-lock-th

    public List<Integer> getMentionsByBookIds(final Integer mainBookId) throws ExecutionException, InterruptedException {

        Callable<List<Integer>> callable = new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return mentionsDao.getMentionsByBookIds(mainBookId);
            }
        };

        Future<List<Integer>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }


    public List<Integer> getMainByBookIds(final Integer mentionsBookId) throws ExecutionException, InterruptedException {

        Callable<List<Integer>> callable = new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return mentionsDao.getMainByBookIds(mentionsBookId);
            }
        };

        Future<List<Integer>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    public void insert(Mentions mentions){
        Log.d("MentionsRepository", " 1103 insert being executed: main " + mentions.getMainId() +" or mentioned : "+mentions.getMentionsId());

        new InsertMentionsAsyncTask(mentionsDao).execute(mentions);

    }
    public void update(Mentions mentions){
        new UpdateMentionsAsyncTask(mentionsDao).execute(mentions);
    }
    public void delete(Mentions mentions){
        new DeleteMentionsAsyncTask(mentionsDao).execute(mentions);
    }
    public void deleteAllMentions(){
        new DeleteMentionsAsyncTask(mentionsDao).execute();
    }

    public LiveData<List<Mentions>> getAllMentions() {
        return allMentions;
    }

    private static class InsertMentionsAsyncTask extends AsyncTask<Mentions, Void, Void>{
        private MentionsDao mentionsDao;

        private InsertMentionsAsyncTask(MentionsDao mentionsDao){
            this.mentionsDao = mentionsDao;
        }
        @Override
        protected Void doInBackground(Mentions... mentions){
            Log.d("MentionsRepository", " 1103 insert mention async task being executed: main " + mentions[0].getMainId() +" or mentioned : "+mentions[0].getMentionsId());

            mentionsDao.insertMentions(mentions[0]);
            return null;
        }
    }

    private static class UpdateMentionsAsyncTask extends AsyncTask<Mentions, Void, Void>{
        private MentionsDao mentionsDao;

        private UpdateMentionsAsyncTask(MentionsDao mentionsDao){
            this.mentionsDao = mentionsDao;
        }
        @Override
        protected Void doInBackground(Mentions... mentions){
            mentionsDao.updateMentions(mentions[0]);
            return null;
        }
    }

    private static class DeleteMentionsAsyncTask extends AsyncTask<Mentions, Void, Void>{
        private MentionsDao mentionsDao;

        private DeleteMentionsAsyncTask(MentionsDao mentionsDao){
            this.mentionsDao = mentionsDao;
        }
        @Override
        protected Void doInBackground(Mentions... mentions){
            mentionsDao.deleteMentions(mentions[0]);
            return null;
        }
    }

    private static class DeleteAllMentionsAsyncTask extends AsyncTask<Mentions, Void, Void>{
        private MentionsDao mentionsDao;

        private DeleteAllMentionsAsyncTask(MentionsDao mentionsDao){
            this.mentionsDao = mentionsDao;
        }

        @Override
        protected Void doInBackground(Mentions... mentions) {
            mentionsDao.deleteAllMentions();
            return null;
        }
    }
}
