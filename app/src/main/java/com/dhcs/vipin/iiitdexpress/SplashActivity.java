package com.dhcs.vipin.iiitdexpress;

import android.app.Activity;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity{
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    private Timer timer;
    private ProgressBar progressBar;
    private int i=0;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, DashboardActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
//        progressBar=(ProgressBar)findViewById(R.id.progressBar);
//        progressBar.setProgress(0);
//        textView=(TextView)findViewById(R.id.textView);
//        textView.setText("");
//
//        final long period = 100;
//        timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //this repeats every 100 ms
//                if (i<100){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText(String.valueOf(i)+"%");
//                        }
//                    });
//                    progressBar.setProgress(i);
//                    i++;
//                }else{
//                    //closing the timer
//                    timer.cancel();
//                    Intent intent =new Intent(SplashActivity.this,DashboardActivity.class);
//                    startActivity(intent);
//                    // close this activity
//                    finish();
//                }
//            }
//        }, 0, period);
    }
}
