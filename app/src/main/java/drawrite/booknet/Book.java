package drawrite.booknet;

import com.google.gson.annotations.SerializedName;
// Model class

public class Book { //[TODO] divide the scripts into different folders like model , api, client etc
    //[TODO] improve the data and fix on the api to be used/ or how to use for getting data
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
