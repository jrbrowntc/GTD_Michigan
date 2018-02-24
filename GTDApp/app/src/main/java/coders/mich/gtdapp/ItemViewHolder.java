package coders.mich.gtdapp;

/**
 * Created by Daniel.Knapp on 2/22/2018.
 */

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class ItemViewHolder extends RecyclerView.ViewHolder {
    public TextView name;
    public TextView created;
    public ImageButton btnDelete;
    public ImageButton btnComplete;

    public ItemViewHolder(View view) {
        super(view);
        name = view.findViewById(R.id.tv_inbox_item_name);
        created = view.findViewById(R.id.tv_inbox_item_date_created);
        btnDelete = view.findViewById(R.id.btn_item_delete);
        btnComplete = view.findViewById(R.id.btn_item_complete);
    }
}
