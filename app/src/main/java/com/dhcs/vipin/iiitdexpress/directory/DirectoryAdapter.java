package com.dhcs.vipin.iiitdexpress.directory;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;

/**
 * Created by vipin on 25/03/18.
 */

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.ViewHolder> {
    private String[] mDataset;
    private int mExpandedPosition = -1;
    private RecyclerView recyclerView;

    // Provide a suitable constructor (depends on the kind of dataset)
    public DirectoryAdapter(String[] myDataset, RecyclerView r) {
        this.mDataset = myDataset;
        this.recyclerView = r;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public CardView mCardView;
        public View mDetails;
//        public ExpandableLinearLayout expandableLinearLayout;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.name);
//            mCardView = (CardView) v.findViewById(R.id.faculty_card);
            mDetails = (View) v.findViewById(R.id.mDetails);
//            expandableLinearLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public DirectoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_faculty_cardview_collapsed, parent, false);
        DirectoryAdapter.ViewHolder vh = new DirectoryAdapter.ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final DirectoryAdapter.ViewHolder holder, final int position) {

        holder.mTextView.setText(mDataset[position]);
        final boolean isExpanded = position == mExpandedPosition;
        holder.mDetails.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.mTextView.setActivated(isExpanded);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                TransitionManager.beginDelayedTransition(recyclerView);
                notifyDataSetChanged();
            }
        });
        // - get element from your dataset at this position
        // - replace the contents of the view with that element


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}