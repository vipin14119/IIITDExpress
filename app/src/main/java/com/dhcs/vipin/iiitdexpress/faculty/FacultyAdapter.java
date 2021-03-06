package com.dhcs.vipin.iiitdexpress.faculty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//import com.github.aakira.expandablelayout.ExpandableLinearLayout;

/**
 * Created by vipin on 24/03/18.
 */

public class FacultyAdapter extends RecyclerView.Adapter<FacultyAdapter.ViewHolder> {
    private String[] mDataset;
    private int mExpandedPosition = -1;
    private RecyclerView recyclerView;



    private ArrayList<FacultyCard> facultyList;

    // Provide a suitable constructor (depends on the kind of dataset)
    public FacultyAdapter(ArrayList<FacultyCard> myDataset, RecyclerView r) {
        this.facultyList = myDataset;
        this.recyclerView = r;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public TextView mTextViewDesg;
        public TextView mTextViewDesc;
        public Button mSendMail;

        public CardView mCardView;
        public View mDetails;
//        public ExpandableLinearLayout expandableLinearLayout;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.faculty_name);
            mTextViewDesg = (TextView) v.findViewById(R.id.faculty_desg);
            mTextViewDesc = (TextView) v.findViewById(R.id.faculty_desc);
            mSendMail = (Button) v.findViewById(R.id.faculty_send_mail);
//            mCardView = (CardView) v.findViewById(R.id.faculty_card);
            mDetails = (View) v.findViewById(R.id.mDetails);
//            expandableLinearLayout = (ExpandableLinearLayout) v.findViewById(R.id.expandableLayout);
        }
    }


    // Create new views (invoked by the layout manager)
    @Override
    public FacultyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_faculty_cardview_collapsed, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final FacultyCard facultyCard = facultyList.get(position);
//        holder.mTextView.setText(mDataset[position]);
        holder.mTextView.setText(facultyCard.name);
        holder.mTextViewDesg.setText(facultyCard.desig);
        holder.mTextViewDesc.setText(facultyCard.desc);
        Log.d("DEBUG", facultyCard.email);
        holder.mSendMail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, facultyCard.email);
                emailIntent.setData(Uri.parse("mailto:"+facultyCard.email));
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
//                emailIntent.setType("text/plain");
                recyclerView.getContext().startActivity(emailIntent);
            }
        });

//        final boolean isExpanded = position == mExpandedPosition;
//        holder.mDetails.setVisibility(isExpanded?View.VISIBLE:View.GONE);
//        holder.mTextView.setActivated(isExpanded);
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                mExpandedPosition = isExpanded ? -1:position;
//                TransitionManager.beginDelayedTransition(recyclerView);
////                notifyDataSetChanged();
//            }
//        });
        // - get element from your dataset at this position
        // - replace the contents of the view with that element



    }



    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return facultyList.size();
    }


}