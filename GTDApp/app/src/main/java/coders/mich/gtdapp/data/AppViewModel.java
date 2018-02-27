package coders.mich.gtdapp.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import coders.mich.gtdapp.model.Bucket;
import coders.mich.gtdapp.model.Task;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */

public class AppViewModel extends ViewModel {

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
}
