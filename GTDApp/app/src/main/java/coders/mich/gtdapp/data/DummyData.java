package coders.mich.gtdapp.data;

import java.util.ArrayList;
import java.util.List;

import coders.mich.gtdapp.R;
import coders.mich.gtdapp.data.dao.Bucket;

/**
 * Created by drew on 2/27/2018.
 */

public class DummyData {


    public static List<Bucket> getBuckets() {
        List<Bucket> buckets = new ArrayList<>();
        buckets.add(new Bucket("Inbox", R.drawable.ic_inbox_black_24dp));
        buckets.add(new Bucket("Home"));
        buckets.add(new Bucket("Work"));
        buckets.add(new Bucket("Archive"));
        buckets.add(new Bucket("Trash", R.drawable.ic_delete_black_24dp));
        return buckets;
    }
}
