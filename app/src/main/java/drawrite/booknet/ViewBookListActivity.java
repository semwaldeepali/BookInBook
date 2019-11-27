package drawrite.booknet;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import drawrite.booknet.entity.Book;
import drawrite.booknet.viewModel.BookViewModel;

public class ViewBookListActivity extends BaseActivity {

    private BookViewModel bookViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); //makes something done under the hood efficient

        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);

        // Gets the view model access as per the android
        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        // observer of the live data
        bookViewModel.getAllBooks().observe(this, new Observer<List<Book>>(){
            @Override
            public void onChanged(@Nullable List<Book> books) {
                adapter.setBooks(books);
            }


        });
    }
}
