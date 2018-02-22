package coders.mich.gtdapp;

/**
 * Created by Daniel.Knapp on 2/22/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView created;

    public ItemViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.tv_inbox_item_name);
        created = view.findViewById(R.id.tv_inbox_item_date_created);
    }
}
