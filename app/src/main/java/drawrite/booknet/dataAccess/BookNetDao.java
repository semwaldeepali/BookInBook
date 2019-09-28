//room data access object component

package drawrite.booknet.dataAccess;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import drawrite.booknet.entity.Book;

@Dao
public interface BookNetDao {

    @Insert
    void addBook(Book book);

    /*@Query("SELECT book_id FROM book where title LIKE :title ")
    String findBookId(String title);*/

    /*@Query("SELECT mentioned_book_id FROM mention where book_id LIKE :bookId")
    List<String> findMentionsIds(String bookId);

    @Query("SELECT title FROM book where book_id IN mentionsBookIdList")
    List<String> findMentionsTitle(String mentionsBookIdList);*/

    /*@Query("SELECT * FROM book")
    List<Book> getAllBooks();*/


}
