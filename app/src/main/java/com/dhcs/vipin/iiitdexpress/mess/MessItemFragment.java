package com.dhcs.vipin.iiitdexpress.mess;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.mess.dummy.DummyContent;
import com.dhcs.vipin.iiitdexpress.mess.dummy.DummyContent.DummyItem;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MessItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_DAY_NUMBER = "day-number";
    private static final String ARG_MESS_TYPE = "mess-type";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private String type;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MessItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MessItemFragment newInstance(int columnCount) {
        MessItemFragment fragment = new MessItemFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mess_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            try{
                JSONArray list = null;
                ArrayList<DummyContent.MessItem> menu = new ArrayList<>();
                if(type.equals("breakfast")){
                    list = ViewPagerMessMenuActivity.MESS_MENU.getJSONObject(ViewPagerMessMenuActivity.SELECTED_DAY).getJSONArray("breakfast");

                }
                else if(type.equals("lunch")){

                    list = ViewPagerMessMenuActivity.MESS_MENU.getJSONObject(ViewPagerMessMenuActivity.SELECTED_DAY).getJSONArray("lunch");

//                    recyclerView.setAdapter(new MessItemRecyclerViewAdapter(DummyContent.LUNCH_ITEMS, mListener));
                }
                else if(type.equals("snack")){
                    list = ViewPagerMessMenuActivity.MESS_MENU.getJSONObject(ViewPagerMessMenuActivity.SELECTED_DAY).getJSONArray("snack");
//                    recyclerView.setAdapter(new MessItemRecyclerViewAdapter(DummyContent.SNACK_ITEMS, mListener));
                }
                else if(type.equals("dinner")){
                    list = ViewPagerMessMenuActivity.MESS_MENU.getJSONObject(ViewPagerMessMenuActivity.SELECTED_DAY).getJSONArray("dinner");
//                    recyclerView.setAdapter(new MessItemRecyclerViewAdapter(DummyContent.DINNER_ITEMS, mListener));
                }

                for(int i=0;i<list.length();i++){
                    menu.add(new DummyContent.MessItem(list.getString(i)));
                }

                recyclerView.setAdapter(new MessItemRecyclerViewAdapter(menu, mListener));
            }
            catch (Exception e){
                e.printStackTrace();
            }


        }
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
        void onListFragmentInteraction(DummyContent.MessItem item);
    }
}
