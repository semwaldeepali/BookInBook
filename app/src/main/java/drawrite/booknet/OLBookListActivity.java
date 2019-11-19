package drawrite.booknet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.model.OLBook;

public class OLBookListActivity extends BaseActivity {
    public static final String BOOK_DETAIL_KEY = "Book";

    private boolean isFreshQuery ;
    private ListView lvBooks;
    private OLBookAdapter bookAdapter;
    private OLBookClient client;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ol_book_list);
        lvBooks = (ListView) findViewById(R.id.lvBooks);
        ArrayList<OLBook> aBooks = new ArrayList<OLBook>();
        bookAdapter = new OLBookAdapter(this, aBooks);
        lvBooks.setAdapter(bookAdapter);
        Log.d("OLBookListActivity", "before accessing query !");

        //get the query
        Intent intent = getIntent();
        query = intent.getStringExtra("EXTRA_QUERY");
        Log.d("OLBookListActivity", "after accessing query !");
        Log.d("OLBookListActivity", query);

        //check if it is fresh query
        isFreshQuery = intent.getBooleanExtra("isFreshQuery", true);

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

    // 1. Executes an API call to the OpenLibrary search endpoint, 2. parses the results
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

    // Executes the selection of book; start detail intent
    public void setupBookSelectedListener(final boolean isFreshQuery) {

        lvBooks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("OLBookListActivity", "Book selected !");
                //[TODO] 1. Asynchronous adding the book to the local [web updating later]


                // [TODO] 2. Launch the detail view passing book as an extra (with the option of adding the mention edge)
                if (isFreshQuery) {
                    //Intent intent = new Intent(OLBookListActivity.this, BookDetailActivity.class);
                    Intent intent = new Intent(OLBookListActivity.this, BookDetailActivityTabbed.class);
                    intent.putExtra(BOOK_DETAIL_KEY, bookAdapter.getItem(position));
                    startActivity(intent);
                }
                else{ // we don't want to look at the detail of the new book.
                    Log.d("OLBookListActivity", "not fresh query !");

                    Intent replyIntent = new Intent();
                    //[TODO] FIGURE the best place to update local store
                    String bookName = bookAdapter.getItem(position).getTitle();
                    replyIntent.putExtra("bookTitle",bookName);
                    setResult(RESULT_OK,replyIntent);
                    finish();
                }
            }
        });
    }

}
