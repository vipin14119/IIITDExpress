package com.dhcs.vipin.iiitdexpress.faculty;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.timetable.Course;
import com.dhcs.vipin.iiitdexpress.timetable.ViewPagerTimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
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

    private static RecyclerView recyclerView;
    public static SimpleItemRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog mDialog;
    public static String FACULTY_URL = "http://192.168.55.36/get_faculty_json";
    static ArrayList<FacultyCard> facultyCardArrayList;
    public static ArrayList<FacultyCard> dictionaryWords;
    public static ArrayList<FacultyCard> filteredList;
    private EditText searchBox;

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

        facultyCardArrayList = new ArrayList<>();
        filteredList = new ArrayList<>();

//        mRecyclerView = (RecyclerView) findViewById(R.id.faculty_list_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);

//        arrayList.add(new FacultyCard("AV Subramanyam", "Assistant Professor", "PhD(2012), Computer Engineering, Nanyang "));
//        arrayList.add(new FacultyCard("Aasim Khan", "Assistant Professor (SSH)", "PhD in Contemporary India at King's College London"));
//        arrayList.add(new FacultyCard("Alexander Fell(Presently on Leave)", "Assistant Professor (ECE)", "PhD (2012), Indian Institute of Science, Bangalore, India"));
//        arrayList.add(new FacultyCard("Aman Parnami", "Assistant Professor (DES, CSE)", "PhD (2017) Human-Computer Interaction with a minor in Industrial Design, Georgia Institute of Technology."));
//        arrayList.add(new FacultyCard("Amarjeet Singh(Presently on Leave)", "Assistant Professor (CSE, ECE)", "PhD (2009), Electrical Engineering, University of California, Los Angeles, USA"));
//        arrayList.add(new FacultyCard("Anand Srivastava", "Professor (ECE)", "PhD (2003), Indian Institute of Technology Delhi"));
//        arrayList.add(new FacultyCard("Angshul Majumdar", "Assistant Professor (ECE)", "PhD (2012), Electrical & Computer Engg., University of British Columbia"));
//        String [] myDataset = {"AV Subramanyam", "Aasim Khan", "Alexander Fell(Presently on Leave)", "Aman Parmani", "Amarjeet Singh(Presently on Leave)", "Anand Srivastava", "Angshul Majumdar", "Anubha Gupta"};

//        mAdapter = new FacultyAdapter(facultyCardArrayList, mRecyclerView);
//        mRecyclerView.setAdapter(mAdapter);

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

        searchBox = (EditText) findViewById(R.id.ET_searchFaculty);
        recyclerView = findViewById(R.id.faculty_list_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
//        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        assert recyclerView != null;
        mAdapter = new SimpleItemRecyclerViewAdapter(filteredList);
        recyclerView.setAdapter(mAdapter);

        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.getFilter().filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    public void fillList() {
        dictionaryWords = facultyCardArrayList;
        filteredList.addAll(dictionaryWords);
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

    //TODO Subjects Recyclerview Adapter
    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> implements Filterable {

        private ArrayList<FacultyCard> mValues;
        private SimpleItemRecyclerViewAdapter.CustomFilter mFilter;
        public String code;

        public SimpleItemRecyclerViewAdapter(ArrayList<FacultyCard> items) {
            mValues = items;
            mFilter = new SimpleItemRecyclerViewAdapter.CustomFilter(SimpleItemRecyclerViewAdapter.this);
        }

        @Override
        public SimpleItemRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_faculty_cardview_collapsed, parent, false);
            return new SimpleItemRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            final FacultyCard facultyCard = mValues.get(position);
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
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        @Override
        public Filter getFilter() {
            return mFilter;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
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

        public class CustomFilter extends Filter {
            private SimpleItemRecyclerViewAdapter mAdapter;
            private CustomFilter(SimpleItemRecyclerViewAdapter mAdapter) {
                super();
                this.mAdapter = mAdapter;
            }
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                filteredList.clear();
                final FilterResults results = new FilterResults();
                if (constraint.length() == 0) {
                    filteredList.addAll(dictionaryWords);
                } else {
                    final String filterPattern = constraint.toString().toLowerCase().trim();
                    for (final FacultyCard mWords : dictionaryWords) {
                        if (mWords.name.toLowerCase().startsWith(filterPattern)) {
                            filteredList.add(mWords);
                        }
                    }
                }
                System.out.println("Count Number " + filteredList.size());
                results.values = filteredList;
                results.count = filteredList.size();
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                System.out.println("Count Number 2 " + ((ArrayList<FacultyCard>) results.values).size());
                this.mAdapter.notifyDataSetChanged();
            }
        }
    }

    class FacultyJsonTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings)
        {
            String parameter = strings[0];
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                String server_url = getResources().getString(R.string.server_ip) + getResources().getString(R.string.get_faculty_json);
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
            fillList();
            mAdapter = new SimpleItemRecyclerViewAdapter(filteredList);
            recyclerView.setAdapter(mAdapter);
        }
    }

    static void handleFacultyJsonData(String raw){
//        ArrayList<FacultyCard> arrayList = new ArrayList<>();
        try{
            JSONObject dict = new JSONObject(raw);
            JSONArray list = dict.getJSONArray("data");
            for(int i=0;i<list.length();i++){
                JSONObject jsonObject = list.getJSONObject(i);
                facultyCardArrayList.add(new FacultyCard(jsonObject.getString("name"), jsonObject.getString("designation"), jsonObject.getString("education"), jsonObject.getString("email")));
            }

//            RecyclerView.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();

        }
        catch (Exception e){
            e.printStackTrace();
            Log.d("DEBUG", "Got some error in output of faculty json");
        }

    }
}
