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
import drawrite.booknet.database.BookNetDatabase;
import drawrite.booknet.entity.Book;

public class BookRepository {

    private BookDao bookDao;

    private LiveData<List<Book>> allBooks;

    private LiveData<List<Book>> personalShelfBooks;

    public BookRepository(Application application){
        BookNetDatabase database = BookNetDatabase.getInstance(application);
        bookDao = database.bookDao();
        allBooks = bookDao.getAllBooks();
        personalShelfBooks = bookDao.getPersonalShelf();
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
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
        new DeleteAllBookAsyncTask(bookDao).execute();
    }

    public void deleteBookByPID(Integer primaryID) {
        new DeleteBookByBPID(bookDao).execute(primaryID);
    }

    public void addToPersonalShelf(Integer primaryId) {
        new AddToPersonalShelfAsyncTask(bookDao).execute(primaryId);
    }

    public void removeFromPersonalShelf(Integer primaryId) {
        new RemoveFromPersonalShelfAsyncTask(bookDao).execute(primaryId);
    }

    public LiveData<List<Book>> getBookByPID(Integer mainBookPrimaryId){
        return  bookDao.getBookByPID(mainBookPrimaryId);
    }

    public LiveData<List<Book>> getBookByTitle(String bookTitle){
        return bookDao.getBookByTitle(bookTitle);
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    public LiveData<List<Book>> getPersonalShelfBooks() {
        return personalShelfBooks;
    }

    //Async task to update the personal shelf
    private static class AddToPersonalShelfAsyncTask extends AsyncTask<Integer, Void, Void> {
        private BookDao bookDao;

        private AddToPersonalShelfAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Integer... primaryIds) {
            bookDao.addToPersonalShelf(primaryIds[0]);
            return null;
        }
    }

    private static class RemoveFromPersonalShelfAsyncTask extends AsyncTask<Integer, Void, Void> {
        private BookDao bookDao;

        private RemoveFromPersonalShelfAsyncTask(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Integer... primaryIds) {
            bookDao.removeFromPersonalShelf(primaryIds[0]);
            return null;
        }
    }

    public List<Boolean> isBookInPersonalShelfByPID(final Integer primaryID) throws ExecutionException, InterruptedException {

        Callable<List<Boolean>> callable = new Callable<List<Boolean>>() {
            @Override
            public List<Boolean> call() throws Exception {
                return bookDao.isBookInPersonalShelfByPID(primaryID);
            }
        };

        Future<List<Boolean>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }

    // Get list of books based on primary id


    // Livedata to reflect any change in db ;
    // [Not working]Doing this as we cannot return result from asyncTask thread to main thread
    // Approach 2 : declaring an async response interface Source :
    // Approach 3 : Using Callable and Futures ; Source https://stackoverflow.com/questions/51013167/room-cannot-access-database-on-the-main-thread-since-it-may-potentially-lock-th

    public List<Integer> getPIdByOLId(final String olId) throws ExecutionException, InterruptedException {

        Callable<List<Integer>> callable = new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return bookDao.getPIdByOLId(olId);
            }
        };

        Future<List<Integer>> future = Executors.newSingleThreadExecutor().submit(callable);

        return future.get();
    }




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

    private static class DeleteBookByBPID extends AsyncTask<Integer, Void, Void> {
        private BookDao bookDao;

        private DeleteBookByBPID(BookDao bookDao) {
            this.bookDao = bookDao;
        }

        @Override
        protected Void doInBackground(Integer... primaryIDs) {
            bookDao.deleteBookByPID(primaryIDs[0]);
            return null;
        }
    }
}
