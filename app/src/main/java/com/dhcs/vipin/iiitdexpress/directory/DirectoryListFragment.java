package com.dhcs.vipin.iiitdexpress.directory;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.directory.dummy.DummyContent;
import com.dhcs.vipin.iiitdexpress.directory.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class DirectoryListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private String type;

    public DirectoryListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DirectoryListFragment newInstance(int columnCount) {
        DirectoryListFragment fragment = new DirectoryListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
            type = getArguments().getString("type");
        }
        Log.d("DEBUG", "OnCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            Log.d("DEBUG", "YES I CAME HERE ********* with "+type);
            Log.d("DEBUG", ViewPagerDirectoryActivity.DIRECTORY_MAP.toString());
            Set set = ViewPagerDirectoryActivity.DIRECTORY_MAP.entrySet();
            Iterator iterator = set.iterator();
            while(iterator.hasNext()) {
                Map.Entry mentry = (Map.Entry)iterator.next();
                System.out.print("key is: "+ mentry.getKey() + " & Value is: ");
                System.out.println(mentry.getValue());
            }
            ArrayList<DummyContent.PersonItem> arrayList = ViewPagerDirectoryActivity.DIRECTORY_MAP.get(type);
            MyPersonRecyclerViewAdapter adapter = new MyPersonRecyclerViewAdapter(arrayList, mListener);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("DEBUG", "I came here");
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyContent.PersonItem item);
    }
}
