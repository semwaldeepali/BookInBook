package drawrite.booknet;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import drawrite.booknet.entity.Book;
import drawrite.booknet.viewModel.BookViewModel;

import static drawrite.booknet.OLBookListActivity.BOOK_DETAIL_KEY;
import static drawrite.booknet.OLBookListActivity.BOOK_ENTITY_KEY;

public class ViewBookListActivity extends BaseActivity implements BookAdapter.OnBookClickListener {

    private BookViewModel bookViewModel;
    private RecyclerView recyclerView;
    private BookAdapter adapter;

    private ProgressBar pbBookList;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("ViewBookListActivity"," Active");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); //makes something done under the hood efficient

        adapter = new BookAdapter(new ArrayList<Book>(), this,this);
        recyclerView.setAdapter(adapter);

        //Progress Bar
        pbBookList = findViewById(R.id.book_list_progressbar);

        // Gets the view model access as per the android
        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        // observer of the live data
        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>(){
            @Override
            public void onChanged(@Nullable List<Book> books) {
                adapter.setBooks(books);
                pbBookList.setVisibility(View.INVISIBLE);
            }


        });

    }



    @Override
    public void OnBookClick(int position, Integer primaryId) {

        Toast.makeText(getApplicationContext(), "Book Clicked !! ", Toast.LENGTH_SHORT).show();

        // 1. Starting detail activity
        Intent intent;
        intent = new Intent(ViewBookListActivity.this, BookDetailActivityTabbed.class);

        intent.putExtra(BOOK_ENTITY_KEY, primaryId);

        Log.d("ViewBookListActivity", "1103 Intent Extra ! " + primaryId );

        startActivity(intent);

    }
}


