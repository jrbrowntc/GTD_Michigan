package coders.mich.gtdapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.SetOptions;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InboxActivity extends AppCompatActivity implements InboxAdapter.InboxAdapterOnClickHandler{

    private RecyclerView mRecyclerView;

    private TextView textview;
    private String mBucketName;
    private FloatingActionButton btnAdd;

    private final String testUserId = "dakna";

    private FirestoreRecyclerAdapter mFirestoreAdapter;

    private FirebaseFirestore firestoreDB;
    private ListenerRegistration firestoreListener;


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
        textview = findViewById(R.id.tv_task_label);

        btnAdd = findViewById(R.id.fab_add_task);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "fab add clicked: ");
                addInboxTestItem("test " + new Date().toString().substring(11, 20));
            }
        });

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mBucketName = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                textview.setText(mBucketName);
            }
        }

        firestoreDB = FirebaseFirestore.getInstance();
        getItemList();

        // Using new Firestore Adapter
        mRecyclerView.setAdapter(mFirestoreAdapter);





    }

    @Override
    public void onClick(String inboxItemData) {
        Log.v(TAG, "onClick");
    }

    private void getItemList() {

        final CollectionReference itemsCollectionRef = firestoreDB.collection("/users/" + testUserId + "/items");
        final DocumentReference inboxDocumentRef = firestoreDB.document("users/" + testUserId + "/buckets/inbox");
        final Query query = itemsCollectionRef.whereEqualTo("bucket", inboxDocumentRef);

        FirestoreRecyclerOptions<Item> response = new FirestoreRecyclerOptions.Builder<Item>()
                .setQuery(query, Item.class)
                .build();

        mFirestoreAdapter = new FirestoreRecyclerAdapter<Item, ItemViewHolder>(response) {

            @Override
            public Item getItem(int position) {
                Item item = super.getItem(position);
                // fill id into local POJO so we can pass it on when clicked
                item.setId(this.getSnapshots().getSnapshot(position).getId());
                return item;
            }

            @Override
            protected void onBindViewHolder(ItemViewHolder holder, int position, final Item item) {

                holder.name.setText(item.getName());
                // created is a server timestamp. but firestore writes into local cache first and notifies change listeners.
                // created will be NULL first, and then updated with server timestamp.
                if (item.getCreated() != null) holder.created.setText(DateFormat.getInstance().format(item.getCreated()));
                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "item name clicked: " + item.getId());
                    }
                });
                // @todo good ui for actions on items: complete, edit, delete. 3 buttons is a bit much per row.
                holder.btnComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "item complete clicked");
                        completeItem(item.getId());
                    }
                });

                holder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d(TAG, "item delete clicked");
                        deleteItem(item.getId());
                    }
                });
            }

            @Override
            public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.inbox_list_item, parent, false);

                return new ItemViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                Log.d(TAG,"on Data changed");
            }

        };

        mFirestoreAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mFirestoreAdapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        mFirestoreAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mFirestoreAdapter.stopListening();
    }

    private void updateItem(String id) {
        // @todo UI needs an edit button, call this method from its click handler
        // @todo build intent with item ID and start another Activity
    }

    private void completeItem(String id) {

        DocumentReference completedBucket = firestoreDB.document("/users/" + testUserId + "/buckets/completed");

        Map<String, Object> data = new HashMap<>();
        data.put("bucket", completedBucket);

        firestoreDB.collection("/users/" + testUserId + "/items")
                .document(id)
                .set(data, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Item has been moved to completed bucket", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addInboxTestItem(String name) {

        DocumentReference inboxBucket = firestoreDB.document("/users/" + testUserId + "/buckets/inbox");

        Map<String, Object> data = new HashMap<>();
        data.put("bucket", inboxBucket);
        data.put("name", name);
        data.put("description", "Test item");
        data.put("created", FieldValue.serverTimestamp());

        firestoreDB.collection("/users/" + testUserId + "/items")
                .document()
                .set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "New test item added to inbox", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteItem(String id) {
        firestoreDB.collection("/users/" + testUserId + "/items")
                .document(id)
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "Item has been deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
