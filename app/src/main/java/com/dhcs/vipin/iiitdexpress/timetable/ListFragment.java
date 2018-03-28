package com.dhcs.vipin.iiitdexpress.timetable;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.dummy.DummyContent;
import com.dhcs.vipin.iiitdexpress.dummy.DummyContent.DummyItem;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ListFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public static Course[] myCourses = {
            new Course("PS", "monday", "9:00-10:00", "C01"),
            new Course("PS", "wednesday", "9:30-10:30", "C01"),
            new Course("PS", "thursday", "9:00-10:00", "C01"),
            new Course("DSA", "tuesday", "9:30-10:30", "C01"),
            new Course("DSA", "friday", "10:30-11:30", "C01"),
            new Course("ADA", "monday", "10:00-11:00", "C21"),
            new Course("ADA", "wednesday", "9:00-10:00", "C21"),
            new Course("ADA", "thursday", "12:30-13:30", "C21"),
            new Course("DBM", "tuesday", "10:00-11:00", "C21"),
            new Course("DBM", "friday", "10:00-11:00", "C21"),
            new Course("FW", "tuesday", "11:30-12:30", "C02"),
            new Course("FW", "thursday", "11:30-12:30", "C02"),
            new Course("FW", "friday", "11:30-12:30", "C02")
    };
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ListFragment newInstance(int columnCount) {
        ListFragment fragment = new ListFragment();
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
        }

        final Spinner spinner = (Spinner) getActivity().findViewById(R.id.timetable_spinner);

        // Spinner Code

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, getResources().getStringArray(R.array.weekday_names) );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                String day = spinner.getSelectedItem().toString().toLowerCase();
                ArrayList<Course> c = new ArrayList<>();
                for(int x = 0; x < myCourses.length; x++) {
                    if(day.equals(myCourses[x].day)) {
                        c.add(myCourses[x]);
                    }
                }
                Course[] c1 = c.toArray(new Course[c.size()]);
                mAdapter = new CourseAdapter(c1, mRecyclerView);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);



        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

//        Course[] myCourses = {
//                new Course("PS", "monday", "9:00-10:00", "C01"),
//                new Course("PS", "wednesday", "9:30-10:30", "C01"),
//                new Course("PS", "thursday", "9:00-10:00", "C01"),
//                new Course("DSA", "tuesday", "9:30-10:30", "C01"),
//                new Course("DSA", "friday", "10:30-11:30", "C01"),
//                new Course("ADA", "monday", "10:00-11:00", "C21"),
//                new Course("ADA", "wednesday", "9:00-10:00", "C21"),
//                new Course("ADA", "thursday", "12:30-13:30", "C21"),
//                new Course("DBM", "tuesday", "10:00-11:00", "C21"),
//                new Course("DBM", "friday", "10:00-11:00", "C21"),
//                new Course("FW", "tuesday", "11:30-12:30", "C02"),
//                new Course("FW", "thursday", "11:30-12:30", "C02"),
//                new Course("FW", "friday", "11:30-12:30", "C02"),
//                new Course("ALG", "tuesday", "10:00-11:30", "A006"),
//                new Course("ALG", "friday", "10:00-11:30", "A006"),
//                new Course("CV", "monday", "11:30-13:00", "C24"),
//                new Course("CV", "wednesday", "11:30-13:00", "C24"),
//                new Course("GDD", "tuesday", "14:30-16:00", "L21"),
//                new Course("GDD", "friday", "14:30-16:00", "L21"),
//                new Course("VR", "monday", "14:30-16:00", "C13"),
//        };

        mAdapter = new CourseAdapter(myCourses, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        // Set the adapter
//        if (view instanceof RecyclerView) {
//            Context context = view.getContext();
//            RecyclerView recyclerView = (RecyclerView) view;
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
//                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
//            }
//            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(DummyContent.ITEMS, mListener));
//        }



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
