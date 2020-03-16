package drawrite.booknet;


import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import drawrite.booknet.entity.Book;
import drawrite.booknet.repository.BookRepository;
import drawrite.booknet.repository.MentionsRepository;
import drawrite.booknet.viewModel.BookViewModel;

import static drawrite.booknet.OLBookListActivity.BOOKNET_MAIN_BOOK_OLID;
import static drawrite.booknet.OLBookListActivity.BOOKNET_MAIN_BOOK_PID;
import static drawrite.booknet.OLBookListActivity.IS_FRESH_QUERY;
import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;


/**
 * A simple {@link Fragment} subclass.
 */
public class HasMentionedFragment extends Fragment implements SearchDialog.SearchDialogListener {



    public HasMentionedFragment() {
        // Required empty public constructor
    }

    // variable for accessing mainBookOlId variable common across fragments.
    private BookDetailActivityTabbed mainBookDetailActivity;

    private String mainBoolOlIdHas;
    private List<Integer> mentionedBookIdsList;
    private BookViewModel bookViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getting the reference to the main activity which contain this fragment.
        mainBookDetailActivity = (BookDetailActivityTabbed) getActivity();
        mainBoolOlIdHas =  mainBookDetailActivity.mainBookOlId;



        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_has_mentioned, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.has_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true); //makes something done under the hood efficient

        final BookAdapter adapter = new BookAdapter();
        recyclerView.setAdapter(adapter);

        // TODO show the list of mentions books from the Mentions db [START]
        //Approach : 1. Query for main book primary id [instead of olid send primary id of main book; will save extra query]
        //           2. Query mentions db to get pairs
        //           3. Query books db to get list of books
        //           4. Display the list of books

        FloatingActionButton fab = view.findViewById(R.id.fab_hm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "this book mentions ... books !!", Toast.LENGTH_SHORT).show();


                // Resource : https://codinginflow.com/tutorials/android/custom-dialog-interface
                // opens the search box for the book to be link
                openSearchDialog();


            }
        });

        // TODO show the list of mentions books from the Mentions db [START]
        //Approach : 1. Query for main book primary id [instead of olid send primary id of main book; will save extra query]
        //           2. Query mentions db to get pairs
        //           3. Query books db to get list of books
        //           4. Display the list of books

        //3. a. Query db to fetch mentioned book ids
        MentionsRepository mentionsRepository = new MentionsRepository((Application) getActivity().getApplicationContext());
        try {
            // Check if null output
            // clean up accesing bookid multiple times
            mentionedBookIdsList = mentionsRepository.getMentionsByBookIds(mainBookDetailActivity.mainBookPrimaryId);
            Log.d("HasMentionedFragment", "1103 retrieved mentioned book ids   " + mentionedBookIdsList.size() );

        }
        catch (ExecutionException e){
            // Handle exception

        }
        catch (InterruptedException e){

        }
        // 3.b Fetch the detail for books from main db
        if(mentionedBookIdsList!=null){

            // Gets the view model access as per the android
            bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);


            for (int i=0; i<mentionedBookIdsList.size(); i++) {
                Log.d("HasMentionedFragment", "1103 looping through book id list  " + i );

                // observer of the live data
                bookViewModel.getBookByPID(mentionedBookIdsList.get(i)).observe(this, new Observer<List<Book>>(){
                    @Override
                    public void onChanged(@Nullable List<Book> books) {
                        adapter.setBooks(books);
                    }


                });


            }




        }

        return  view;
    }
    public void openSearchDialog(){
        Toast.makeText(getContext(), "search dialog opened ", Toast.LENGTH_SHORT).show();

        SearchDialog searchDialog = new SearchDialog();

        //setting the context
        // Resource : https://github.com/mitchtabian/DialogFragmentToFragment/blob/master/DialogFragmentFragment/app/src/main/java/codingwithmitch/com/dialogfragmentfragment/MainFragment.java
        searchDialog.setTargetFragment(HasMentionedFragment.this, 1);

        searchDialog.show(getFragmentManager(),"search dialog");
    }

    @Override
    // Call the search activity here for searching with add link functionality
    public void applyTexts(String bookName){

        Intent OLintent = new Intent(getContext(), OLBookListActivity.class);
        OLintent.putExtra(EXTRA_QUERY, bookName);
        // TODO : How to send signal if is or has mentioned ?
        // Approach 1: [Dirty] Duplicate the search dialog & book detail activity
        OLintent.putExtra(IS_FRESH_QUERY,false);
        OLintent.putExtra(BOOKNET_MAIN_BOOK_OLID,mainBoolOlIdHas);
        OLintent.putExtra(BOOKNET_MAIN_BOOK_PID,mainBookDetailActivity.mainBookPrimaryId);

        Log.d("HasMentionedFragment", "1103 is fresh query ?  false " );
        Log.d("HasMentionedFragment", "1103 query book name   " + bookName);
        Log.d("HasMentionedFragment", "1103 main book ol id   " + mainBoolOlIdHas);
        Log.d("HasMentionedFragment", "1103 main book primary id   " + mainBookDetailActivity.mainBookPrimaryId);


        startActivity(OLintent);
        Toast.makeText(getContext(), "got book name to search : " + bookName +"Main book ol id "+ mainBoolOlIdHas, Toast.LENGTH_SHORT).show();

    }


}
