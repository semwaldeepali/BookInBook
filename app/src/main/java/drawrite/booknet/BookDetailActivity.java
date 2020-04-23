//normal book detail activity without tabs for adding book
package drawrite.booknet;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.entity.Book;
import drawrite.booknet.entity.Mentions;
import drawrite.booknet.model.OLBook;
import drawrite.booknet.repository.BookRepository;
import drawrite.booknet.repository.MentionsRepository;


import static drawrite.booknet.OLBookListActivity.BOOKNET_INNER_BOOK_OLID;

public class BookDetailActivity extends BaseActivity {

    @Override
    public void onStart() {
        super.onStart();
        Log.d("BookDetailActivity"," Active");

    }

    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublishedByText;
    private TextView tvPublishedByYearText;
    private TextView tvPageCountText;
    private ToggleButton btnAddLinkToggle;

    private OLBookClient client;

    private String nrPagesInBook =""; // declared to take the value from nrPages in inner class
    private String bookPublisher = "" ;
    private String bookTitle = "";
    private String subTitle = "";
    private String author = "";
    private String publishYear = "";
    private String openLibraryId = "";
    private String goodreadsId = "";

    private ProgressBar pbDetailBook ;
    private SwitchCompat switchCompat;

    // outer book contains the reference to the inner book

    //private String outerBookOlId = "";
    private Integer outerBookPrimaryId = -1;

    //private String innerBookOlId = "";
    private Integer innerBookPrimaryId = -1;

    private Boolean isBookInPersonalShelf = false;

    // if the book being detailed is for outer book or inner book
    private boolean isThisOuterBook = true;

    BookRepository repository ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_detailed_book);

        // Progress Bar
        pbDetailBook = findViewById(R.id.detailed_book_progressbar);

        // Fetch views
        ivBookCover = findViewById(R.id.ivBookCover);
        tvTitle = findViewById(R.id.tvTitle);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedByText = findViewById(R.id.tvPublishedByText);
        tvPageCountText = findViewById(R.id.tvPageCountText);
        tvPublishedByYearText = findViewById(R.id.tvPublishedYearText);
        switchCompat = findViewById(R.id.switchButton);
        switchCompat.setChecked(false);

        // add link button
        btnAddLinkToggle = findViewById(R.id.tbAddLink);

        repository = new BookRepository((Application) getApplicationContext());

        // Use the book to populate the data into our views
        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            OLBook book = (OLBook) intent.getSerializableExtra(OLBookListActivity.BOOK_DETAIL_KEY);

            // The book being detailed is outer book since we are sending inner book data as intent EXTRA
            if (intent.hasExtra(BOOKNET_INNER_BOOK_OLID)) {
                // We have the data for inner book already
                // The book being detailed here is outer book
                isThisOuterBook = true;
                Log.d("BookDetailActivity", " 1103 Detailed book is inner");

            }
            if (isThisOuterBook) {

                // get inner book data from intent EXTRAS
                //innerBookOlId = intent.getStringExtra(OLBookListActivity.BOOKNET_INNER_BOOK_OLID);
                innerBookPrimaryId = intent.getIntExtra(OLBookListActivity.BOOKNET_INNER_BOOK_PID, -1);
            } else {
                // get outer book data from th extras
                //outerBookOlId = intent.getStringExtra(OLBookListActivity.BOOKNET_OUTER_BOOK_OLID);
                outerBookPrimaryId = intent.getIntExtra(OLBookListActivity.BOOKNET_OUTER_BOOK_PID, -1);
            }

            //Format strings
            bookTitle = capitalizeFirstLetter(book.getTitle());
            subTitle = capitalizeFirstLetter(book.getSubTitle());
            author = capitalizeFirstLetter(book.getAuthor());
            publishYear = book.getPublishYear();
            openLibraryId = book.getOpenLibraryId();
            goodreadsId = book.getGoodReadsId();


            // Update UI
            loadBook(book);

            // Update db with new book.
            insertBookInDb(book);

            //set switch
            if (isBookInPersonalShelf)
                switchCompat.setChecked(true);

            //get inserted book's Primary Id.

            // 2. Add the link into mentions table.
            // Algo : 1. get main book id (Passed with the intent)
            //        2. get mentioned book id
            //        3. Update the mentions db for the [main, mentioned] case When AddLink Button is Pressed
            //        see for more detail BookDetailedActivityTabbed



            // 2. get mentioned book id

            //TODO : check might cause some kind of issue because async insertion
            try {
                // Check if null output

                //SET the value based on availability of data
                if (isThisOuterBook)
                {
                    // Get the primary id for the book detailed
                    outerBookPrimaryId = repository.getPIdByOLId(BookDetailActivity.this.openLibraryId).get(0);
                    Log.d("BookDetailActivity", " 1103 O found  the primary ids outer " + outerBookPrimaryId + " or inner  : " + innerBookPrimaryId);
                } else {
                    innerBookPrimaryId = repository.getPIdByOLId(BookDetailActivity.this.openLibraryId).get(0);
                    Log.d("BookDetailActivity", " 1103 I found  the primary ids  outer " + outerBookPrimaryId + " or inner  : " + innerBookPrimaryId);
                }

            }
            catch (ExecutionException e){
                // Handle exception
                Log.d("BookDetailActivity", " 1103 Execution exception " + e);

            }
            catch (InterruptedException e){
                Log.d("BookDetailActivity", " 1103 Interrupt exception " + e);


            }

        }

        //Set toggle button clicks
        btnAddLinkToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Mentions mentionPair = new Mentions(outerBookPrimaryId, innerBookPrimaryId);
                MentionsRepository mentionsRepository = new MentionsRepository((Application) getApplicationContext());

                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(getBaseContext(), "Added book link", Toast.LENGTH_SHORT).show();
                    Log.d("BookDetailActivity", " 1103 clicked the add link ");
                    //3.Update the mentions db for the [main, mentioned] case
                    if (outerBookPrimaryId != -1 && innerBookPrimaryId != -1) {
                        Log.d("BookDetailActivity", " 1103 both of the primary id is valid: outer " + outerBookPrimaryId + " or inner : " + innerBookPrimaryId);
                        mentionsRepository.insert(mentionPair);
                        Log.d("BookDetailActivity", " 1103 Insertion initiated for mentions");
                    } else {
                        Log.d("BookDetailActivity", " 1103 either of the primary id is not valid: outer " + outerBookPrimaryId + " or inner : " + innerBookPrimaryId);


                    }
                } else {
                    // The toggle is disabled
                    mentionsRepository.deleteMention(mentionPair);
                }
            }
        });

    }

    /*  Function : Capitalizes first letter of each word (non functional).
      Parameter : String to be formatted.
      Return : formatted string.
      */
    private String capitalizeFirstLetter(String str){

        String formattedString = "";
        String[] words;

        // Split the string by space
        if(str!=null) {
            words = str.split(" ");
            // upper case each word's first letter
            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 1)
                    words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
                formattedString = formattedString + " " +words[i];
            }
        }

        Log.d("BookDetailActivity", "1103 formatted String is " + formattedString);
        return formattedString;

    }

    private void insertBookInDb(OLBook book) {


        //TODO.V2 primary id is returned when insert happens; Check if that can be used here. ** Change in V2 **
        // It will save one query for geting primary id for inserted book. below


        //check if book already in db.

        Integer bookCheckPID = -1;
        try {
            bookCheckPID = repository.getPIdByOLId(BookDetailActivity.this.openLibraryId).get(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        //if book is already present and is in personal shelf
        if (bookCheckPID == -1) {

            repository.insert(new Book(
                    BookDetailActivity.this.openLibraryId,
                    BookDetailActivity.this.goodreadsId,
                    BookDetailActivity.this.bookTitle,
                    BookDetailActivity.this.subTitle,
                    BookDetailActivity.this.author,
                    BookDetailActivity.this.bookPublisher,
                    BookDetailActivity.this.publishYear,
                    BookDetailActivity.this.nrPagesInBook,
                    false));
            // TODO.V2 1.b Update web db

        } else {
            try {
                isBookInPersonalShelf = repository.isBookInPersonalShelfByPID(bookCheckPID).get(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    // Populate data for the book
    private void loadBook(OLBook book) {

        // Populate image data
        Context c = getApplicationContext(); // cannot directly set picasso without getting activity context
        Picasso.with(c).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);


        pbDetailBook.setVisibility(View.GONE);

        //update the book details whatever is present.
        if(!bookTitle.isEmpty() && bookTitle!=null) {
            subTitle = subTitle.trim();
            if (!subTitle.isEmpty() && subTitle != null)
                bookTitle = bookTitle + " : " + subTitle;

            tvTitle.setText(bookTitle);
        }
        else
            tvTitle.setVisibility(View.INVISIBLE);

        if(!author.isEmpty() && author!=null)
            tvAuthor.setText("By " + author);
        else
            tvAuthor.setVisibility(View.INVISIBLE);

        if(!publishYear.isEmpty() && publishYear!=null)
            tvPublishedByYearText.setText("Published By - "+publishYear);
        else
        {
            tvPublishedByYearText.setVisibility(View.INVISIBLE);
        }


        btnAddLinkToggle.setVisibility(View.VISIBLE);

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
                                    tvPublishedByText.setText("Published By - "+publisherName);
                                else
                                {
                                    tvPublishedByText.setVisibility(View.INVISIBLE);
                                }


                            }else
                            {
                                tvPublishedByText.setVisibility(View.INVISIBLE);
                            }
                            if (response.has("number_of_pages")) {
                                String nrPages = Integer.toString(response.getInt("number_of_pages")) ;
                                BookDetailActivity.this.nrPagesInBook = nrPages;
                                if(!nrPages.isEmpty() && nrPages!=null)
                                    tvPageCountText.setText(nrPages + " Pages") ;
                                else
                                {
                                    tvPageCountText.setVisibility(View.INVISIBLE);
                                }

                            }else
                            {
                                tvPageCountText.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }







                    }
                }
        );


    }



}
