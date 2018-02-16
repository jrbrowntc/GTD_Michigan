package coders.mich.gtdapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jasonbrown on 2/16/18.
 */
/**
 * {@link InboxAdapter} exposes a list of weather forecasts to a
 * {@link android.support.v7.widget.RecyclerView}
 */
public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxAdapterViewHolder> {

    private String[] mInboxData;

    private final InboxAdapterOnClickHandler mClickHandler;

    public interface InboxAdapterOnClickHandler {
        void onClick(String inboxItemData);
    }

    public InboxAdapter(InboxAdapterOnClickHandler onClickHandler) {
        mClickHandler = onClickHandler;
    }

    @Override
    public InboxAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int LayoutIdForListItem = R.layout.inbox_list_item;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        boolean shouldAttachImmediately = false;
        View view = layoutInflater.inflate(LayoutIdForListItem, parent, shouldAttachImmediately);
        return new InboxAdapterViewHolder(view);
    }


    @Override
    public int getItemCount() {
        if (mInboxData == null) return 0;
        return mInboxData.length;
    }

    public class InboxAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mInboxTextView;

        public InboxAdapterViewHolder(View itemView) {
            super(itemView);
            mInboxTextView = itemView.findViewById(R.id.tv_item_data);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPos = getAdapterPosition();
            String itemData = mInboxData[adapterPos];
            mClickHandler.onClick(itemData);
        }
    }
    @Override
    public void onBindViewHolder(InboxAdapterViewHolder holder, int position) {
        String inboxItemDetails = mInboxData[position];
        InboxAdapterViewHolder.mInboxTextView.setText(inboxItemDetails);
    }

    public void setInboxData(String[] inboxData) {
        mInboxData = inboxData;
        notifyDataSetChanged();
    }

}
