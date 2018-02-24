package coders.mich.gtdapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class InboxActivity extends AppCompatActivity implements InboxAdapter.InboxAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private InboxAdapter mInboxAdapter;
    private TextView textview;
    private String mBucketName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        Intent intentThatStartedThisActivity = getIntent();
        mRecyclerView = findViewById(R.id.inbox_recycler_view);
        int recyclerViewOrientation = LinearLayoutManager.VERTICAL;

        boolean shouldReverseLayout = false;
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, recyclerViewOrientation, shouldReverseLayout);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mInboxAdapter = new InboxAdapter(this);
        mRecyclerView.setAdapter(mInboxAdapter);
        textview = findViewById(R.id.tv_task_label);
        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mBucketName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                textview.setText(mBucketName);
            }
        }
    }

    @Override
    public void onClick(String inboxItemData) {

    }
}
