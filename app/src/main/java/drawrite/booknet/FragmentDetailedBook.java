package drawrite.booknet;


import android.app.Application;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;
import drawrite.booknet.apiClient.OLBookClient;
import drawrite.booknet.entity.Book;
import drawrite.booknet.model.OLBook;
import drawrite.booknet.repository.BookRepository;
import drawrite.booknet.viewModel.BookViewModel;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDetailedBook extends Fragment {


    private View view;
    private ImageView ivBookCover;

    private TextView tvTitle;
    private TextView tvSubTitle;
    private TextView tvAuthor;
    private TextView tvPublishedByText;
    private TextView tvPublishedByYearText;
    private TextView tvPageCountText;

    private ProgressBar pbDetailedBook;

    private OLBookClient client;
    private boolean detailFromLocal = false;

    private Integer primaryId;


    private BookViewModel bookViewModel;

    // variable for accesing mainBookOlId variable common across fragments.
    private BookDetailActivityTabbed bookDetailActivityTabbed;

    public FragmentDetailedBook() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("FragmentDetailedBook", " 1103 IN fragment detail");

        //getting the reference to the main activity which contain this fragment.
        // For variable sharing purpose
        bookDetailActivityTabbed = (BookDetailActivityTabbed) getActivity();

        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_detailed_book, container, false);

        // Fetch views
        ivBookCover =  view.findViewById(R.id.ivBookCover);
        tvTitle =  view.findViewById(R.id.tvTitle);
        tvSubTitle =  view.findViewById(R.id.tvSubTitle);
        tvAuthor =  view.findViewById(R.id.tvAuthor);

        tvPublishedByText = view.findViewById(R.id.tvPublishedByText);
        tvPageCountText = view.findViewById(R.id.tvPageCountText);
        tvPublishedByYearText = view.findViewById(R.id.tvPublishedYearText);


        //Progress Bar
        pbDetailedBook = view.findViewById(R.id.detailed_book_progressbar);

        // Use the book to populate the data into our views

        //TODO : Decouple loading book based on intents.
        Intent intent = getActivity().getIntent();

        if(intent.hasExtra(OLBookListActivity.BOOK_DETAIL_KEY)) {
            // Intent has been raised for fresh query
            OLBook book = (OLBook) intent.getSerializableExtra(OLBookListActivity.BOOK_DETAIL_KEY);
            loadBook(book);
        }
        else if(intent.hasExtra(OLBookListActivity.BOOK_ENTITY_KEY)){

            detailFromLocal = true;
            primaryId =  intent.getIntExtra(OLBookListActivity.BOOK_ENTITY_KEY,-1);
            Log.d("FragmentDetailedBook", " 1103 Got the Book Entity "+ primaryId.toString());

            if(primaryId!=-1)
            {
            // Gets the view model access as per the android
            bookViewModel = ViewModelProviders.of(this).get(BookViewModel.class);

            // observer of the live data
            bookViewModel.getBookByPID(primaryId).observe(this, new Observer<List<Book>>(){
                @Override
                public void onChanged(@Nullable List<Book> books) {
                    loadBook(books.get(0));
                }


            });
            }
            else
                Log.d("FragmentDetailedBook", " 1103 No Primary Id even when called from vieList "+ primaryId.toString());



        }




        return view;
    }

    /*  Function : Capitalizes first letter of each word (non functional).
      Parameter : String to be formatted.
      Return : formatted string.
      */
    private String capitalizeFirstLetter(String str){

        String formattedString = "";
        String[] words;

        // Split the string by space
        if(str!=null) {
            words = str.split(" ");
            // upper case each word's first letter
            for (int i = 0; i < words.length; i++) {
                if (words[i].length() > 1)
                    words[i] = words[i].substring(0, 1).toUpperCase() + words[i].substring(1).toLowerCase();
                formattedString = formattedString + " " +words[i];
            }
        }

        Log.d("FragmentedDetailedBook","1103 formatted String is " + formattedString);
        return formattedString;

    }

    //Populate data for the book from local data base
    private void loadBook(Book book){

        Log.d("FragmentDetailedBook", " 1103 Loading book from Local Db "+ primaryId.toString());

        String bookTitle ;
        String subTitle ;
        String author ;
        String publishYear ;
        String bookOlId ;
        Integer bookPId ;

        if(book != null){



                bookOlId = book.getOlid();
                bookPId = book.getId();

                //String already formatted
                bookTitle = book.getTitle();
                subTitle = book.getSubTitle();
                author = book.getAuthor();
                publishYear = book.getPublishYear();

                //setting the mainBookId variable of the parent activity.
                bookDetailActivityTabbed.detailedBookOlId = bookOlId ;
                bookDetailActivityTabbed.detailedBookPrimaryId = bookPId;
                bookDetailActivityTabbed.tvBookTitle.setText(bookTitle);


                Log.d("FragmentDetailedBook", " 1103 Got the book from db "+ book.getOlid());


                //TODO : clean up ** Change in V2 **
                // DIRTY way of loading book cover
                // Populate image data
                Context c = getActivity().getApplicationContext(); // cannot directly set picasso without getting activity context
                Picasso.with(c).load(Uri.parse("http://covers.openlibrary.org/b/olid/" + book.getOlid() + "-L.jpg?default=false")).error(R.drawable.ic_nocover).into(ivBookCover);



                // Disable progress bar once books are found
                pbDetailedBook.setVisibility(View.GONE);


                //Setting the view data

                //update the book details whatever is present.
                if (!bookTitle.isEmpty() && bookTitle != null) {
                    tvTitle.setText(bookTitle);
                }
                else
                    tvTitle.setVisibility(View.INVISIBLE);

                if (!subTitle.isEmpty() && subTitle != null) {

                    tvSubTitle.setText(subTitle);
                }
                else
                    tvSubTitle.setVisibility(View.INVISIBLE);

                if (!author.isEmpty() && author != null)
                    tvAuthor.setText(author);
                else
                    tvAuthor.setVisibility(View.INVISIBLE);

                if (!publishYear.isEmpty() && publishYear != null)
                    tvPublishedByYearText.setText("First Published : " + publishYear + ", ");
                else
                    tvPublishedByYearText.setVisibility(View.INVISIBLE);

                // fetch extra book data from books API
                client = new OLBookClient();
                client.getExtraBookDetails(book.getOlid(), new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                try {
                                    if (response.has("publishers")) {

                                        // display comma separated list of publishers
                                        final JSONArray publisher = response.getJSONArray("publishers");
                                        final int numPublishers = publisher.length();
                                        final String[] publishers = new String[numPublishers];
                                        for (int i = 0; i < numPublishers; ++i) {
                                            publishers[i] = publisher.getString(i);
                                        }
                                        String pubList = TextUtils.join(", ", publishers);
                                        if (!pubList.isEmpty() && pubList != null)
                                            tvPublishedByText.setText("Published By " + pubList + ".");
                                        else
                                            tvPublishedByText.setVisibility(View.INVISIBLE);


                                    }else {
                                        tvPublishedByText.setVisibility(View.INVISIBLE);
                                    }
                                    if (response.has("number_of_pages")) {

                                        String nrPages = Integer.toString(response.getInt("number_of_pages"));
                                        if (!nrPages.isEmpty() && nrPages != null){
                                            Log.d("FragmentDetailedBook", " 1103 nr. of pages !=null" + nrPages);
                                            tvPageCountText.setText( nrPages + " Pages, ");
                                        }
                                        else {
                                            Log.d("FragmentDetailedBook", " 1103 nr. of pages == null");
                                            tvPageCountText.setVisibility(View.INVISIBLE);
                                        }

                                    }
                                    else{
                                        tvPageCountText.setVisibility(View.INVISIBLE);
                                    }
                                } catch (JSONException e) {
                                    Log.d("FragmentDetailedBook", " 1103 exception " + e );
                                    e.printStackTrace();
                                }

                            }
                        }
                );


            }
        else {
            Log.d("FragmentDetailedBook", " 1103 no book in local db even the call came from viewBooks");
        }


    }

    // Populate data for the book from OpenLibrary
    private void loadBook(OLBook book) {

        String bookTitle ;
        String subTitle ;
        String author ;
        String publishYear ;
        String bookOlId;
        String bookPId;

        // Populate image data
        Context c = getActivity().getApplicationContext(); // cannot directly set picasso without getting activity context
        Picasso.with(c).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);

        //TODO : standardize function names (eg. getOlID) across entities and models


            bookOlId = book.getOpenLibraryId();
            // Formatting string again as data is recieved from Open Library.

            bookTitle = capitalizeFirstLetter(book.getTitle());
            subTitle = capitalizeFirstLetter(book.getSubTitle());
            author = capitalizeFirstLetter(book.getAuthor());
            publishYear = book.getPublishYear();

            //setting the mainBookId variable of the parent activity.
            bookDetailActivityTabbed.detailedBookOlId = bookOlId;

            bookDetailActivityTabbed.tvBookTitle.setText(bookTitle);
            Log.d("FragmentDetailedBook", " 1103 set main book ol id : " + bookDetailActivityTabbed.detailedBookOlId);

            // Making a DB Query to get mainBook Primary Id
            {
                BookRepository repository = new BookRepository((Application) getActivity().getApplicationContext());


                try {
                    // Check if null output
                    // clean up accesing bookid multiple times

                    //Setting detailedBookPrimaryId
                    bookDetailActivityTabbed.detailedBookPrimaryId = repository.getBookId(bookOlId).get(0);
                    Log.d("FragmentDetailedBook", " 1103 set main book ol id : " + bookDetailActivityTabbed.detailedBookPrimaryId);

                } catch (ExecutionException e) {
                    // Handle exception

                } catch (InterruptedException e) {

                }
            }



        // Disable progress bar once books are found
        pbDetailedBook.setVisibility(View.GONE);

            //update the book details whatever is present.
            if (!bookTitle.isEmpty() && bookTitle != null){

                tvTitle.setText(bookTitle);
            }
            else
                tvTitle.setVisibility(View.INVISIBLE);

            if (!subTitle.isEmpty() && subTitle != null)
                tvSubTitle.setText(subTitle);
            else
                tvSubTitle.setVisibility(View.INVISIBLE);

            if (!author.isEmpty() && author != null)
                tvAuthor.setText("By : "+ author);
            else
                tvAuthor.setVisibility(View.INVISIBLE);

            if (!publishYear.isEmpty() && publishYear != null)
                tvPublishedByYearText.setText("First Published : " + publishYear + ", ");
            else {
                tvPublishedByYearText.setVisibility(View.INVISIBLE);
            }

            // fetch extra book data from books API
            client = new OLBookClient();
            client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                if (response.has("publishers")) {
                                    // display comma separated list of publishers
                                    final JSONArray publisher = response.getJSONArray("publishers");
                                    final int numPublishers = publisher.length();
                                    final String[] publishers = new String[numPublishers];
                                    for (int i = 0; i < numPublishers; ++i) {
                                        publishers[i] = publisher.getString(i);
                                    }
                                    String pubList = TextUtils.join(", ", publishers);
                                    if (!pubList.isEmpty() && pubList != null)
                                        tvPublishedByText.setText("Published By " + pubList + ".");
                                    else {
                                        tvPublishedByText.setVisibility(View.INVISIBLE);
                                    }


                                }else {
                                    tvPublishedByText.setVisibility(View.INVISIBLE);
                                }
                                if (response.has("number_of_pages")) {
                                    String nrPages = Integer.toString(response.getInt("number_of_pages"));
                                    if (!nrPages.isEmpty() && nrPages != null) {
                                        Log.d("FragmentDetailedBook", " 1103 nr. of pages !=null" + nrPages);
                                        tvPageCountText.setText( nrPages + " Pages, ");
                                    }
                                    else {
                                        Log.d("FragmentDetailedBook", " 1103 nr. of pages == null" );

                                        tvPageCountText.setVisibility(View.INVISIBLE);
                                    }

                                }
                                else{
                                    tvPageCountText.setVisibility(View.INVISIBLE);
                                }
                            } catch (JSONException e) {
                                Log.d("FragmentDetailedBook", " 1103 exception " + e );

                                e.printStackTrace();
                            }

                        }
                    }
            );


        }

}
