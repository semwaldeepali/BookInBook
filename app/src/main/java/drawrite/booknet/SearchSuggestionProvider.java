package drawrite.booknet;

import android.content.SearchRecentSuggestionsProvider;

// source : https://developer.android.com/guide/topics/search/adding-recent-query-suggestions
//TODO : Provide clear history option
public class SearchSuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "drawrite.booknet.SearchSuggestionProvider";
    public final static int MODE  = DATABASE_MODE_QUERIES;

    public SearchSuggestionProvider(){
        setupSuggestions(AUTHORITY,MODE);
    }
}
