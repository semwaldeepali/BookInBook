package drawrite.booknet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// retrofit.builder class
public class BookNetClient {
        public static Retrofit retrofit;

        private static final String BASE_URL = "http://192.168.43.153:8888/testBookNetServer/api/book/";

        public static Retrofit getRetrofitInstance(){
            if(retrofit == null){

                retrofit = new retrofit2.Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return  retrofit;

        }
}
