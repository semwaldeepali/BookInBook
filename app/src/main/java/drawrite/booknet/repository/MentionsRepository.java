package drawrite.booknet.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

import drawrite.booknet.dataAccessObject.MentionsDao;

import drawrite.booknet.database.MentionsDatabase;
import drawrite.booknet.entity.Mentions;

public class MentionsRepository {

    private MentionsDao mentionsDao;

    private LiveData<List<Mentions>> allMentions;

    public MentionsRepository(Application application){
        MentionsDatabase database = MentionsDatabase.getInstance(application);
        mentionsDao = database.mentionsDao();
        allMentions = mentionsDao.getAllMentions();
    }

    public void insert(Mentions mentions){
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
