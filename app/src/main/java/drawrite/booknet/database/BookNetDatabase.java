//room database component connecting entity and dao

package drawrite.booknet.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import drawrite.booknet.dataAccessObject.BookDao;
import drawrite.booknet.dataAccessObject.MentionsDao;
import drawrite.booknet.entity.Book;
import drawrite.booknet.entity.Mentions;
import io.reactivex.annotations.NonNull;

@Database(entities = {Mentions.class,Book.class}, version = 1)

public abstract class BookNetDatabase extends RoomDatabase {

    // singleton
    private static BookNetDatabase INSTANCE;

    //abstract method to return data access object
    public abstract MentionsDao mentionsDao();
    public abstract BookDao bookDao();

    //NOTE: singleton hence static function;
    //synchronized so that only one thread can
    //access the database at one time.

    public static synchronized BookNetDatabase getInstance(Context context){

        if(INSTANCE == null) { // if no instance
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    BookNetDatabase.class, "booknet_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return INSTANCE;


    }

    //TODO : Learn more about callback
    public static Callback roomCallback = new Callback(){
      @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
          super.onCreate(db);
          new PopulateDbAsyncTask(INSTANCE).execute();
      }

    };

    // Populating the dummy values initially. Called only once
    // when the instance of db is first created
    public static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private MentionsDao mentionsDao;
        private BookDao bookDao;

        private PopulateDbAsyncTask(BookNetDatabase db){

            mentionsDao = db.mentionsDao();
            bookDao = db.bookDao();
        }

        @Override
        protected  Void doInBackground(Void... voids){
            //TODO : add more basic data
            bookDao.insertBook(new Book("olid 1","grid 1",
                    "title 1","sub title 1","author 1",
                    "publisher 1","publishyear 1",
                    "nrPages 1" ));

            bookDao.insertBook(new Book("olid 2","grid 2",
                    "title 2","sub title 2","author 2",
                    "publisher 2","publishyear 2",
                    "nrPages 2" ));


            bookDao.insertBook(new Book("olid 3","grid 3",
                    "title 3","sub title 3","author 3",
                    "publisher 3","publish year 3",
                    "nr Pages 3" ));
            mentionsDao.insertMentions(new Mentions(1,2 ));
            return null;
        }

    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
