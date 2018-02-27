package coders.mich.gtdapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openActivityBasedOnTag(View view) {
        String className = getPackageName() + "." + view.getTag();
        try {
            Intent activityIntent = new Intent(this,
                        Class.forName(className));
            startActivity(activityIntent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
