package coders.mich.gtdapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import coders.mich.gtdapp.navigation.MainNavManager;

/**
 * Created by drew on 2/20/2018.
 */

public class TestMainActivity extends AppCompatActivity {

    private static final String TAG = "TestMainActivity";

    private MainNavManager navManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);

        navManager = new MainNavManager(this,
                findViewById(R.id.navigation), findViewById(R.id.view_pager_content));

    }

}
