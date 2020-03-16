package drawrite.booknet;

import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import drawrite.booknet.entity.Book;
import drawrite.booknet.entity.Mentions;
import drawrite.booknet.repository.BookRepository;
import drawrite.booknet.repository.MentionsRepository;
import drawrite.booknet.viewModel.BookViewModel;
import drawrite.booknet.viewModel.MentionsViewModel;

public class TestViewMentionsListActivity extends BaseActivity {

    private MentionsViewModel mentionsViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        //TODO : On clicking book show the mentions details
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mention_list);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true); //makes something done under the hood efficient

        final MentionsAdapter adapter = new MentionsAdapter();
        recyclerView.setAdapter(adapter);
        Log.d("ViewMentionsActivity", " 1103 In mentions view  ");




        // verify if data is present.
        BookViewModel bookViewModel;
        // Gets the view model access as per the android
        bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

        // observer of the live data
        bookViewModel.getBookByPID(1).observe(this, new Observer<List<Book>>(){
            @Override
            public void onChanged(@Nullable List<Book> books) {
                Log.d("ViewMentionsActivity", "1103 has book corresponding to 1 " + books.get(0).getTitle());

            }


        });
        // observer of the live data
        bookViewModel.getBookByPID(2).observe(this, new Observer<List<Book>>(){
            @Override
            public void onChanged(@Nullable List<Book> books) {
                Log.d("ViewMentionsActivity", "1103 has book corresponding to 2 " + books.get(0).getTitle());

            }


        });

        // Since data present inserting mentions

        Mentions mentions = new Mentions( 1,2);


        // dummy insert in the mentions table;
        MentionsRepository repository = new MentionsRepository((Application) getApplicationContext());
        repository.insert(mentions);

/*        BookRepository bookRepository = new BookRepository((Application) getApplicationContext());
        Book book1 ;
        Book book2 ;
        Log.d("ViewMentionsActivity", " 1103 Verifying if data is present ");
        book1 = bookRepository.getBookByPID(32).getValue().get(0);

        Log.d("ViewMentionsActivity", " 1103 book1 is " + book1);
        if(book1==null){
            Log.d("ViewMentionsActivity", " has NO book corresponding to 32 ");
        }
        else


        book2 = bookRepository.getBookByPID(33).getValue().get(0);

        if(bookRepository.getBookByPID(33).getValue()!=null){
            Log.d("ViewMentionsActivity", " has NO book corresponding to 33 ");
        }
        else
            Log.d("ViewMentionsActivity", " has book corresponding to 33 ");


        if(book1!=null && book2!= null) {
            Log.d("ViewMentionsActivity", " book ids are present in the parent data ");

            // dummy insert in the mentions table;
            MentionsRepository repository = new MentionsRepository((Application) getApplicationContext());
            repository.insert(mentions);
        }
*/

        // Gets the view model access as per the android
        mentionsViewModel = ViewModelProviders.of(this).get(MentionsViewModel.class);

        // observer of the live data
        mentionsViewModel.getAllMentions().observe(this, new Observer<List<Mentions>>(){
            @Override
            public void onChanged(@Nullable List<Mentions> mentions) {
                Log.d("ViewMentionsActivity", "1103 mention table has data ");

                adapter.setMentions(mentions);
            }


        });


    }
}
