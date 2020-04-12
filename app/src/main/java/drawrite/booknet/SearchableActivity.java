package drawrite.booknet;

//TODO : how much waiting to show?
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

import drawrite.booknet.model.Book;

public class SearchableActivity extends AppCompatActivity {

    public static final String EXTRA_QUERY = "extraQuery";
    public static final String DRAWRITE_NOCONNECTION = "NOINTERNETCONNECTION";
    private RecyclerView recyclerView;

    ProgressDialog progressDialog;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("SearchableActivity"," Active");

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);

        Log.d("SearchableActivity","onCreateFunction");

        // TODO : Give auto fill search options

        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        boolean isNetworkAvailable = false;
        //Check internet connection
        if(CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            isNetworkAvailable = true;

        }
        else
        {
            Toast.makeText(this,"No Internet Connection", Toast.LENGTH_LONG).show();
        }
        handleIntent(intent,isNetworkAvailable);

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

    @Override
    protected void onNewIntent(Intent intent) {
        boolean isNetworkAvailable = false;
        //Check internet connection
        if(CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            isNetworkAvailable = true;

        }
        else
        {
            Toast.makeText(this,"No Internet Connection", Toast.LENGTH_LONG).show();
        }
        handleIntent(intent,isNetworkAvailable);


    }



    private void handleIntent(Intent intent, boolean isNetworkAvailable) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading....");
            progressDialog.show();

            // [TODO] implement function to check the query is valid
            String query = intent.getStringExtra(SearchManager.QUERY);
            query = query.trim();
            if (query != null) {
                Log.d("SearchableActivity", "Got the search query");

            /* [TODO] add the logic for local and asynchronous web search for the book ***Change in V2***


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
                // [TODO] implement Case 1 ***Change in V2***

                //Case 2
            /*Create handle for the RetrofitInstance interface
            [TODO] figure out processing sequence (parrallel or serial) for these 3 cases ***Change in V2***
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
                if (isNetworkAvailable) {
                    OLintent.putExtra(EXTRA_QUERY, query);
                } else {
                    OLintent.putExtra(EXTRA_QUERY, DRAWRITE_NOCONNECTION);
                }
                startActivity(OLintent);

                //Finishing the activity to prevent loading screen when back is pressed.
                SearchableActivity.this.finish();

            }
        }


    }
}
