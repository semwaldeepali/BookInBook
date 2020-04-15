package drawrite.booknet.model;

import com.google.gson.annotations.SerializedName;

public class Quote {

    private String quote;

    private String author;

    @SerializedName("publication")
    private String bookTitle;

    public String getQuote(){
        return quote;
    }

    public String getAuthor(){
        return author;
    }

    public String getBookTitle(){
        return bookTitle;
    }



}
