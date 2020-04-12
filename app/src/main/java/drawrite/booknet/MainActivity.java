package drawrite.booknet;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends BaseActivity {

    //private android.support.v7.widget.Toolbar toolbar;
    private TextView mTvQuote;
    private TextView mTvAuthor;
    private TextView mTvBookName;

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

        //TODO : create db of quotes.
        //Setting random quote.
        chooseRandomQuote();



    }

    private void chooseRandomQuote(){
        mTvQuote.setText("\"The Ultimate Answer to Life, The Universe and Everything is...42!\"");
        mTvAuthor.setText(" - Douglas Adams, ");
        mTvBookName.setText("The Hitchhiker's Guide to the Galaxy");
    }


}




