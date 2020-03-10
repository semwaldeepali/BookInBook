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
import android.widget.ListView;
import android.support.v7.widget.SearchView;
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

import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;

public class OLBookListActivity extends BaseActivity {
    public static final String BOOK_DETAIL_KEY = "Book";
    public static final String IS_FRESH_QUERY = "IsFreshQuery";

    private boolean isFreshQuery ;
    private ListView lvBooks;
    private OLBookAdapter bookAdapter;
    private OLBookClient client;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ol_book_list);
        lvBooks = findViewById(R.id.lvBooks);
        ArrayList<OLBook> aBooks = new ArrayList<OLBook>();
        bookAdapter = new OLBookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);
        Log.d("OLBookListActivity", "before accessing query !");

        //get the query
        Intent intent = getIntent();
        query = intent.getStringExtra(EXTRA_QUERY);
        Log.d("OLBookListActivity", "after accessing query !");
        Log.d("OLBookListActivity", query);

        //check if it is fresh query
        isFreshQuery = intent.getBooleanExtra(IS_FRESH_QUERY, true);

        //fetch books remotely TODO move to asynchronous thread [?]
        fetchBooks(query);

        // set the listener for details
        setupBookSelectedListener(isFreshQuery);
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

                        // Get the docs json array
                        docs = response.getJSONArray("docs");

                        // Parse json array into array of model objects
                        final ArrayList<OLBook> books = OLBook.fromJson(docs);

                        // Remove all books from the adapter
                        bookAdapter.clear();

                        // Load model objects into the adapter
                        // TODO : Provide check option to add the "mentions" book.
                        for (OLBook book : books) {
                            bookAdapter.add(book); // add book through the adapter
                        }
                        bookAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
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



                // [TODO] 2. Launch the detail view passing book as an extra (with the option of adding the mention edge)
                // 1. Asynchronous adding the book to the local
                // TODO: Web updating ; [Also, if this is the best place to update local cache]
                Toast.makeText(getApplicationContext(), "added", Toast.LENGTH_SHORT).show();

                //adding book
                BookRepository repository = new BookRepository((Application) getApplicationContext());
                drawrite.booknet.model.OLBook OLbook = bookAdapter.getItem(position);
                repository.insert(new Book(OLbook.getOpenLibraryId(),OLbook.getGoodReadsId(),
                        OLbook.getTitle(),OLbook.getSubTitle(),OLbook.getAuthor(),OLbook.getPublisher(),
                        OLbook.getPublishYear(),Integer.toString(OLbook.getNrPages())));

                // 1. Starting detail activity
                Intent intent;
                if(!isFreshQuery)
                    intent  = new Intent(OLBookListActivity.this, BookDetailActivity.class);
                else
                    intent  = new Intent(OLBookListActivity.this, BookDetailActivityTabbed.class);

                intent.putExtra(BOOK_DETAIL_KEY, bookAdapter.getItem(position));
                    startActivity(intent);

            }
        });
    }

}
