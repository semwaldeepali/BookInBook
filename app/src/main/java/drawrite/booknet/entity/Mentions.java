package drawrite.booknet.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;


// mentions_book_id is the one that is mentioned within main_book_id
@Entity(tableName = "mentions_table",
    foreignKeys = {
        @ForeignKey(entity = Book.class,
        parentColumns = "id",
        childColumns = "main_book_id"),
        @ForeignKey(entity = Book.class,
        parentColumns = "id",
        childColumns = "mentions_book_id")
    })
public class Mentions {

    @ColumnInfo(name = "main_book_id")
    private String mainId;

    @ColumnInfo(name = "mentions_book_id")
    private String mentionsId;

    //constructor
    public Mentions(String mainId, String mentionsId) {
        this.mainId = mainId;
        this.mentionsId = mentionsId;
    }

    public String getMainId() {
        return mainId;
    }

    public String getMentionsId() {
        return mentionsId;
    }

    public void setMainId(String mainId) {
        this.mainId = mainId;
    }

    public void setMentionsId(String mentionsId) {
        this.mentionsId = mentionsId;
    }
}
