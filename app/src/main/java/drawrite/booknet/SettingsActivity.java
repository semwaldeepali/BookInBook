package drawrite.booknet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SettingsActivity extends BaseActivity {

    private Button btnClearSearchHistory;
    private SearchSuggestionSingleton searchSuggestionSingleton;


    @Override
    public void onStart() {
        super.onStart();
        Log.d("SettingsActivity", " Active");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        searchSuggestionSingleton = SearchSuggestionSingleton.getInstance();
        btnClearSearchHistory = findViewById(R.id.btnClearSearchHistory);
        btnClearSearchHistory.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searchSuggestionSingleton.suggestions.clearHistory();
                        Toast.makeText(getApplicationContext(), "Cleared Search History! ", Toast.LENGTH_SHORT).show();

                    }
                }
        );

    }


}




