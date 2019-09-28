//not using
package drawrite.booknet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import drawrite.booknet.model.OLBook;

public class BookDetailActivity extends AppCompatActivity {
    public static final int HAS_MENTIONED_REQUEST = 1;
    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private SearchView svHasMentioned;
    private SearchView svIsMentioned;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        Log.d("BookDetailActivity","Book detail display !");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        ivBookCover = (ImageView) findViewById(R.id.ivBookCoverDetail);
        tvTitle = (TextView) findViewById(R.id.tvTitleDetail);
        tvAuthor = (TextView) findViewById(R.id.tvAuthorDetail);
        svHasMentioned = (SearchView) findViewById(R.id.searchViewHbm);
        svIsMentioned = (SearchView) findViewById(R.id.searchViewImi);
        // Use the book to populate the data into our views
        OLBook book = (OLBook) getIntent().getSerializableExtra(OLBookListActivity.BOOK_DETAIL_KEY);
        loadBook(book);

        //[TODO] 1. add options for adding new edges/mentions.
        svHasMentioned.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent searchIntent = new Intent(BookDetailActivity.this, OLBookListActivity.class);
                searchIntent.putExtra("isFreshQuery",false);
                searchIntent.putExtra("EXTRA_QUERY",query);
                startActivityForResult(searchIntent,HAS_MENTIONED_REQUEST);

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // Test for the right intent reply.
        if (requestCode == HAS_MENTIONED_REQUEST) {
            // Test to make sure the intent reply result was good.
            if (resultCode == RESULT_OK) {
                String bookTitle = data.getStringExtra("bookTitle");
                //[TODO] 1. update the xml to reflect the added book.
                Log.d("BookDetailActivity",bookTitle);
            }
        }
    }



    // Populate data for the book
    private void loadBook(OLBook book) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
    }

}
