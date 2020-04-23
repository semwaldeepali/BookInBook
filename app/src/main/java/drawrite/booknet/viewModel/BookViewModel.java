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
    private LiveData<List<Book>> personalShelfBooks;


    public BookViewModel(@NonNull Application application) {
        super(application);
        repository = new BookRepository(application);
        allBooks = repository.getAllBooks();
        personalShelfBooks = repository.getPersonalShelfBooks();
    }

    public void insert(Book book){
        repository.insert(book);
    }

    public void update(Book book){
        repository.update(book);
    }

    public void addToPersonalShelf(Integer primaryId) {
        repository.addToPersonalShelf(primaryId);
    }

    public void removeFromPersonalShelf(Integer primaryId) {
        repository.removeFromPersonalShelf(primaryId);
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

    public LiveData<List<Book>> getPersonalShelfBooks() {
        return personalShelfBooks;
    }

    public LiveData<List<Book>> getBookByPID(Integer mainBookPrimaryId){ return repository.getBookByPID(mainBookPrimaryId);}


    //public LiveData<List<String>> getPIdByOLId(String olId) { return repository.getPIdByOLId(olId); }
}
