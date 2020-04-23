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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import drawrite.booknet.entity.Book;
import drawrite.booknet.entity.Mentions;
import drawrite.booknet.repository.MentionsRepository;
import drawrite.booknet.viewModel.BookViewModel;

import static drawrite.booknet.OLBookListActivity.BOOKNET_OUTER_BOOK_OLID;
import static drawrite.booknet.OLBookListActivity.BOOKNET_OUTER_BOOK_PID;
import static drawrite.booknet.OLBookListActivity.BOOK_ENTITY_KEY;
import static drawrite.booknet.OLBookListActivity.IS_FRESH_QUERY;
import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentInnerBooks extends Fragment implements SearchDialog.SearchDialogListener, BookAdapter.OnItemClickListener {



    public FragmentInnerBooks() {
        // Required empty public constructor
    }

    // variable for accessing mainBookOlId variable common across fragments.
    private BookDetailActivityTabbed bookDetailActivityTabbed;

    private String detailedBookOlId;
    private List<Integer> innerBookIdList = null;

    private BookViewModel bookViewModel;
    private View view;

    private ProgressBar pbInnerBooks;
    private TextView mTvBookTitleInner;
    private TextView mTvBookSubTitleInner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getting the reference to the main activity which contain this fragment.
        bookDetailActivityTabbed = (BookDetailActivityTabbed) getActivity();
        detailedBookOlId =  bookDetailActivityTabbed.detailedBookOlId;


        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inner_book, container, false);

        //Progress Bar
        pbInnerBooks = view.findViewById(R.id.inner_book_progressbar);

        RecyclerView recyclerView = view.findViewById(R.id.inner_book_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true); //makes something done under the hood efficient

        final BookAdapter adapter = new BookAdapter(new ArrayList<Book>(),this,getActivity());
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = view.findViewById(R.id.fab_add_inner_book);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Resource : https://codinginflow.com/tutorials/android/custom-dialog-interface
                // opens the search box for the book to be link
                openSearchDialog();


            }
        });


        //Approach : 1. Query for main book primary id [instead of olid send primary id of main book; will save extra query]
        //           2. Query mentions db to get pairs
        //           3. Query books db to get list of books
        //           4. Display the list of books

        //3. a. Query db to fetch mentioned book ids
        MentionsRepository mentionsRepository = new MentionsRepository((Application) getActivity().getApplicationContext());
        try {
            // Check if null output
            // clean up accesing bookid multiple times
            innerBookIdList = mentionsRepository.getMentionsByBookIds(bookDetailActivityTabbed.detailedBookPrimaryId);
            Log.d("FragmentInnerBooks", "1103 retrieved mentioned book ids   " + innerBookIdList.size() );

        }
        catch (ExecutionException e){
            // Handle exception

        }
        catch (InterruptedException e){

        }
        // 3.b Fetch the detail for books from main db
        if(innerBookIdList.size()!=0){
            Log.d("FragmentInnerBooks", "1103 innerBookIdList not Empty" );


            // Gets the view model access as per the android
            bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);


            // Disable progress bar once books are found
            pbInnerBooks.setVisibility(View.GONE);


            for (int i=0; i<innerBookIdList.size(); i++) {
                Log.d("FragmentInnerBooks", "1103 looping through book id list  " + i );


                // observer of the live data
                bookViewModel.getBookByPID(innerBookIdList.get(i)).observe(this, new Observer<List<Book>>(){
                    @Override
                    public void onChanged(@Nullable List<Book> books) {
                        adapter.addBooks(books);
                    }


                });


            }




        }
        else {
            Log.d("FragmentInnerBooks", "1103 innerBookIdList Empty" );

            // Disable progress bar once books are found
            pbInnerBooks.setVisibility(View.GONE);

            view.findViewById(R.id.tvEmptyInnerBook).setVisibility(View.VISIBLE);
        }

        return  view;
    }
    public void openSearchDialog(){

        SearchDialog searchDialog = new SearchDialog();

        //setting the context
        // Resource : https://github.com/mitchtabian/DialogFragmentToFragment/blob/master/DialogFragmentFragment/app/src/main/java/codingwithmitch/com/dialogfragmentfragment/MainFragment.java
        searchDialog.setTargetFragment(FragmentInnerBooks.this, 1);

        searchDialog.show(getFragmentManager(),"search dialog");
    }

    @Override
    // Call the search activity here for searching with add link functionality
    public void applyTexts(String bookName){

        Intent OLintent = new Intent(getContext(), OLBookListActivity.class);
        OLintent.putExtra(EXTRA_QUERY, bookName);

        // Approach 1: [Dirty] Duplicate the search dialog & book detail activity
        OLintent.putExtra(IS_FRESH_QUERY,false);
        OLintent.putExtra(BOOKNET_OUTER_BOOK_OLID, bookDetailActivityTabbed.detailedBookOlId);
        OLintent.putExtra(BOOKNET_OUTER_BOOK_PID,bookDetailActivityTabbed.detailedBookPrimaryId);

        Log.d("FragmentInnerBooks", "1103 is fresh query ?  false " );
        Log.d("FragmentInnerBooks", "1103 query book name   " + bookName);
        Log.d("FragmentInnerBooks", "1103 outer book ol id   " + bookDetailActivityTabbed.detailedBookOlId);
        Log.d("FragmentInnerBooks", "1103 outer book primary id   " + bookDetailActivityTabbed.detailedBookPrimaryId);


        startActivity(OLintent);

    }
    @Override
    public void onBookClick(int position, Integer primaryId) {
        // 1. Asynchronous adding the book to the local
        // TODO.V2: Web updating ; [Also, if this is the best place to update local cache]

        // 1. Starting detail activity
        Intent intent;
        intent = new Intent(getContext(), BookDetailActivityTabbed.class);

        intent.putExtra(BOOK_ENTITY_KEY, primaryId );
        startActivity(intent);
        Log.d("FragmentINNERBooks", "1103 Started intent");

    }


    @Override
    public void onDeleteClick(int position, Integer primaryId) {
        MentionsRepository mentionsRepository = new MentionsRepository((Application) getActivity().getApplicationContext());
        // Primary id is for inner book
        mentionsRepository.deleteMention(new Mentions(bookDetailActivityTabbed.detailedBookPrimaryId, primaryId));

    }
}
