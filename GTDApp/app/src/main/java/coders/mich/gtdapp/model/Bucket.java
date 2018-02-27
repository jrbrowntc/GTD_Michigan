package coders.mich.gtdapp.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */

@Entity
public class Bucket {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;

    public Bucket() {
    }

    public long getId() {
        return id;
    }

    // @todo how is this going to work with auto increment?
    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
