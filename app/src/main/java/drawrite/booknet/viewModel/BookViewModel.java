package drawrite.booknet.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import drawrite.booknet.dataAccessObject.BookDao;
import drawrite.booknet.entity.Book;
import drawrite.booknet.repository.BookRepository;

public class BookViewModel extends AndroidViewModel {
    private BookRepository repository;
    private LiveData<List<Book>> allBooks;


    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        allBooks = repository.getAllBooks();
    }

    public void insert(Book book){
        repository.insert(book);
    }

    public void update(Book book){
        repository.update(book);
    }

    public void delete(Book book){
        repository.delete(book);
    }

    public void deleteAllBooks(){
        repository.deleteAllBooks();
    }

    public LiveData<List<Book>> getAllBooks() {
        return allBooks;
    }

    //public LiveData<List<String>> getBookId(String olId) { return repository.getBookId(olId); }
}
