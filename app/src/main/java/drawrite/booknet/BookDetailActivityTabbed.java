package drawrite.booknet;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class BookDetailActivityTabbed extends AppCompatActivity {
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

}
