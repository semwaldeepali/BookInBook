package drawrite.booknet.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import drawrite.booknet.entity.Book;
import drawrite.booknet.dataAccess.BookDao;

@Database(entities = {Book.class}, version = 1)
public abstract class BookDatabase extends RoomDatabase {
    private static BookDatabase INSTANCE;
    public abstract BookDao bookDao();

    public static BookDatabase getBookDatabase(Context context){
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), BookDatabase.class, "book-database").build();
        }
        return INSTANCE;


    }

    public static void destryInstance(){
        INSTANCE = null;
    }
}
