package drawrite.booknet.apiClient;

import drawrite.booknet.model.QuoteList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface JsonQuote {

    @GET("/tag/{tagName}/")
    Call<QuoteList> getQuoteList(@Path(value = "tagName",encoded = true) String tagName);

}
