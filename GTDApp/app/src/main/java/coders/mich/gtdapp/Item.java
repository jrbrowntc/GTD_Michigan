package coders.mich.gtdapp;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/**
 * Created by Daniel.Knapp on 2/21/2018.
 */

public class Item {
    private String id;
    private String name;
    private String description;
    private DocumentReference bucket;
    private String test;
    private Date created;

    public Item(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public DocumentReference getBucket() { return bucket; }
    public Date getCreated() { return created; }



}
