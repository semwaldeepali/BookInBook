package drawrite.booknet;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HelpInfoActivity extends BaseActivity {

    //private android.support.v7.widget.Toolbar toolbar;


    @Override
    public void onStart() {
        super.onStart();
        Log.d("HelpInfoActivity", " Active");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helpinfo);


    }


}




