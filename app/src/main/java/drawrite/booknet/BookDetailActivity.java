//normal book detail activity without tabs for adding book
package drawrite.booknet;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.model.OLBook;

public class BookDetailActivity extends BaseActivity {

    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvAuthor;
    private TextView tvPublishedByText;
    private TextView tvPublisher;
    private TextView tvPublishedByYearText;
    private TextView tvPublishYear;

    private TextView tvPageCountText;
    private TextView tvPageCount;

    private Button btnAddLink;

    private OLBookClient client;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_main_book_detail);


        // Fetch views
        ivBookCover =  findViewById(R.id.ivBookCover);
        tvTitle =  findViewById(R.id.tvTitle);
        tvAuthor =  findViewById(R.id.tvAuthor);

        tvPublishedByText = findViewById(R.id.tvPublishedByText);
        tvPublisher =  findViewById(R.id.tvPublisher);

        tvPageCountText = findViewById(R.id.tvPageCountText);
        tvPageCount =  findViewById(R.id.tvPageCount);

        tvPublishedByYearText = findViewById(R.id.tvPublishedYearText);
        tvPublishYear =  findViewById(R.id.tvPublishYear);

        tvSubTitle =  findViewById(R.id.tvSubTitle);

        // add link button
        btnAddLink = findViewById(R.id.btnAddEdge);

        // Use the book to populate the data into our views
        Intent intent = getIntent();
        if(intent.getExtras()!=null) {

            OLBook book = (OLBook) intent.getSerializableExtra(OLBookListActivity.BOOK_DETAIL_KEY);

            loadBook(book);
        }
        //TODO: Asynchronous update the local db

    }

    // Populate data for the book
    private void loadBook(OLBook book) {

        // Populate image data
        Context c = getApplicationContext(); // cannot directly set picasso without getting activity context
        Picasso.with(c).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);

        String bookTitle = book.getTitle();
        String subTitle = book.getSubTitle();
        String author = book.getAuthor();
        String publishYear = book.getPublishYear();

        //update the book details whatever is present.
        if(!bookTitle.isEmpty() && bookTitle!=null)
            tvTitle.setText(bookTitle);
        else
            tvTitle.setVisibility(View.INVISIBLE);

        if(!subTitle.isEmpty() && subTitle!=null)
            tvSubTitle.setText(subTitle);
        else
            tvSubTitle.setVisibility(View.INVISIBLE);

        if(!author.isEmpty() && author!=null)
            tvAuthor.setText(author);
        else
            tvAuthor.setVisibility(View.INVISIBLE);

        if(!publishYear.isEmpty() && publishYear!=null)
            tvPublishYear.setText(publishYear);
        else
        {
            tvPublishedByYearText.setVisibility(View.INVISIBLE);
            tvPublishYear.setVisibility(View.INVISIBLE);
        }

        // fetch extra book data from books API
        client = new OLBookClient();
        client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            if (response.has("publishers") ) {
                                // display comma separated list of publishers
                                final JSONArray publisher = response.getJSONArray("publishers");
                                final int numPublishers = publisher.length();
                                final String[] publishers = new String[numPublishers];
                                for (int i = 0; i < numPublishers; ++i) {
                                    publishers[i] = publisher.getString(i);
                                }
                                String pubList = TextUtils.join(", ", publishers);
                                if(!pubList.isEmpty() && pubList!=null)
                                    tvPublisher.setText(pubList);
                                else
                                {
                                    tvPublishedByText.setVisibility(View.INVISIBLE);
                                    tvPublisher.setVisibility(View.INVISIBLE);
                                }


                            }
                            if (response.has("number_of_pages")) {
                                String nrPages = Integer.toString(response.getInt("number_of_pages")) ;
                                // TODO fix the pageCountText not getting invisible ... look for book : Grimms' Fairy Tales
                                if(!nrPages.isEmpty() && nrPages!=null)
                                    tvPageCount.setText(nrPages + " pages") ;
                                else
                                {
                                    tvPageCountText.setVisibility(View.INVISIBLE);
                                    tvPageCount.setVisibility(View.INVISIBLE);
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        btnAddLink.setVisibility(View.VISIBLE);


                        //TODO : Add on click listener 070320
                        btnAddLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getBaseContext(), "Added book link", Toast.LENGTH_SHORT).show();

                            }
                            //TODO : [8March start here] updating books links in db local/web
                        });

                    }
                }
        );


    }



}
