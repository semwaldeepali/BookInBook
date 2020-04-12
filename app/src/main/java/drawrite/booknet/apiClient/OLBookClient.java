package drawrite.booknet.apiClient;



import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

//OpenLibrary API client for sending out network requests to specific endpoints.

public class OLBookClient {
    private static final String API_BASE_URL = "http://openlibrary.org/";
    private AsyncHttpClient client;

    public OLBookClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {

        return API_BASE_URL + relativeUrl;
    }

    //TODO : improve search results.

    // Method for accessing the search API
    public void getBooks(final String query, JsonHttpResponseHandler handler) {
        try {
            String url = getApiUrl("search.json?q="); // api for searching books
            client.get(url + URLEncoder.encode(query, "utf-8"), handler);
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    // Method for accessing books API to get publisher and no. of pages in a book.
    public void getExtraBookDetails(String openLibraryId, JsonHttpResponseHandler handler) {
        String url = getApiUrl("books/");
        client.get(url + openLibraryId + ".json", handler);
    }


}
