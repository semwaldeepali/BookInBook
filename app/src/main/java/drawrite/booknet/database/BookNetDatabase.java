//room database component

package drawrite.booknet.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import drawrite.booknet.dataAccess.BookNetDao;
import drawrite.booknet.entity.Book;

@Database(entities = {Book.class}, version = 1)

public abstract class BookNetDatabase extends RoomDatabase {

    private static BookNetDatabase INSTANCE;

    //abstract method to return data access object
    public abstract BookNetDao bookNetDaoDao();

    public static BookNetDatabase getBookDatabase(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BookNetDatabase.class, "book-database").build();
        }
        return INSTANCE;


    }

    public static void destryInstance(){
        INSTANCE = null;
    }
}
