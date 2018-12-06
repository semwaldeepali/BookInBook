package drawrite.booknet;

import com.google.gson.annotations.SerializedName;
// Model class

public class Book {
    @SerializedName("title")
    private String title;

    public Book(String title){ //[TODO] add more data features
        this.title = title;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

}
