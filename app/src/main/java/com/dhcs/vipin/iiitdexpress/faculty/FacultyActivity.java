package com.dhcs.vipin.iiitdexpress.faculty;

import android.app.ProgressDialog;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

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

public class FacultyActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mDialog;
    public static String FACULTY_URL = "http://192.168.55.36/get_faculty_json";
    ArrayList<FacultyCard> facultyCardArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.faculty_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        facultyCardArrayList = new ArrayList<>();
//        arrayList.add(new FacultyCard("AV Subramanyam", "Assistant Professor", "PhD(2012), Computer Engineering, Nanyang "));
//        arrayList.add(new FacultyCard("Aasim Khan", "Assistant Professor (SSH)", "PhD in Contemporary India at King's College London"));
//        arrayList.add(new FacultyCard("Alexander Fell(Presently on Leave)", "Assistant Professor (ECE)", "PhD (2012), Indian Institute of Science, Bangalore, India"));
//        arrayList.add(new FacultyCard("Aman Parnami", "Assistant Professor (DES, CSE)", "PhD (2017) Human-Computer Interaction with a minor in Industrial Design, Georgia Institute of Technology."));
//        arrayList.add(new FacultyCard("Amarjeet Singh(Presently on Leave)", "Assistant Professor (CSE, ECE)", "PhD (2009), Electrical Engineering, University of California, Los Angeles, USA"));
//        arrayList.add(new FacultyCard("Anand Srivastava", "Professor (ECE)", "PhD (2003), Indian Institute of Technology Delhi"));
//        arrayList.add(new FacultyCard("Angshul Majumdar", "Assistant Professor (ECE)", "PhD (2012), Electrical & Computer Engg., University of British Columbia"));
//        String [] myDataset = {"AV Subramanyam", "Aasim Khan", "Alexander Fell(Presently on Leave)", "Aman Parmani", "Amarjeet Singh(Presently on Leave)", "Anand Srivastava", "Angshul Majumdar", "Anubha Gupta"};

        mAdapter = new FacultyAdapter(facultyCardArrayList, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        String username = "vipin14119";
        String password = "vipin14119";
        try{
            String encodedUrl = "&username=" + URLEncoder.encode(username, "UTF-8") +
                    "&password=" + URLEncoder.encode(password, "UTF-8");
            new FacultyJsonTask().execute(encodedUrl);
        }
        catch (Exception e){
            Log.d("DEBUG", "Some error occured in starting Async Task");
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    private class WifiManager {
    }

    class FacultyJsonTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings)
        {
            String parameter = strings[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                URL url = new URL(FACULTY_URL);

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
            mDialog = new ProgressDialog(FacultyActivity.this);
            mDialog.setMessage("Logging you in");
            mDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("DEBUG", s);
            handleFacultyJsonData(s);
            mDialog.dismiss();
        }
    }


    void handleFacultyJsonData(String raw){
//        ArrayList<FacultyCard> arrayList = new ArrayList<>();
        try{
            JSONArray list = new JSONArray(raw);
            for(int i=0;i<list.length();i++){
                JSONObject jsonObject = list.getJSONObject(i);
                facultyCardArrayList.add(new FacultyCard(jsonObject.getString("name"), jsonObject.getString("designation"), jsonObject.getString("education")));
            }

            mRecyclerView.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("DEBUG", "Got some error in output of faculty json");
        }

    }
}
