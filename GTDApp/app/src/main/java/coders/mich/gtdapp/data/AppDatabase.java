package coders.mich.gtdapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import coders.mich.gtdapp.model.Bucket;
import coders.mich.gtdapp.model.Task;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */


@Database(entities = {Task.class, Bucket.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private final String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase sInstance;

    public abstract TaskDao taskDao();
    public abstract BucketDao bucketDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (null == sInstance) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), AppDatabase.class, "gtd_database")

                    // @todo fix blocking MainThread queries
                    .allowMainThreadQueries()
                    .build();

            sInstance.populateInitialData();
        }
        return sInstance;
    }

    private void populateInitialData() {
        if (taskDao().count() == 0) {
            Task task = new Task();
            Bucket bucket = new Bucket();

            beginTransaction();
            try {
                bucket.setName("Init 1");
                long bucketId = bucketDao().insert(bucket);

                for (int i = 0; i < 5; i++) {
                    task.setName("Init " + i);
                    task.setDescription("Initial data population " + i);
                    task.setBucketId(bucketId);
                    long taskId = taskDao().insert(task);
                    Log.d(TAG, "adding task with ID: " + taskId);
                }
                setTransactionSuccessful();
            } finally {
                endTransaction();
            }
        }
    }

}
