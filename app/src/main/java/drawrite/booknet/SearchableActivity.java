package drawrite.booknet;


import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.List;

import drawrite.booknet.apiClient.BookNetAPI;
import drawrite.booknet.apiClient.BookNetClient;
import drawrite.booknet.model.Book;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {

    public static final String EXTRA_QUERY = "";
    private BookAdapter adapter;
    private RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Log.d("SearchableActivity","onCreateFunction");
        //[TODO] 1. add progress bar when search is happening.
        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        handleIntent(intent);

    }
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

    /*Method to generate List of data using RecyclerView with custom adapter*/
    private void generateDataList(List<Book> bookList){
        Log.d("SearchableActivity","Generating Data List");
        recyclerView = (RecyclerView) findViewById(R.id.customRecyclerView);
        adapter = new BookAdapter(this,bookList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SearchableActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();

            // [TODO] implement function to check the query is valid
            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchableActivity","Got the search query");

            /* [TODO] add the logic for local and asynchronous web search for the book

                when search is received :
                1. if (book_id exists in local db) :
                        show book data
                        1.1 if(mentions data exists)
                            1.1.1 show the data and give relevant edit option.
                        1.2 else
                            1.2.1 give both edit options.
                2. else if ( book_id exists in booknet server)
                        show book data
                        2.1 if(mentions data exists)
                            2.1.1 show the data and give relevant edit option.
                        2.2 else
                            2.2.1 give both edit options.
                3. else
                    3.1 access open library data to get book information
                    3.2 update local and web data
                    3.3 give both edit options


             */
            // [TODO] implement Case 1

            //Case 2
            /*Create handle for the RetrofitInstance interface
            [TODO] figure out processing sequence (parrallel or serial) for these 3 cases
            BookNetAPI service = BookNetClient.getRetrofitInstance().create(BookNetAPI.class);
            Call<List<Book>> call = service.read(query);
            call.enqueue(new Callback<List<Book>>() {
                @Override
                public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                    Log.d("SearchableActivity","Got the search response");
                    progressDialog.dismiss();
                    generateDataList(response.body());
                }

                @Override
                public void onFailure(Call<List<Book>> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(SearchableActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                }
            });*/

            // case 3
            // call OLBookList Activity
            Intent OLintent = new Intent(this, OLBookListActivity.class);
            OLintent.putExtra("EXTRA_QUERY", query);
            startActivity(OLintent);

        }


    }
}
