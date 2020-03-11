package drawrite.booknet.repository;

// Resource : https://google-developer-training.github.io/android-developer-fundamentals-course-concepts-v2/unit-4-saving-user-data/lesson-10-storing-data-with-room/10-1-c-room-livedata-viewmodel/10-1-c-room-livedata-viewmodel.html
// Repository is an interface of data with the app, Combining multiple data sources.
import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import drawrite.booknet.dataAccessObject.BookDao;
import drawrite.booknet.database.BookDatabase;
import drawrite.booknet.entity.Book;

public class BookRepository {

    private BookDao bookDao;

    private LiveData<List<Book>> allBooks;

    public BookRepository(Application application){
        BookDatabase database = BookDatabase.getInstance(application);
        bookDao = database.bookDao();
        allBooks = bookDao.getAllBooks();
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insert(Book book){
        new InsertBookAsyncTask(bookDao).execute(book);

    }
    public void update(Book book){
        new UpdateBookAsyncTask(bookDao).execute(book);
    }
    public void delete(Book book){
        new DeleteBookAsyncTask(bookDao).execute(book);
    }
    public void deleteAllBooks(){
        new DeleteBookAsyncTask(bookDao).execute();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    // Livedata to reflect any change in db ;
    // [Not working]Doing this as we cannot return result from asyncTask thread to main thread
    // Approach 2 : declaring an async response interface Source :
    // Approach 3 : Using Callable and Futures ; Source https://stackoverflow.com/questions/51013167/room-cannot-access-database-on-the-main-thread-since-it-may-potentially-lock-th

    public List<Integer> getBookId(final String olId) throws ExecutionException, InterruptedException {

        Callable<List<Integer>> callable = new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return bookDao.getBookId(olId);
            }
        };

        Future<List<Integer>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    /*private static class GetBookIdAsyncTask extends AsyncTask<String, Void, List<String>>{
        private BookDao bookDao;

        public interface AsyncResponse {
            void processFinish(List<String> output);
        }

        public AsyncResponse delegate = null;

        public GetBookIdAsyncTask(AsyncResponse delegate){
            this.delegate = delegate;
        }

        private GetBookIdAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected List<String> doInBackground(String... olId){

            return bookDao.getBookId(olId[0]);
        }

        // for asyncResponse handling back to main UI
        @Override
        protected void onPostExecute(List<String> result) {
            delegate.processFinish(result);
        }
    }
 */
    private static class InsertBookAsyncTask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private InsertBookAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books){
            bookDao.insertBook(books[0]);
            return null;
        }
    }

    private static class UpdateBookAsyncTask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private UpdateBookAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books){
            bookDao.updateBook(books[0]);
            return null;
        }
    }

    private static class DeleteBookAsyncTask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private DeleteBookAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }
        @Override
        protected Void doInBackground(Book... books){
            bookDao.deleteBook(books[0]);
            return null;
        }
    }

    private static class DeleteAllBookAsyncTask extends AsyncTask<Book, Void, Void>{
        private BookDao bookDao;

        private DeleteAllBookAsyncTask(BookDao bookDao){
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Book... books) {
            bookDao.deleteAllBooks();
            return null;
        }
    }
}
