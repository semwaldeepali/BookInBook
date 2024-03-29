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
import drawrite.booknet.entity.Mentions;

// NOTE: Good approach to have one Dao per entity
@Dao
public interface MentionsDao { // NOTE: interface because room will auto generate the code.

    // Functions which are already defined by room

    // The conflict strategy defines what happens,
    // if there is an existing entry.
    // The default action is ABORT.

    @Insert(onConflict = OnConflictStrategy.REPLACE) // id already exist don't replace
    void insertMentions(Mentions mentions);

    @Delete
    void deleteMentions(Mentions mentions);

    @Update
    void updateMentions(Mentions mentions);


    // Functions not already defined in the room.

    //Delete all mentions
    @Query("DELETE FROM mentions_table")
    void deleteAllMentions();

    // NOTE: Simple query without parameters that returns values.
    @Query("SELECT * from mentions_table")
    LiveData<List<Mentions>> getAllMentions();

    //Deleting mention
    @Query("DELETE FROM mentions_table where main_book_id == :mainBookId AND mentions_book_id == :mentionsBookId")
    void deleteLink(String mainBookId, String mentionsBookId);

    //Get the mentions based on main_book_id
    @Query("SELECT mentions_book_id from mentions_table where main_book_id == :mainBookId")
    List<Integer> getMentionsByBookIds(Integer mainBookId);

    //Get the mentions based on main_book_id
    @Query("SELECT main_book_id from mentions_table where mentions_book_id == :mentionsBookId")
    List<Integer> getMainByBookIds(Integer mentionsBookId);



}
