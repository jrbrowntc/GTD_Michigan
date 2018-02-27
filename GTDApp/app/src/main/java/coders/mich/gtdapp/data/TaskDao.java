package coders.mich.gtdapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import coders.mich.gtdapp.model.Task;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task WHERE id=:id")
    LiveData<Task> getTaskById(long id);

    @Query("SELECT * FROM task WHERE bucketId=:bucketId")
    LiveData<Task> getTaskByBucketId(long bucketId);

    @Query("SELECT COUNT(*) FROM task")
    long count();

    @Insert
    long insert(Task task);
}
