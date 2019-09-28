package drawrite.booknet.model;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

//TODO : maybe fix this class by changing name or readjusting functions

public class OLBook implements Serializable {

    private String openLibraryId;
    private String goodReadsId;
    private String author;
    private String title;
    private String subTitle;
    private String publisher;
    private String publishYear;
    private int nrPages;


    //private String mention; [TODO] when differentiation is needed Task1

    public String getOpenLibraryId() {

        return openLibraryId;
    }

    public String getGoodReadsId(){
        return goodReadsId;
    }

    public String getTitle() {

        return title;
    }

    public String getSubTitle(){
        return subTitle;
    }

    public String getAuthor() {

        return author;
    }

    public String getPublisher(){
        return publisher;
    }

    public String getPublishYear(){
        return publishYear;
    }

    public int getNrPages(){
        return nrPages;
    }


    // Get medium sized book cover from covers API
    public String getCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-M.jpg?default=false";
    }

    // Get large sized book cover from covers API
    public String getLargeCoverUrl() {
        return "http://covers.openlibrary.org/b/olid/" + openLibraryId + "-L.jpg?default=false";
    }


    // Returns a Book given the expected JSON
    // [TODO] include a method for setting a threshold on book match [check best place if in fromJSON function]

    public static OLBook fromJson(JSONObject jsonObject) {
        OLBook book = new OLBook();
        try {
            // Deserialize json into object fields

            // Book open Library Id ; Check if a cover edition is available
            if (jsonObject.has("cover_edition_key"))  {
                book.openLibraryId = jsonObject.getString("cover_edition_key");
            } else if(jsonObject.has("edition_key")) {
                final JSONArray ids = jsonObject.getJSONArray("edition_key");
                book.openLibraryId = ids.getString(0); // getting open library ids.
            }

            //Book goodreads Id
            book.goodReadsId = jsonObject.has("id_goodreads" ) ? jsonObject.getString("id_goodreads"):"";
            //Book Title
            book.title = jsonObject.has("title") ? jsonObject.getString("title") : "";
            //Book Subtitle
            book.subTitle = jsonObject.has("subtitle") ? jsonObject.getString("subtitle") : "";
            //Book Author
            book.author = getAuthor(jsonObject);
            //Book Publisher calling in get extra detail
            // book.publisher = getPublisher(jsonObject);
            //Book publish year
            book.publishYear = jsonObject.has("first_publish_year")?jsonObject.getString("first_publish_year"):null;



        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return book;
    }

    // Return comma separated author list when there is more than one author
    private static String getAuthor(final JSONObject jsonObject) {
        try {
            final JSONArray authors = jsonObject.getJSONArray("author_name");
            int numAuthors = authors.length();
            final String[] authorStrings = new String[numAuthors];
            for (int i = 0; i < numAuthors; ++i) {
                authorStrings[i] = authors.getString(i);
            }
            return TextUtils.join(", ", authorStrings);
        } catch (JSONException e) {
            return "";
        }
    }
    // Return comma separated author list when there is more than one author
    private static String getPublisher(final JSONObject jsonObject) {
        try {
            final JSONArray publisher = jsonObject.getJSONArray("publisher");
            int numpublisher = publisher.length();
            final String[] publisherStrings = new String[numpublisher];
            for (int i = 0; i < numpublisher; ++i) {
                publisherStrings[i] = publisher.getString(i);
            }
            return TextUtils.join(", ", publisherStrings);
        } catch (JSONException e) {
            return "";
        }
    }

    // Decodes array of book json results into business model objects
    public static ArrayList<OLBook> fromJson(JSONArray jsonArray) {
        ArrayList<OLBook> books = new ArrayList<OLBook>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject bookJson = null;
            try {
                bookJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            OLBook book = OLBook.fromJson(bookJson);
            if (book != null) {
                books.add(book);
            }
        }
        return books;

    }
}

