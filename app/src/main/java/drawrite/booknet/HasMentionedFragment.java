package drawrite.booknet;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import static drawrite.booknet.OLBookListActivity.IS_FRESH_QUERY;
import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;


/**
 * A simple {@link Fragment} subclass.
 */
public class HasMentionedFragment extends Fragment {


    public HasMentionedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_has_mentioned, container, false);
        FloatingActionButton fab = view.findViewById(R.id.fab_hm);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "this book mentions ... books !!", Toast.LENGTH_SHORT).show();
                //TODO : Add the mechanism of searching book to add.
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
        searchDialog.show(getChildFragmentManager(),"search dialog");
    }




}
