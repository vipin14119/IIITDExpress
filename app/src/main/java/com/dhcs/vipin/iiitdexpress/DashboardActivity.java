package com.dhcs.vipin.iiitdexpress;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.dhcs.vipin.iiitdexpress.directory.ViewPagerDirectoryActivity;
import com.dhcs.vipin.iiitdexpress.faculty.FacultyActivity;
import com.dhcs.vipin.iiitdexpress.mess.ViewPagerMessMenuActivity;
import com.dhcs.vipin.iiitdexpress.silencio.SilencioActivity;
import com.dhcs.vipin.iiitdexpress.timetable.Course;
import com.dhcs.vipin.iiitdexpress.timetable.ViewPagerTimeTableActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import javax.net.ssl.HttpsURLConnection;

public class DashboardActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        toolbar = (Toolbar)findViewById(R.id.dashboard_toolbar);
        setSupportActionBar(toolbar);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        TextView mDate = (TextView)findViewById(R.id.today_date);
        TextView mWelcome = (TextView)findViewById(R.id.welcome);

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        String current_day;
        current_day = calendar.get(Calendar.DATE)+" "+calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())+" "+ calendar.get(Calendar.YEAR);
//        Log.d("DEBUG", calendar.get(Calendar.YEAR)+"");
        mDate.setText(current_day);
        mWelcome.setText(Config.USERNAME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.navigation_logout) {
//            Toast.makeText(MainActivity.this, "Action clicked", Toast.LENGTH_LONG).show();
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish();
                    }
                });
    }

    public void startActivityMessMenu(View view) {
        Intent intent = new Intent(this, ViewPagerMessMenuActivity.class);
        startActivity(intent);
    }

    public void startActivityTimeTable(View view) {
        Intent intent = new Intent(this, ViewPagerTimeTableActivity.class);
//        intent.putExtra("ALL_COURSES", allCourses);
        startActivity(intent);
    }

    public void startActivitySilencio(View view) {
        Intent intent = new Intent(this, SilencioActivity.class);
        startActivity(intent);
    }

    public void startActivityDirectory(View view) {
        Intent intent = new Intent(this, ViewPagerDirectoryActivity.class);
        startActivity(intent);
    }

    public void startActivityFaculty(View view) {
        Intent intent = new Intent(this, FacultyActivity.class);
        startActivity(intent);
    }

    public void startActivityPlacement(View view) {
        Intent intent = new Intent(this, PlacementActivity.class);
        startActivity(intent);
    }

    public void startFacebook(View view) {
        Intent intent = getOpenFacebookIntent(DashboardActivity.this);
        startActivity(intent);
    }

    public void startTwitter(View view){
        Intent intent = getOpenTwitterIntent(DashboardActivity.this);
        startActivity(intent);
    }

    public void startEsyaActivity(View view){
        Intent intent = getOpenEsyaIntent(DashboardActivity.this);
        startActivity(intent);
    }

    public void startOdyssey(View view){
        Intent intent = getOpenOdysseyIntent(DashboardActivity.this);
        startActivity(intent);
    }

    public static Intent getOpenFacebookIntent(Context context) {

        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/IIITDelhi/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://touch.facebook.com/androiddevs"));
        }
    }

    public static Intent getOpenTwitterIntent(Context context) {

        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/IIITDelhi"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/IIITDelhi"));
        }
    }

    public static Intent getOpenEsyaIntent(Context context) {

        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://esya.iiitd.edu.in/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://esya.iiitd.edu.in/"));
        }
    }

    public static Intent getOpenOdysseyIntent(Context context) {

        try {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://odyssey.iiitd.edu.in/"));
        } catch (Exception e) {
            return new Intent(Intent.ACTION_VIEW, Uri.parse("http://odyssey.iiitd.edu.in/"));
        }
    }

    @Override
    public void onBackPressed()
    {
        // code here to show dialog
//        super.onBackPressed();  // optional depending on your needs
    }

}
