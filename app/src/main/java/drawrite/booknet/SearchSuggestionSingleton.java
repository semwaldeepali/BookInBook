package drawrite.booknet;

import android.provider.SearchRecentSuggestions;

//Singleton source geekforgeeks
public class SearchSuggestionSingleton {

    // static variable single_instance of type Singleton
    private static SearchSuggestionSingleton single_instance = null;

    public SearchRecentSuggestions suggestions;


    public SearchSuggestionSingleton() {
        suggestions = new SearchRecentSuggestions(BookInBookContext.getContext(),
                SearchSuggestionProvider.AUTHORITY, SearchSuggestionProvider.MODE);

    }

    // static method to create instance of Singleton class
    public static SearchSuggestionSingleton getInstance() {
        if (single_instance == null)
            single_instance = new SearchSuggestionSingleton();

        return single_instance;
    }
}
