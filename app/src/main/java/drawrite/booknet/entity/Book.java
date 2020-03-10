package drawrite.booknet.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;


@Entity(tableName = "book_table", indices = {@Index(value =
        {"book_ol_id"}, unique = true)}) // table name convention as sql
public class Book {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "book_ol_id")
    private String olid;

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
    private String nrPages;

    //constructor
    public Book(String olid, String goodReadsId, String title, String subTitle, String author, String publisher, String publishYear, String nrPages) {
        this.olid = olid;
        this.goodReadsId = goodReadsId;
        this.title = title;
        this.subTitle = subTitle;
        this.author = author;
        this.publisher = publisher;
        this.publishYear = publishYear;
        this.nrPages = nrPages;
    }

    //getters
    public int getId(){ //no setter for id as auto increment
        return id;
    }

    public String getOlid(){ return olid; }

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

    public String getNrPages(){
        return nrPages;
    }

    //Setters
    //Was getting "error: Cannot find setter for field." Hence adding setter for autoincrement field

    public void setId(int id){ this.id = id; }



}
