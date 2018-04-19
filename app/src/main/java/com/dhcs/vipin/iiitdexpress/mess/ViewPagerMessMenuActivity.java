package com.dhcs.vipin.iiitdexpress.mess;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcs.vipin.iiitdexpress.Config;
import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.faculty.FacultyActivity;
import com.dhcs.vipin.iiitdexpress.mess.dummy.DummyContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ViewPagerMessMenuActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private ProgressDialog mDialog;
    public static HashMap<String, JSONObject> hashMap = new HashMap<>();
    private SectionsPagerAdapter mSectionsPagerAdapter;


    public static JSONObject MESS_MENU;
    public static String SELECTED_DAY;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public static String MESS_URL = "http://192.168.32.130/get_mess_menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_mess_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Spinner spinner = (Spinner) findViewById(R.id.mess_spinner);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        // Spinner Code

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(ViewPagerMessMenuActivity.this, R.layout.custom_spinner_item, getResources().getStringArray(R.array.weekday_names) );
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                SELECTED_DAY = spinner.getSelectedItem().toString();
                mViewPager.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String username = Config.USERNAME;
        String password = Config.PASSWORD;
        try{
            String encodedUrl = "&username=" + URLEncoder.encode(username, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");
            new MessMenuJsonTask().execute(encodedUrl);
        }
        catch (Exception e){
            Log.d("DEBUG", "Some error occured in starting Async Task");
            e.printStackTrace();
        }

//        new FetchLeaderBoard().execute();
//        new MessMenuJsonTask().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_view_pager_mess_menu, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(Object object){
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            MessItemFragment f;
            Bundle args;
            switch (position){
                case 0:
                    f = new MessItemFragment();
                    // Supply index input as an argument.
                    args = new Bundle();
                    args.putString("type", "breakfast");
                    f.setArguments(args);
                    return f;
                case 1:
                    f = new MessItemFragment();
                    // Supply index input as an argument.
                    args = new Bundle();
                    args.putString("type", "lunch");
                    f.setArguments(args);
                    return f;
                case 2:
                    f = new MessItemFragment();
                    // Supply index input as an argument.
                    args = new Bundle();
                    args.putString("type", "snack");
                    f.setArguments(args);
                    return f;
                case 3:
                    f = new MessItemFragment();
                    // Supply index input as an argument.
                    args = new Bundle();
                    args.putString("type", "dinner");
                    f.setArguments(args);
                    return f;
                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }

    class MessMenuJsonTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings)
        {
            String parameter = strings[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                String url_string = getResources().getString(R.string.server_ip) + getResources().getString(R.string.get_mess_menu);
                URL url = new URL(url_string);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestProperty("Accept", "application/x-www-form-urlencoded");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(parameter);
                writer.close();
                int response_code = urlConnection.getResponseCode();
                Log.d("DEBUGGER", "****************** RESPONSE CODE = "+response_code);

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine);
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                String return_value = buffer.toString();
                Log.d("DEBUG", return_value);
                return return_value;
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("TAG RESPONSE", "Error closing stream", e);
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(ViewPagerMessMenuActivity.this);
            mDialog.setMessage("Fetching Mess data");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DEBUG", s);
            handleMessJsonData(s);
            mDialog.dismiss();
        }
    }

    void handleMessJsonData(String s){
        try{
            JSONObject raw = new JSONObject(s);
            MESS_MENU = raw.getJSONObject("data");

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
