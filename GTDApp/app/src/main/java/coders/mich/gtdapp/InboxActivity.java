package coders.mich.gtdapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class InboxActivity extends AppCompatActivity implements InboxAdapter.InboxAdapterOnClickHandler{

    private RecyclerView mRecyclerView;
    private InboxAdapter mInboxAdapter;
    private TextView textview;
    private String mBucketName;

    private final String testUserId = "dakna";

    private CollectionReference mItemCollection;

    private final String TAG = InboxActivity.class.getSimpleName();

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
        mItemCollection = FirebaseFirestore.getInstance().collection( "/users/" + testUserId + "/items");
        mItemCollection.whereEqualTo("bucket", "users/" + testUserId + "/buckets/" + mBucketName);
        mItemCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String id = document.getId();
                                String name = document.getString("name");
                                Item item = document.toObject(Item.class);
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

        });

    }

    @Override
    public void onClick(String inboxItemData) {
        Log.v(TAG, "onClick");
    }
}
