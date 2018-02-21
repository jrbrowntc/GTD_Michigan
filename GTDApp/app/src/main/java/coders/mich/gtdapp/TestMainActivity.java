package coders.mich.gtdapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import coders.mich.gtdapp.navigation.MainNavManager;

/**
 * Created by drew on 2/20/2018.
 */

public class TestMainActivity extends AppCompatActivity {

    private static final String TAG = "TestMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_main);
    }

}
