package drawrite.booknet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.support.v7.widget.SearchView;
import android.widget.TextView;

public class BookDetailActivityTabbed extends BaseActivity {

    private BookDetailTabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public String detailedBookOlId="";
    public Integer detailedBookPrimaryId = -1;



    public TextView tvBookTitle;

    @Override
    public void onStart() {
        super.onStart();
        Log.d("BookDetailActivityTabed"," Active");

    }


    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.book_detail_activity);
        tvBookTitle = findViewById(R.id.tvBookTopTitle);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new BookDetailTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOuterBooks(),"Outer Books");
        adapter.addFragment(new FragmentDetailedBook(),"Book Detail");
        adapter.addFragment(new FragmentInnerBooks(),"Inner Books");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1); // for setting the default tab
    }

    //onNewIntent triggered when data called from the same activity
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        detailedBookPrimaryId = -1;
        detailedBookOlId="";
        Log.d("BOOKDETAILACTIVITY", "1103 OnNewIntent reseting !");


        //tvBookTitle = findViewById(R.id.tvBookDetailTitle);
        //tvBookTitle.setText(detailedBookTitle);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new BookDetailTabAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOuterBooks(),"Outer Books");
        adapter.addFragment(new FragmentDetailedBook(),"Book Detail");
        adapter.addFragment(new FragmentInnerBooks(),"Inner Books");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1); // for setting the default tab*/
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



}
