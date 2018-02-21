package coders.mich.gtdapp;

import com.google.firebase.firestore.DocumentReference;

/**
 * Created by Daniel.Knapp on 2/21/2018.
 */

public class Item {
    private String name;
    private String description;
    private DocumentReference bucket;

    public Item(){}

    public String getName() { return name; }
    public String getDescription() { return description; }
    public DocumentReference getBucket() { return bucket; }

}
