package drawrite.booknet.apiClient;

import java.util.List;

import drawrite.booknet.model.Book;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// This contains the actual functions for calling booknet personal api services

public interface BookNetAPI {

    @GET("read.php") //[TODO] server side increase features and pretty url support ***Change in V2***
    Call<List<Book>> read(@Query("BookTitle") String title);
}
