package coders.mich.gtdapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import coders.mich.gtdapp.model.Bucket;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */

@Dao
public interface BucketDao {

    @Query("SELECT * FROM bucket")
    LiveData<List<Bucket>> getBuckets();

    @Query("SELECT * FROM bucket WHERE id=:id")
    LiveData<Bucket> getBucketById(long id);

    @Insert
    long insert(Bucket bucket);

}
