package drawrite.booknet;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static drawrite.booknet.OLBookListActivity.IS_FRESH_QUERY;
import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;


/**
 * A simple {@link Fragment} subclass.
 */
public class HasMentionedFragment extends Fragment implements SearchDialog.SearchDialogListener {

    public static final String BOOKNET_MAIN_BOOK_OLID = "";

    public HasMentionedFragment() {
        // Required empty public constructor
    }

    // variable for accessing mainBookOlId variable common across fragments.
    private BookDetailActivityTabbed mainBookDetailActivity;

    private String mainBoolOlIdHas;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getting the reference to the main activity which contain this fragment.
        mainBookDetailActivity = (BookDetailActivityTabbed) getActivity();
        mainBoolOlIdHas =  mainBookDetailActivity.mainBookOlId;


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_has_mentioned, container, false);
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
        Log.d("HasMentionedFragment", "1103 is fresh query ?  false " );
        Log.d("HasMentionedFragment", "1103 query book name   " + bookName);
        Log.d("HasMentionedFragment", "1103 main book ol id   " + mainBoolOlIdHas);


        startActivity(OLintent);
        Toast.makeText(getContext(), "got book name to search : " + bookName +"Main book ol id "+ mainBoolOlIdHas, Toast.LENGTH_SHORT).show();

    }


}
