package drawrite.booknet.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//testing this : all int as the foreign keys from book is int auto increment primary key

// mentions_book_id is the one that is mentioned within main_book_id
@Entity(tableName = "mentions_table",primaryKeys = {"main_book_id","mentions_book_id"},
        foreignKeys = {
        @ForeignKey(entity = Book.class,
        parentColumns = "id",
        childColumns = "main_book_id"),
        @ForeignKey(entity = Book.class,
        parentColumns = "id",
        childColumns = "mentions_book_id")
    }
    )
public class Mentions {

    @ColumnInfo(name = "main_book_id")
    @NonNull private int mainId;

    @ColumnInfo(name = "mentions_book_id")
    @NonNull private int mentionsId;

    //constructor
    public Mentions(int mainId, int mentionsId) { //TODO fix the id's data type. If char possible
        this.mainId = mainId;
        this.mentionsId = mentionsId;
    }

    public int getMainId() {
        return mainId;
    }

    public int getMentionsId() {
        return mentionsId;
    }

    public void setMainId(int mainId) {
        this.mainId = mainId;
    }

    public void setMentionsId(int mentionsId) {
        this.mentionsId = mentionsId;
    }
}
