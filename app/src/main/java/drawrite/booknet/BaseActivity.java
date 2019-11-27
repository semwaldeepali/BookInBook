package drawrite.booknet;
// this activity implements the toolbar to be used across all the app activities
// TODO: Add home button ; fix the toolbar look;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    // Handles the pressing of back button;
    // If navigation drawer is open : drawer gets closed
    // else closes the activity
    @Override
    public void onBackPressed(){
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }


    @Override
    public void setContentView(int layoutResID)
    {
        drawer = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = drawer.findViewById(R.id.fragment_container);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawer);
        Toolbar toolbar = findViewById(R.id.toolbar);

        if (useToolbar())
        {
            setSupportActionBar(toolbar); // sets toolbar as action bar
        }
        else
        {
            toolbar.setVisibility(View.GONE);
        }

        // adding navigation drawer click listener
        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //adding the hamburger icon with animation using the toggle library.

        //TODO : Fix the hamburger icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        Log.d("BaseActivity","onCreateFunction; putting up the hamburger");

        toggle.syncState();

    }

    protected boolean useToolbar()
    {
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.main_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        Log.d("BaseActivity","onCreateOptionsMenu Inflated and search associated");
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        Intent intent;
        // TODO : find best way to call different activity or move to fragments ? Decide
        switch(menuItem.getItemId()){
            case R.id.nav_viewBook:
                Toast.makeText(this, "view books", Toast.LENGTH_SHORT).show();
                intent = new Intent(BaseActivity.this,ViewBookListActivity.class);
                startActivity(intent);
                break;

            case R.id.nav_addBook:
                Toast.makeText(this, "add book", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }


        return true;
    }
}
