package com.dhcs.vipin.iiitdexpress;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.dhcs.vipin.iiitdexpress.directory.ViewPagerDirectoryActivity;
import com.dhcs.vipin.iiitdexpress.faculty.FacultyActivity;
import com.dhcs.vipin.iiitdexpress.mess.ViewPagerMessMenuActivity;
import com.dhcs.vipin.iiitdexpress.silencio.SilencioActivity;
import com.dhcs.vipin.iiitdexpress.timetable.ViewPagerTimeTableActivity;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

    }

    public void startActivityMessMenu(View view){
        Intent intent = new Intent(this, ViewPagerMessMenuActivity.class);
        startActivity(intent);
    }

    public void startActivityTimeTable(View view){
        Intent intent = new Intent(this, ViewPagerTimeTableActivity.class);
        startActivity(intent);
    }

    public void startActivitySilencio(View view){
        Intent intent = new Intent(this, SilencioActivity.class);
        startActivity(intent);
    }

    public void startActivityDirectory(View view){
        Intent intent = new Intent(this, ViewPagerDirectoryActivity.class);
        startActivity(intent);
    }

    public void startActivityFaculty(View view){
        Intent intent = new Intent(this, FacultyActivity.class);
        startActivity(intent);
    }

    public void startActivityPlacement(View view){
        Intent intent = new Intent(this, PlacementActivity.class);
        startActivity(intent);
    }
}
