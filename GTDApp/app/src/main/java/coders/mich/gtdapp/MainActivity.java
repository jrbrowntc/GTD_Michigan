package coders.mich.gtdapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button inboxButton;
    private final static String inbox = "inbox";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inboxButton = findViewById(R.id.btn_inbox);
        inboxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Class destinationClass = InboxActivity.class;
                Intent intentToStartDetailActivity = new Intent(context, destinationClass);
                intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, inbox);
                startActivity(intentToStartDetailActivity);
            }
        });

        findViewById(R.id.btn_test_main_activity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent testMainActivityIntent = new Intent(getApplicationContext(),
                        TestMainActivity.class);
                startActivity(testMainActivityIntent);
            }
        });
    }
}
