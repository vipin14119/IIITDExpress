package com.dhcs.vipin.iiitdexpress.timetable;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.dhcs.vipin.iiitdexpress.Config;
import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.dummy.DummyContent;
import com.dhcs.vipin.iiitdexpress.dummy.DummyContent.DummyItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

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

    private static RecyclerView mRecyclerView;
    private static RecyclerView.Adapter mAdapter;
    private static RecyclerView.LayoutManager mLayoutManager;

    public static ArrayList<Course> dayCourses;

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

        dayCourses = new ArrayList<Course>();

        // Spinner Code
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, getResources().getStringArray(R.array.weekday_names) );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = spinner.getSelectedItem().toString();
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                String day = s.substring(0, Math.min(s.length(), 3)).toUpperCase();
                Log.d("Day : ", day);
                final JSONObject mainObject = new JSONObject();
                try {
                    mainObject.put("username", Config.USERNAME);
                    mainObject.put("password", Config.PASSWORD);
                    mainObject.put("day", day);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    dayCourses.clear();
                    new GetDayCoursesTask().execute(mainObject.toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    public static void fillList1() {
        mRecyclerView.setLayoutManager(mLayoutManager);
        Course[] c1 = dayCourses.toArray(new Course[dayCourses.size()]);
        mAdapter = new CourseAdapter(c1, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }

    private static class GetDayCoursesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL("http://18.188.28.89/get_day_courses");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(params[0]);
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return "false : " + responseCode;
                }
            }
            catch(Exception e){
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", result);
            try {
                JSONObject obj = new JSONObject(result);
                JSONArray arr = obj.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonobject = arr.getJSONObject(i);
                    String code = jsonobject.getString("course");
                    String name = jsonobject.getString("name");
                    String room = jsonobject.getString("room");
                    String st = jsonobject.getString("start_time");
                    String et = jsonobject.getString("end_time");
                    dayCourses.add(new Course(code,name,"",st+"-"+et,room,true));
                    Log.d("Course : ", code + " " + name + " " + room + " " + st + "-" + et);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ListFragment.fillList1();
        }
    }
}
