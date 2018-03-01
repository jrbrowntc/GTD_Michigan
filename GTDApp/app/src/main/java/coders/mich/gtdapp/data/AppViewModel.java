package coders.mich.gtdapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import coders.mich.gtdapp.model.Bucket;
import coders.mich.gtdapp.model.Task;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */

public class AppViewModel extends ViewModel {
    private static final String TAG = AppViewModel.class.getSimpleName();

    public AppViewModel() {
    }

    public LiveData<Task> getTaskById(TaskDao taskDao, long id) {
        return taskDao.getTaskById(id);
    }

    public LiveData<Task> getItemByBucketId(TaskDao taskDao, long bucketId) {
        return taskDao.getTaskByBucketId(bucketId);
    }

    public LiveData<List<Task>> getItems(TaskDao taskDao) {
        return taskDao.getTasks();
    }

    public LiveData<Bucket> getBucketById(BucketDao bucketDao, long id) {
        return bucketDao.getBucketById(id);
    }

    public LiveData<List<Bucket>> getBuckets(BucketDao bucketDao) {
        return bucketDao.getBuckets();
    }

    public void createBucket(final Bucket bucket, final BucketDao bucketDao) {
        // AsyncTask won't leak memory when used within the ViewModel
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                bucketDao.insert(bucket);
                return null;
            }
        }.execute();
    }

    public void createTask(final Task task, final TaskDao taskDao) {
        // AsyncTask won't leak memory when used within the ViewModel
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                taskDao.insert(task);
                Log.d(TAG, "doInBackground: Task created");
                return null;
            }
        }.execute();
    }

}
