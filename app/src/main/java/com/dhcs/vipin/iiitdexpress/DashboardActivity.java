package com.dhcs.vipin.iiitdexpress;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.dhcs.vipin.iiitdexpress.directory.ViewPagerDirectoryActivity;
import com.dhcs.vipin.iiitdexpress.faculty.FacultyActivity;
import com.dhcs.vipin.iiitdexpress.mess.ViewPagerMessMenuActivity;
import com.dhcs.vipin.iiitdexpress.silencio.SilencioActivity;
import com.dhcs.vipin.iiitdexpress.timetable.Course;
import com.dhcs.vipin.iiitdexpress.timetable.ViewPagerTimeTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
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

}
