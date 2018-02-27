package coders.mich.gtdapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */

@Entity(foreignKeys = @ForeignKey(entity = Bucket.class,
        parentColumns = "id",
        childColumns = "bucketId"))
public class Task {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long bucketId;
    private String name;
    private String description;

    public Task() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
