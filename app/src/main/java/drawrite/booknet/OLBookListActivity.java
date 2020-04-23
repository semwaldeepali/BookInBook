package drawrite.booknet;

//perform search and list the searched books

import android.app.Application;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.entity.Book;
import drawrite.booknet.model.OLBook;
import drawrite.booknet.repository.BookRepository;

import static drawrite.booknet.SearchableActivity.DRAWRITE_NOCONNECTION;
import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;

public class OLBookListActivity extends BaseActivity {
    public static final String BOOK_DETAIL_KEY = "BookDetailSerialized";
    public static final String BOOK_ENTITY_KEY = "BookEntitySerialized";

    public static final String IS_FRESH_QUERY = "IsFreshQuery";
    public static final String BOOKNET_INNER_BOOK_OLID = "booknetInnerBookOLID";
    public static final String BOOKNET_INNER_BOOK_PID = "booknetInnerBookPID";

    // When the mentions become main book for our main detailed book
    public static final String BOOKNET_OUTER_BOOK_OLID = "booknetOuterBookOLID";
    public static final String BOOKNET_OUTER_BOOK_PID = "booknetOuterBookPId";


    private boolean isFreshQuery = true;
    private boolean isOuterBookList = false;
    private String outerBookOLID;
    private String innerBookOLID;
    private Integer outerBookPID;
    private Integer innerBookPID;
    private ListView lvBooks;
    private TextView tvNoInternet;
    private OLBookAdapter bookAdapter;
    private OLBookClient client;
    private String query;
    private ProgressBar pbBookList;
    private ImageView mDelete;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("OLBookListActivity"," Active");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ol_book_list);
        pbBookList = findViewById(R.id.book_list_progressbar);


        //get the query
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_QUERY)) {

            Log.d("OLBookListActivity", "1103 before accessing query !");

            query = intent.getStringExtra(EXTRA_QUERY);

            Log.d("OLBookListActivity", "1103 after accessing query ! " + query);

            if(query.equals(DRAWRITE_NOCONNECTION)) {
                pbBookList.setVisibility(View.INVISIBLE);
                tvNoInternet = findViewById(R.id.tvSomeIssue);
                tvNoInternet.setVisibility(View.VISIBLE);
                tvNoInternet.setText("Uh Oh! Looks like you are offline !");
                Log.d("OLBookListActivity", "1103 We know no internet connection " + query);


            }
            else {
                lvBooks = findViewById(R.id.lvBooks);
                ArrayList<OLBook> aBooks = new ArrayList<OLBook>();
                bookAdapter = new OLBookAdapter(this, aBooks);

                lvBooks.setAdapter(bookAdapter);
                lvBooks.setVisibility(View.VISIBLE);

                //check if it is fresh query (By Default query is fresh)
                if(intent.hasExtra(IS_FRESH_QUERY)) {
                    isFreshQuery = intent.getBooleanExtra(IS_FRESH_QUERY, true);
                    Log.d("OLBookListActivity", "1103 is fresh query ?  " + isFreshQuery);
                }

                //get the detailed book id if not fresh query for (inner wrt to detailed book) i.e. books mentioned in detailed book
                if(!isFreshQuery && intent.hasExtra(BOOKNET_INNER_BOOK_OLID)) {

                    //This is the list of candidate outer books that contains inner book detailed
                    isOuterBookList = true;

                    innerBookOLID = intent.getStringExtra(BOOKNET_INNER_BOOK_OLID);
                    Log.d("OLBookListActivity", "1103 got main book id ! " + innerBookOLID);

                    innerBookPID = intent.getIntExtra(BOOKNET_INNER_BOOK_PID, -1);
                    Log.d("OLBookListActivity", "1103 got main book id ! " + innerBookPID);
                }

                //get the detailed book id if not fresh query ; books that mention detailed book
                if(!isFreshQuery && intent.hasExtra(BOOKNET_OUTER_BOOK_OLID)) {

                    outerBookOLID = intent.getStringExtra(BOOKNET_OUTER_BOOK_OLID);
                    Log.d("OLBookListActivity", "1103 got main book id ! " + outerBookOLID);
                    outerBookPID = intent.getIntExtra(BOOKNET_OUTER_BOOK_PID,-1);
                    Log.d("OLBookListActivity", "1103 got main book id ! " + outerBookPID);

                }



                // TODO.V2 : include selection from Local db. Remove local data completely only Cache
                //fetch books remotely TODO move to asynchronous thread [?]
                fetchBooks(query);

                // set the listener for details
                setupBookSelectedListener(isFreshQuery);
            }
        }
        else {
            tvNoInternet = findViewById(R.id.tvSomeIssue);
            tvNoInternet.setVisibility(View.VISIBLE);
            tvNoInternet.setText("Uh Oh! Something went wrong. Try Again");
            Log.d("OLBookListActivity", "1103 NO QUERY ! ");
        }

    }

    // This inflates the search option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    /*  Function : Capitalizes first letter of each word (non functional).
      Parameter : String to be formatted.
      Return : formatted string.
      */
    private String capitalizeFirstLetter(String str){

        //TODO : don't capitalize functional words

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

        Log.d("FragmentedDetailedBook","1103 formatted String is " + formattedString);
        return formattedString;

    }

    // 1. Executes an API call to the OpenLibrary search endpoint,
    // 2. parses the results
    // 3. Converts them into an array of book objects and adds them to the adapter
    private void fetchBooks(String query) {

        client = new OLBookClient();
        client.getBooks(query, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONArray docs = null;
                    if(response != null) {
                        pbBookList.setVisibility(View.INVISIBLE);

                        // Get the docs json array
                        docs = response.getJSONArray("docs");

                        // Parse json array into array of model objects
                        final ArrayList<OLBook> books = OLBook.fromJson(docs);

                        // Remove all books from the adapter
                        bookAdapter.clear();


                        // Disable progress bar once books are found
                        findViewById(R.id.book_list_progressbar).setVisibility(View.GONE);


                        // Load model objects into the adapter
                        for (OLBook book : books) {
                            bookAdapter.add(book); // add book through the adapter
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.

                    tvNoInternet = findViewById(R.id.tvSomeIssue);
                    tvNoInternet.setVisibility(View.VISIBLE);
                    tvNoInternet.setText("Uh Oh! Something went wrong. Try Again");
                    e.printStackTrace();
                }
            }
        });
    }

    // 1.Executes the selection of book;
    // 2.Start detail intent
    public void setupBookSelectedListener(final boolean isFreshQuery) {

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("OLBookListActivity", "Book selected !");



                // 1. Asynchronous adding the book to the local
                // TODO.V2 : Web updating ; [Also, if this is the best place to update local cache] ***Change in V2***

                // Adding book to local table
                BookRepository repository = new BookRepository((Application) getApplicationContext());
                drawrite.booknet.model.OLBook OLbook = bookAdapter.getItem(position);

                // Formatting strings & Updating Table
                String mBookTitle = OLbook.getTitle();
                String mBookSubTitle = OLbook.getSubTitle();
                String mAuthor = OLbook.getAuthor();
                String mPublisher = OLbook.getPublisher();

                mBookTitle = capitalizeFirstLetter(mBookTitle);
                mBookSubTitle = capitalizeFirstLetter(mBookSubTitle);
                mAuthor = capitalizeFirstLetter(mAuthor);
                mPublisher = capitalizeFirstLetter(mPublisher);


                repository.insert(new Book(OLbook.getOpenLibraryId(),OLbook.getGoodReadsId(),
                        mBookTitle,mBookSubTitle,mAuthor,mPublisher,
                        OLbook.getPublishYear(), Integer.toString(OLbook.getNrPages()), false));

                // 1. Starting detail activity
                Intent intent;
                if(!isFreshQuery) {

                    intent = new Intent(OLBookListActivity.this, BookDetailActivity.class);

                    // if the list is for outer book, pass the data for inner detailed book.
                    if(isOuterBookList){
                        intent.putExtra(BOOKNET_INNER_BOOK_OLID,innerBookOLID);
                        intent.putExtra(BOOKNET_INNER_BOOK_PID,innerBookPID);
                    }
                    else {
                        intent.putExtra(BOOKNET_OUTER_BOOK_OLID,outerBookOLID);
                        intent.putExtra(BOOKNET_OUTER_BOOK_PID,outerBookPID);

                    }
                }
                else {

                    intent = new Intent(OLBookListActivity.this, BookDetailActivityTabbed.class);
                }
                intent.putExtra(BOOK_DETAIL_KEY, bookAdapter.getItem(position));
                startActivity(intent);

            }
        });
    }

}
