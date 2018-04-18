package com.dhcs.vipin.iiitdexpress.directory;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.directory.dummy.DummyContent;
import com.dhcs.vipin.iiitdexpress.mess.ViewPagerMessMenuActivity;

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

public class ViewPagerDirectoryActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    public static HashMap<String, ArrayList<DummyContent.PersonItem>> DIRECTORY_MAP = new HashMap<>();

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_directory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        initDirectoryMap();

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        String username = "vipin14119";
        String password = "vipin14119";
        try{
            String encodedUrl = "&username=" + URLEncoder.encode(username, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");
            new DirectoryTask().execute(encodedUrl);
        }
        catch (Exception e){
            Log.d("DEBUG", "Some error occured in starting Async Task");
            e.printStackTrace();
        }


    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_view_pager_directory, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
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
            View rootView = inflater.inflate(R.layout.fragment_view_pager_directory, container, false);
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
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            DirectoryListFragment f;
            Bundle args;
            if (position == 0) {
                f = new DirectoryListFragment();
                // Supply index input as an argument.
                args = new Bundle();
                args.putString("type", "fms");
                f.setArguments(args);
                return f;
            }
            else if(position == 1){
                f = new DirectoryListFragment();
                // Supply index input as an argument.
                args = new Bundle();
                args.putString("type", "acad");
                f.setArguments(args);
                return f;
            }
            else if(position == 2){
                f = new DirectoryListFragment();
                // Supply index input as an argument.
                args = new Bundle();
                args.putString("type", "serv");
                f.setArguments(args);
                return f;
            }
            else{
                return PlaceholderFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

    class DirectoryTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings)
        {
            String parameter = strings[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                String server_url = getResources().getString(R.string.server_ip) + getResources().getString(R.string.get_directory_json);
                URL url = new URL(server_url);

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
            mDialog = new ProgressDialog(ViewPagerDirectoryActivity.this);
            mDialog.setMessage("Logging you in");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DEBUG", s);
            handleDirectoryJsonData(s);
            mDialog.dismiss();
        }
    }

    public void handleDirectoryJsonData(String string){
        try{
            JSONObject jsonObject = new JSONObject(string);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray jsonArray;
            String [] types = {"acad", "fms", "serv"};
            for(String type:types){
                jsonArray = data.getJSONArray(type);
                ArrayList<DummyContent.PersonItem> arrayList;
                arrayList = new ArrayList<>();

                for(int i=0;i<jsonArray.length();i++){
                    JSONObject object = jsonArray.getJSONObject(i);
                    arrayList.add(new DummyContent.PersonItem(object.getString("name"),object.getString("designation"), object.getString("room"), object.getString("email") ));
                }
                DIRECTORY_MAP.put(type, arrayList);
//                mViewPager.getAdapter().notifyDataSetChanged();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    public void initDirectoryMap(){
        String [] types = {"acad", "fms", "serv"};
        for(String type:types){

            ArrayList<DummyContent.PersonItem> arrayList = new ArrayList<>();

            DIRECTORY_MAP.put(type, arrayList);

        }
    }
}
