package drawrite.booknet;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BookNetAPI {

    @GET("read.php")
    Call<List<Book>> read(@Query("BookTitle") String title);
}
