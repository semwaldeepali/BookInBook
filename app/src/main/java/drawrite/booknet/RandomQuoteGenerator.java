package drawrite.booknet;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import drawrite.booknet.apiClient.JsonQuote;
import drawrite.booknet.model.Quote;
import drawrite.booknet.model.QuoteList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RandomQuoteGenerator {


    private final String CLASSNAME = "RandomQuoteGenerator";

    private static String mAuthor;
    private static String mBookTitle;
    private static String mQuoteText;
    private static boolean mInstantiatedOnce = false;

    private List<String> tagList = new ArrayList(){
        {
            add("wisdom");
            add("funny");
            add("love");
            add("poem");
            add("psychology");
            add("romance");
            add("fiction");
            add("history");
            add("sci-fi");
            add("classics");
        }
    };

    //Constructor

    //Interface to track the getQuote request
    public interface GetQuoteListener{
        void onGetQuote();

    }

    private void setDefaultQuote(){

        mQuoteText = "The Ultimate Answer to Life, The Universe and Everything is...42!";
        mAuthor = " - Douglas Adams, ";
        mBookTitle = "The Hitchhiker's Guide to the Galaxy";
    }

    public void chooseRandomQuote(final GetQuoteListener getQuoteListener){

        if(mInstantiatedOnce){
            getQuoteListener.onGetQuote();
            return;
        }
        mInstantiatedOnce = true;
        //for randomly selecting quote
        final Random rand = new Random();
        String tag =  tagList.get(rand.nextInt(tagList.size()));


        // retrofit call
        Retrofit retrofit =
                new Retrofit.Builder()
                        .baseUrl("https://goodquotesapi.herokuapp.com/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();


        JsonQuote jsonQuoteClientApi = retrofit.create(JsonQuote.class);

        Call<QuoteList> call = jsonQuoteClientApi.getQuoteList(tag);

        call.enqueue(new Callback<QuoteList>(){
            @Override
            public void onResponse(Call<QuoteList> call, Response<QuoteList> response){

                if (!response.isSuccessful()) {
                    Log.d(CLASSNAME," not successful" + response.code());
                    setDefaultQuote();
                    getQuoteListener.onGetQuote();
                    return;
                }
                try {

                    //get List of Quotes
                    List<Quote> quotes = response.body().getQuotes();

                    //Choose random quote
                    Quote quote = quotes.get(rand.nextInt(quotes.size()));
                    mQuoteText = quote.getQuote();
                    mAuthor = " - " + quote.getAuthor();
                    //Quote Size Check
                    if(mQuoteText.length()<=150 && mAuthor!=null)
                    {
                        mBookTitle = quote.getBookTitle();

                        Log.d(CLASSNAME, " Quote : " + mQuoteText);
                        Log.d(CLASSNAME, " Author : " + mAuthor);
                        Log.d(CLASSNAME, " BookTitle : " + mBookTitle);

                    }
                    else
                    {

                        Log.d(CLASSNAME, " Quote Length Check FAIL: " + mQuoteText.length());
                        Log.d(CLASSNAME, " Author Check FAIL : author null " );
                        Log.d(CLASSNAME, " Quote : " + mQuoteText);
                        Log.d(CLASSNAME, " Author : " + mAuthor);
                        Log.d(CLASSNAME, " BookTitle : " + mBookTitle);
                        setDefaultQuote();

                    }

                    getQuoteListener.onGetQuote();

                }
                catch ( Exception e){
                    setDefaultQuote();
                    Log.d(CLASSNAME, "Exception caught : " + e.getMessage());
                    getQuoteListener.onGetQuote();

                }
            }

            @Override
            public void onFailure(Call<QuoteList> call, Throwable t) {
                setDefaultQuote();
                Log.d(CLASSNAME," Faliure ; " + t.getMessage());
                getQuoteListener.onGetQuote();
            }
        });
    }

    public String getAuthor(){
        return mAuthor;
    }

    public String getBookTitle(){
        return mBookTitle;
    }
    public String getQuoteText(){
        return mQuoteText;
    }

    public static boolean getIsInstantiatedOnce() {
        return mInstantiatedOnce;
    }
}
