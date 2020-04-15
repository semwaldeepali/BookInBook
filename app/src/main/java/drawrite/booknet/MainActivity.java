package drawrite.booknet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

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

public class MainActivity extends BaseActivity {

    //private android.support.v7.widget.Toolbar toolbar;
    private  TextView mTvQuote;
    private  TextView mTvAuthor;
    private  TextView mTvBookName;
    private RandomQuoteGenerator mRandomQuoteGenerator;




    @Override
    public void onStart() {
        super.onStart();
        Log.d("MainActivity"," Active");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Log.d("MainActivity","onCreateFunction");

        mTvQuote = (TextView)findViewById(R.id.tvQuoteMain);
        mTvAuthor = (TextView)findViewById(R.id.tvAuthorMain);
        mTvBookName = (TextView) findViewById(R.id.tvBookNameMain);

        //Setting random quote.
        //Source : https://stackoverflow.com/questions/52482921/how-to-check-if-retrofit-api-call-was-successful-in-android

        setGetQuoteListener();
    }

    private void setGetQuoteListener() {
        mRandomQuoteGenerator = new RandomQuoteGenerator();
        boolean isInstantiated = mRandomQuoteGenerator.getIsInstantiatedOnce();
        mRandomQuoteGenerator.chooseRandomQuote(new RandomQuoteGenerator.GetQuoteListener() {
            @Override
            public void onGetQuote() {

                Log.d("MainActivity","onGetQuote");

                String bookTitle = mRandomQuoteGenerator.getBookTitle();
                String author = mRandomQuoteGenerator.getAuthor();
                String quoteText = mRandomQuoteGenerator.getQuoteText();

                Log.d("MainActivity","Quote " + quoteText);
                Log.d("MainActivity","Author " + author);
                Log.d("MainActivity","Book Name "+ bookTitle);

                mTvQuote.setText(quoteText);
                mTvAuthor.setText(author);
                if (bookTitle!=null)
                    mTvBookName.setText(bookTitle);
                else
                    mTvBookName.setVisibility(View.INVISIBLE);
            }

        });
    }



}




