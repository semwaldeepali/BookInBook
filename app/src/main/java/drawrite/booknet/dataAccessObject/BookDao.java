//room data access object component

package drawrite.booknet.dataAccessObject;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import drawrite.booknet.entity.Book;

// NOTE: Good approach to have one Dao per entity
@Dao
public interface BookDao { // NOTE: interface because room will auto generate the code.

    // Functions which are already defined by room

    // The conflict strategy defines what happens,
    // if there is an existing entry.
    // The default action is ABORT.
    // The default action is ABORT.

    @Insert(onConflict = OnConflictStrategy.IGNORE) //if already exist donot replace as primary id will keep on incrementing
    void insertBook(Book book);

    @Delete
    void deleteBook(Book book);

    @Update
    void updateBook(Book book);


    // Functions not already defined in the room.

    //Delete all books
    @Query("DELETE FROM book_table")
    void deleteAllBooks();

    // NOTE: Simple query without parameters that returns values.
    @Query("SELECT * from book_table")
    LiveData<List<Book>> getAllBooks();

    // Query to get book id corresponding to olid
    @Query("SELECT id from book_table WHERE book_ol_id = :ol_id") //single = sign
    List<Integer> getBookId(String ol_id);

    // Get list of books based on primary id
    @Query("SELECT * from book_table WHERE id = :primaryId")
    LiveData<List<Book>> getBookByPID(Integer primaryId);


}
