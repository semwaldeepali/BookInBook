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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchableActivity extends AppCompatActivity {

    private BookAdapter adapter;
    private RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Log.d("SearchableActivity","onCreateFunction");

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

            String query = intent.getStringExtra(SearchManager.QUERY);
            Log.d("SearchableActivity","Got the search query");
            //use the query to search your data somehow
            /*Create handle for the RetrofitInstance interface*/
            /* [TODO] add the logic for local and asyncronous web search for the book
                case 1: if (new book) [how to conclude if new book ?]
                            1. fetch information from openlibrary api.
                            2. update data locally and on web
                            3. give options "mentioned in " & "mentions"
                                    search "mBook" locally and web ; update both

             */
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
            });
        }


    }
}
