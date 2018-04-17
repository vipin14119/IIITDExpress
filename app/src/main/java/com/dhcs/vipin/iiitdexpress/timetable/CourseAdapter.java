package com.dhcs.vipin.iiitdexpress.timetable;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;

//import com.github.aakira.expandablelayout.ExpandableLinearLayout;

/**
 * Created by vipin on 24/03/18.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    private Course[] mDataset;
    private int mExpandedPosition = -1;
    private RecyclerView recyclerView;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseAdapter(Course[] myDataset, RecyclerView r) {
        this.mDataset = myDataset;
        this.recyclerView = r;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextView1;
        public TextView mTextView2;
//        public ExpandableLinearLayout expandableLinearLayout;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.TV_code);
            mTextView1 = (TextView) v.findViewById(R.id.TV_time);
            mTextView2 = (TextView) v.findViewById(R.id.TV_room);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_cardview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mTextView.setText(mDataset[position].getCode());
        holder.mTextView1.setText(mDataset[position].getTime());
        holder.mTextView2.setText(mDataset[position].getRoom());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}