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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcs.vipin.iiitdexpress.R;
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
    private static final String LEADERBOARD_URL = "http://192.168.55.36/"+"get_mess_menu";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_mess_menu);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final Spinner spinner = (Spinner) findViewById(R.id.mess_spinner);
        setSupportActionBar(toolbar);
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
                Toast.makeText(ViewPagerMessMenuActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

//        new FetchLeaderBoard().execute();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_view_pager_mess_menu, menu);
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

    public static String getResponseString(String request_url, String parameter) throws IOException {

        String JsonResponse = null;
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        URL url = new URL(request_url);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(5000);
        // is output buffer writter
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        urlConnection.setRequestProperty("Accept", "application/x-www-form-urlencoded");
        Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
        writer.write(parameter);
        writer.close();

        int res_code = urlConnection.getResponseCode();
//        printDebugMsg("Response Code = "+res_code);

        switch (res_code) {
            case 200:
                // aage ja mere sher
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));
                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                break;
            case 404:
                // page not found error
                break;
            case 500:
                // server error
                break;
            default:
                //any fucking error
                break;
        }
        urlConnection.disconnect();
        if (reader != null) {
            try {
                reader.close();
            } catch (final IOException e) {
//                printErrorMsg("Get IO Error in Challenge Overview task\n"+e);
            }
        }
        return JsonResponse;

    }

    class FetchLeaderBoard extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings)
        {
            try {
                String parameter = "&username=" + URLEncoder.encode("vipin", "UTF-8") +
                        "&password=" + URLEncoder.encode("pass1234", "UTF-8");
                return getResponseString(LEADERBOARD_URL, parameter);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mDialog = new ProgressDialog(ViewPagerMessMenuActivity.this);
            mDialog.setMessage("Hold on , just fetching Top 20 students");
            mDialog.show();
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s!=null){
                try{
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    Iterator keysToIterator = jsonObject1.keys();
                    while (keysToIterator.hasNext()){
                        String key = (String) keysToIterator.next();
                        hashMap.put(key, jsonObject1.getJSONObject(key));
                    }

//                    JSONArray jsonArray = new JSONArray(s);
//                    ArrayList<DummyContent.MessItem> localList = new ArrayList<>();
//                    for (int i=0;i<jsonArray.length();i++){
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        localList.add(new DummyContent.MessItem(jsonObject.getString("username"), jsonObject.getInt("rank"), (float)jsonObject.getDouble("score"), jsonObject.getString("profile")));
//                    }
//                    leaderBoardArrayList = localList;
//                    printInfoMsg("Got leader board correctly");
//                    leaderBoardAdapter = new LeaderBoardAdapter(leaderBoardArrayList);
//                    recyclerView.setAdapter(leaderBoardAdapter);
                    mDialog.dismiss();
                }
                catch (Exception e){
                    new AlertDialog.Builder(ViewPagerMessMenuActivity.this)
                            .setTitle("Leaderboard activity error")
                            .setMessage("There was some error in server please try again later")
                            .setPositiveButton(android.R.string.ok,  new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setCancelable(false)
                            .setIcon(R.drawable.ic_bug_report_black_24dp)
                            .show();
                }
            }
            else{
                new AlertDialog.Builder(ViewPagerMessMenuActivity.this)
                        .setTitle("Leaderboard activity error")
                        .setMessage("There was some error in server please try again later")
                        .setPositiveButton(android.R.string.ok,  new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setCancelable(false)
                        .setIcon(R.drawable.ic_bug_report_black_24dp)
                        .show();
            }
        }
    }
}
