//room database component connecting entity and dao

package drawrite.booknet.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import drawrite.booknet.dataAccessObject.BookDao;
import drawrite.booknet.entity.Book;

@Database(entities = {Book.class}, version = 1)

public abstract class BookDatabase extends RoomDatabase {

    // singleton
    private static BookDatabase INSTANCE;

    //abstract method to return data access object
    public abstract BookDao bookDao();

    //NOTE: singleton hence static function;
    //synchronized so that only one thread can
    //access the database at one time.

    public static synchronized BookDatabase getInstance(Context context){

        if(INSTANCE == null) { // if no instance
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    BookDatabase.class, "book_database").build();
        }
        return INSTANCE;


    }

    public static void destroyInstance(){
        INSTANCE = null;
    }
}
