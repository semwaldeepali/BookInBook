package drawrite.booknet;

//For getting application context.
//source  : https://stackoverflow.com/questions/21818905/get-application-context-from-non-activity-singleton-class

import android.app.Application;

public class BookInBookContext extends Application {

    private static BookInBookContext mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static BookInBookContext getContext() {
        return mContext;
    }
}