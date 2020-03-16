package drawrite.booknet;


import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.model.OLBook;
import drawrite.booknet.repository.BookRepository;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainBookDetailFragment extends Fragment {


    private View view;
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvAuthor;
    private TextView tvPublishedByText;
    private TextView tvPublisher;
    private TextView tvPublishedByYearText;
    private TextView tvPublishYear;

    private TextView tvPageCountText;
    private TextView tvPageCount;

    private OLBookClient client;

    // variable for accesing mainBookOlId variable common across fragments.
    private BookDetailActivityTabbed mainBookDetailActivity;

    private String mainBoolOlIdMain;

    public MainBookDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getting the reference to the main activity which contain this fragment.
        // For variable sharing purpose
        mainBookDetailActivity = (BookDetailActivityTabbed) getActivity();



        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_main_book_detail, container, false);

        // Fetch views
        ivBookCover =  view.findViewById(R.id.ivBookCover);
        tvTitle =  view.findViewById(R.id.tvTitle);
        tvAuthor =  view.findViewById(R.id.tvAuthor);

        tvPublishedByText = view.findViewById(R.id.tvPublishedByText);
        tvPublisher =  view.findViewById(R.id.tvPublisher);

        tvPageCountText = view.findViewById(R.id.tvPageCountText);
        tvPageCount =  view.findViewById(R.id.tvPageCount);

        tvPublishedByYearText = view.findViewById(R.id.tvPublishedYearText);
        tvPublishYear =  view.findViewById(R.id.tvPublishYear);

        tvSubTitle =  view.findViewById(R.id.tvSubTitle);

        // Use the book to populate the data into our views
        Intent intent = getActivity().getIntent();
        if(intent.getExtras()!=null) {

            OLBook book = (OLBook) intent.getSerializableExtra(OLBookListActivity.BOOK_DETAIL_KEY);


            loadBook(book);
        }
        //TODO: Asynchronous update the local db
        return view;
    }

    // Populate data for the book
    private void loadBook(OLBook book) {

        // Populate image data
        Context c = getActivity().getApplicationContext(); // cannot directly set picasso without getting activity context
        Picasso.with(c).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);

        //setting the mainBookId variable of the parent activity.
        mainBookDetailActivity.mainBookOlId = book.getOpenLibraryId();
        Toast.makeText(getContext(), "set main book ol id : " + mainBookDetailActivity.mainBookOlId, Toast.LENGTH_SHORT).show();
        Log.d("MainBookDetailFragment", " 1103 set main book ol id : " + mainBookDetailActivity.mainBookOlId);

        // Making a DB Query to get mainBook Primary Id
        {
            BookRepository repository = new BookRepository((Application) getActivity().getApplicationContext());

            try {
                // Check if null output
                // clean up accesing bookid multiple times
                mainBookDetailActivity.mainBookPrimaryId = repository.getBookId(book.getOpenLibraryId()).get(0);
                Log.d("MainBookDetailFragment", " 1103 set main book ol id : " + mainBookDetailActivity.mainBookPrimaryId);

            } catch (ExecutionException e) {
                // Handle exception

            } catch (InterruptedException e) {

            }
        }

        String bookTitle = book.getTitle();
        String subTitle = book.getSubTitle();
        String author = book.getAuthor();
        String publishYear = book.getPublishYear();


        //update the book details whatever is present.
        if(!bookTitle.isEmpty() && bookTitle!=null)
            tvTitle.setText(bookTitle);
        else
            tvTitle.setVisibility(View.INVISIBLE);

        if(!subTitle.isEmpty() && subTitle!=null)
            tvSubTitle.setText(subTitle);
        else
            tvSubTitle.setVisibility(View.INVISIBLE);

        if(!author.isEmpty() && author!=null)
            tvAuthor.setText(author);
        else
            tvAuthor.setVisibility(View.INVISIBLE);

        if(!publishYear.isEmpty() && publishYear!=null)
            tvPublishYear.setText(publishYear);
        else
        {
            tvPublishedByYearText.setVisibility(View.INVISIBLE);
            tvPublishYear.setVisibility(View.INVISIBLE);
        }

        // fetch extra book data from books API
        client = new OLBookClient();
        client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    if (response.has("publishers") ) {
                        // display comma separated list of publishers
                        final JSONArray publisher = response.getJSONArray("publishers");
                        final int numPublishers = publisher.length();
                        final String[] publishers = new String[numPublishers];
                        for (int i = 0; i < numPublishers; ++i) {
                            publishers[i] = publisher.getString(i);
                        }
                        String pubList = TextUtils.join(", ", publishers);
                        if(!pubList.isEmpty() && pubList!=null)
                            tvPublisher.setText(pubList);
                        else
                        {
                            tvPublishedByText.setVisibility(View.INVISIBLE);
                            tvPublisher.setVisibility(View.INVISIBLE);
                        }


                    }
                    if (response.has("number_of_pages")) {
                        String nrPages = Integer.toString(response.getInt("number_of_pages")) ;
                        // TODO fix the pageCountText not getting invisible ... look for book : Grimms' Fairy Tales
                        if(!nrPages.isEmpty() && nrPages!=null)
                            tvPageCount.setText(nrPages + " pages") ;
                        else
                        {
                            tvPageCountText.setVisibility(View.INVISIBLE);
                            tvPageCount.setVisibility(View.INVISIBLE);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
        );


    }
}
