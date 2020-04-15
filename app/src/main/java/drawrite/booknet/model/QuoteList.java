package drawrite.booknet.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class QuoteList {

    @SerializedName("current_page")
    private String currentPage;

    @SerializedName("total_pages")
    private String totalPage;

    private List<Quote> quotes;


    public String getCurrentPage(){
        return currentPage;
    }

    public String getTotalPage(){
        return totalPage;
    }

    public List<Quote> getQuotes(){
        return quotes;
    }


}
