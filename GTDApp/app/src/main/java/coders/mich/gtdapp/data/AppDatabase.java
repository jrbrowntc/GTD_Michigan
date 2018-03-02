package coders.mich.gtdapp.data;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import coders.mich.gtdapp.R;
import coders.mich.gtdapp.model.Bucket;
import coders.mich.gtdapp.model.Task;

/**
 * Created by Daniel.Knapp on 2/27/2018.
 */


@Database(entities = {Task.class, Bucket.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private final String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase sInstance;

    public abstract TaskDao taskDao();
    public abstract BucketDao bucketDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (null == sInstance) {
            sInstance = Room
                    .databaseBuilder(context, AppDatabase.class, "gtd_database")
                    .addCallback(getDatabaseCreationCallback())
                    .addMigrations(MIGRATION_1_2)
                    .build();

        }
        return sInstance;
    }

    private static RoomDatabase.Callback getDatabaseCreationCallback() {
        return new RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                db.beginTransaction();
                try {
                    db.execSQL("INSERT INTO Bucket " +
                            "(name, iconId) " +
                            "VALUES " +
                            "('Inbox', " + R.drawable.ic_inbox_black_24dp + ")," +
                            "('Trash', " + R.drawable.ic_delete_black_24dp + ");"
                    );
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
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

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            // Add Inbox and Trash Buckets if they don't Exist
            database.execSQL(
                    "INSERT INTO Bucket(name) " +
                            "SELECT 'Inbox' " +
                            "WHERE NOT EXISTS(SELECT 1 FROM Bucket WHERE name 'Inbox');");
            database.execSQL(
                    "INSERT INTO Bucket(name) " +
                            "SELECT 'Trash' " +
                            "WHERE NOT EXISTS(SELECT 1 FROM Bucket WHERE name 'Trash');");

            // Add the iconId column
            database.execSQL(
                    "ALTER TABLE Bucket " +
                            "ADD COLUMN iconId INTEGER;");

            // Add Inbox and Trash iconIds
            database.execSQL(
                    "UPDATE Bucket" +
                            " SET iconId = " + R.drawable.ic_inbox_black_24dp +
                            " WHERE name = 'Inbox';");
            database.execSQL(
                    "UPDATE Bucket" +
                            " SET iconId = " + R.drawable.ic_delete_black_24dp +
                            " WHERE name = 'Trash';");
        }
    };

}
