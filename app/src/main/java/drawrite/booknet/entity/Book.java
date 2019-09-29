package drawrite.booknet.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "books")
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id = 0;

    @ColumnInfo(name = "book_ol_id")
    private String OLId;

    public String goodReadsId;

    @ColumnInfo(name = "book_title")
    private String title;

    @ColumnInfo(name = "book_subtitle")
    private String subTitle;

    @ColumnInfo(name = "book_author")
    private String author;

    @ColumnInfo(name = "book_publisher")
    private String publisher;

    @ColumnInfo(name = "book_publish_date")
    private String publishYear;

    @ColumnInfo(name = "book_nr_pages")
    private int nrPages;

    //getters
    public int getId(){ //no setter for id as auto increment
        return id;
    }

    public String getOLId(){ return OLId; }

    public String getGoodReadsId(){
        return goodReadsId;
    }

    public String getTitle(){
        return title;
    }

    public String getSubTitle(){
        return subTitle;
    }

    public String getAuthor(){
        return author;
    }

    public  String getPublisher(){
        return publisher;
    }

    public String getPublishYear(){ return publishYear; }

    public int getNrPages(){
        return nrPages;
    }

    //Setters
    // TODO was getting "error: Cannot find setter for field." Hence adding setter for autoincrement field

    public void setId(int id){ this.id = id; }

    public void setOLId(String OLId) { this.OLId = OLId; }

    public void setGoodReadsId(String goodReadsId) {
        this.goodReadsId = goodReadsId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubTitle(String subTitle) { this.subTitle = subTitle; }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public void setNrPages(int nrPages) {
        this.nrPages = nrPages;
    }

}
