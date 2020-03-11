//normal book detail activity without tabs for adding book
package drawrite.booknet;

import android.app.Application;
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

import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.dataAccessObject.BookDao;
import drawrite.booknet.dataAccessObject.MentionsDao;
import drawrite.booknet.entity.Book;
import drawrite.booknet.model.OLBook;
import drawrite.booknet.repository.BookRepository;

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

    private String nrPagesInBook =""; // declared to take the value from nrPages in inner class
    private String bookPublisher = "" ;
    private String bookTitle = "";
    private String subTitle = "";
    private String author = "";
    private String publishYear = "";
    private String openLibraryId = "";
    private String goodreadsId = "";

    private String mainBookOlId = "";


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
            mainBookOlId = intent.getStringExtra(OLBookListActivity.BOOKNET_MAIN_BOOK_OLID);
            loadBook(book);

        }

    }

    // Populate data for the book
    private void loadBook(OLBook book) {

        // Populate image data
        Context c = getApplicationContext(); // cannot directly set picasso without getting activity context
        Picasso.with(c).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);

        bookTitle = book.getTitle();
        subTitle = book.getSubTitle();
        author = book.getAuthor();
        publishYear = book.getPublishYear();
        openLibraryId = book.getOpenLibraryId();
        goodreadsId = book.getGoodReadsId();


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
                                String publisherName = TextUtils.join(", ", publishers);
                                BookDetailActivity.this.bookPublisher = publisherName;
                                if(!publisherName.isEmpty() && publisherName!=null)
                                    tvPublisher.setText(publisherName);
                                else
                                {
                                    tvPublishedByText.setVisibility(View.INVISIBLE);
                                    tvPublisher.setVisibility(View.INVISIBLE);
                                }


                            }
                            if (response.has("number_of_pages")) {
                                String nrPages = Integer.toString(response.getInt("number_of_pages")) ;
                                BookDetailActivity.this.nrPagesInBook = nrPages;
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




                        btnAddLink.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(getBaseContext(), "Added book link", Toast.LENGTH_SHORT).show();
                                // 1. Update the book db with new book.
                                // 1.a Update Local db
                                //adding book
                                BookRepository repository = new BookRepository((Application) getApplicationContext());

                                repository.insert(new Book(
                                        BookDetailActivity.this.openLibraryId,
                                        BookDetailActivity.this.goodreadsId,
                                        BookDetailActivity.this.bookTitle,
                                        BookDetailActivity.this.subTitle,
                                        BookDetailActivity.this.author,
                                        BookDetailActivity.this.bookPublisher,
                                        BookDetailActivity.this.publishYear,
                                        BookDetailActivity.this.nrPagesInBook));
                                // TODO 1.b Update web db

                                //TODO Start here some error when clicking add link

                                // 2. Add the link into mentions table.
                                // Algo : 1. get main book id
                                //        2. get mentioned book id
                                //        3. Update the mentions db for the [main, mentioned] case
                                // TODO : Going ahead assuming only "mentions edge"
                                //        see for more detail BookDetailedActivityTabbed

                                // 1. get main book id
                                Integer mainBookId = -1;
                                try {
                                    // Check if null output
                                    // clean up accesing bookid multiple times
                                    mainBookId = repository.getBookId(BookDetailActivity.this.mainBookOlId).get(0);
                                    Toast.makeText(getBaseContext(), "Found the id for "+ BookDetailActivity.this.mainBookOlId  + ": " + mainBookId, Toast.LENGTH_SHORT).show();

                                }
                                catch (ExecutionException e){
                                    // Handle exception

                                }
                                catch (InterruptedException e){

                                }


                                // 2. get mentioned book id

                                //TODO : START HERE add function for getting primary id corresponding to olid from book_table
                                Integer mentionedBookId = -1;
                                 try {
                                     // Check if null output
                                     // clean up accesing bookid multiple times
                                     mentionedBookId = repository.getBookId(BookDetailActivity.this.openLibraryId).get(0);
                                     Toast.makeText(getBaseContext(), "Found the id for "+ BookDetailActivity.this.openLibraryId  + ": " + mentionedBookId, Toast.LENGTH_SHORT).show();

                                 }
                                 catch (ExecutionException e){
                                     // Handle exception

                                 }
                                 catch (InterruptedException e){

                                 }








                            }
                            //TODO : [8March start here] updating books links in db local/web
                        });

                    }
                }
        );


    }



}
