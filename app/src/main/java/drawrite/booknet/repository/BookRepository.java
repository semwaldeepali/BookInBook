package drawrite.booknet.repository;

import android.app.Application;

import java.util.List;

import drawrite.booknet.dataAccess.BookDao;
import drawrite.booknet.database.BookDatabase;
import drawrite.booknet.entity.Book;

public class BookRepository {
    private BookDao mBookDao;
    private List<Book> mAllBook;

    BookRepository(Application application){
        BookDatabase db = BookDatabase.getBookDatabase(application);
        mBookDao = db.bookDao();
        mAllBook = mBookDao.getAllBooks();
    }


}
