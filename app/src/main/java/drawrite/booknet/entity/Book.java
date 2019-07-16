package drawrite.booknet.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "book")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int bid;

    @ColumnInfo(name = "book_id")
    private String bookId;

    @ColumnInfo(name = "original_title")
    private String originalTitle;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "original_publication_year")
    private String originalPublicationYear;

    @ColumnInfo(name ="language_code")
    private String language;

    //getters
    public int getBid(){
        return bid;
    }

    public String getBookId() {
        return bookId;
    }

    public String getLanguage() {
        return language;
    }

    public String getOriginalPublicationYear() {
        return originalPublicationYear;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    //setters

    public void setBid(int bid) {
        this.bid = bid;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setOriginalPublicationYear(String originalPublicationYear) {
        this.originalPublicationYear = originalPublicationYear;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
