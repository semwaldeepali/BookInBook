package drawrite.booknet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import static drawrite.booknet.OLBookListActivity.IS_FRESH_QUERY;
import static drawrite.booknet.SearchableActivity.EXTRA_QUERY;

public class BookDetailActivityTabbed extends BaseActivity implements SearchDialog.SearchDialogListener{
    private BookDetailTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.book_detail_activity);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new BookDetailTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new IsMentionedInFragment(),"IsMentionedIn");
        adapter.addFragment(new MainBookDetailFragment(),"Book Detail");
        adapter.addFragment(new HasMentionedFragment(),"Has Mentioned");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1); // for setting the default tab
    }

    // inflating the search option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    // Call the search activity here for searching with add link functionality
    public void applyTexts(String bookName){

        Intent OLintent = new Intent(this, OLBookListActivity.class);
        OLintent.putExtra(EXTRA_QUERY, bookName);
        OLintent.putExtra(IS_FRESH_QUERY,false);
        startActivity(OLintent);
        Toast.makeText(this, "got book name to search : " + bookName, Toast.LENGTH_SHORT).show();

    }

}
