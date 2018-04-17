package com.dhcs.vipin.iiitdexpress.silencio;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhcs.vipin.iiitdexpress.R;
import com.dhcs.vipin.iiitdexpress.directory.ViewPagerDirectoryActivity;
import com.dhcs.vipin.iiitdexpress.faculty.FacultyActivity;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class SilencioActivity extends AppCompatActivity {

    private MediaRecorder mediaRecorder = null;
    private final String MSG = "Main Thread Logging";
    private TextView amplitude; // TextFiled for showing textual reading
    private boolean recording_flag = false; // Boolean to check if graph has to be plot or not
    private LineGraphSeries<DataPoint> series;
    private int lastX = 0;  // Pointer for plotting the amplitude
    private static final double amp_ref = 3.27;
    private static final String PREVIOUS_X = "Previous x axis point";
    private static final String PREVIOUS_dB = "Previous noted decibals ";
    private int db_level; // decibel levels
    private boolean PLAY_PAUSE_STATUS = false;
    private Button play_pause_button;
    private ImageView loud_image;
    private WifiManager mWifiManager;
    private TextView current_location;

    private String current_ip;
    private final String FILENAME = "myFingerprinting";
    private TextView location_name;
    private boolean canR, canW;
    private static final String DATETIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static final int MIC_PERMISSION_REQUEST_CODE = 1;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 2;
    public static final int WRITE_EXTERNAL_STORAGE = 0;

    public Queue<NoiseRecord> recordQueue;
    private DateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_silencio);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // add back arrow to toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Intantiate android component
        // Intantiate of android component
        amplitude = (TextView) findViewById(R.id.amp);
        play_pause_button = (Button) findViewById(R.id.play_pause_button);
        loud_image = (ImageView) findViewById(R.id.loud_image);
        current_location = (TextView) findViewById(R.id.current_location);
//        location_name = (TextView)findViewById(R.id.location_name);


        recordQueue = new LinkedList<NoiseRecord>();
        dateFormat = new SimpleDateFormat(DATETIME_FORMAT);

        // Real time graph of Noise in campus

        /**
         *  Initialising the empty graph
         */
        GraphView graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>();
        graph.addSeries(series);
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setMinY(0);  // min value is 0
        viewport.setMaxY(100);  // max value is 32768
        viewport.setMaxX(100);  // 10 units frame
        viewport.setScalable(true); // auto scroll to right
        GridLabelRenderer gridLabel = graph.getGridLabelRenderer();
        gridLabel.setHorizontalAxisTitle("Partition Intervals");
        gridLabel.setVerticalAxisTitle("db Levels");


        // Check for Mic Permission
        checkMicPermission();

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
     * Function called when start button is pressed
     *
     * @param view
     */
    public void play_pause_handler(View view) {
        if (!PLAY_PAUSE_STATUS) {
            startMIC(view);
            PLAY_PAUSE_STATUS = true;
            play_pause_button.setBackground(getResources().getDrawable(R.drawable.ic_pause_circle_filled_white_48dp));
        } else {
            // If Mic is running
            stopMIC(view);
            PLAY_PAUSE_STATUS = false;
            play_pause_button.setBackground(getResources().getDrawable(R.drawable.ic_play_circle_filled_white_48dp));
        }

    }

    public void showGraph(View view) {
        View myView = findViewById(R.id.graph);

        // get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

        // get the final radius for the clipping circle
        float finalRadius = (float) Math.hypot(cx, cy);

        // create the animator for this view (the start radius is zero)
        Animator anim =
                ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

        // make the graph and meter visible and start the animation
        myView.setVisibility(View.VISIBLE);
//        db_meter.setVisibility(View.VISIBLE);
        anim.start();
    }

    public void hideGraph(View view) {
        TextView tview = (TextView) findViewById(R.id.amp);
        tview.setText(getString(R.string.press_start));

        // previously visible view
        final View myView = findViewById(R.id.graph);

        // get the center for the clipping circle
        int cx = myView.getWidth() / 2;
        int cy = myView.getHeight() / 2;

        // get the initial radius for the clipping circle
        float initialRadius = (float) Math.hypot(cx, cy);

        // create the animation (the final radius is zero)
        Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

        // make the view invisible when the animation is done
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // make graph and meter invisible
                myView.setVisibility(View.INVISIBLE);
//                db_meter.setVisibility(View.GONE);
            }
        });

        // start the animation
        anim.start();
    }

    public void startMIC(View view) {

        /**
         * start the MIC if mediaRecorder instance is created else Pops up a message
         */
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.reset();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile("/dev/null"); // Not saving the audio

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                recording_flag = true;

                Thread newT = new Thread(new AudioListener());  // New Thread is created to handle the amplitude fetching and plotting graph
                newT.start();
                /*showGraph(view);*/

            } catch (IOException e) {
                Log.d(MSG, "================== EXCEPTION ================");
                e.printStackTrace();
            }

        }
    }

    public void stopMIC(View view) {
        if (mediaRecorder != null) {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
            recording_flag = false; //reset the flag
            /*hideGraph(view);*/
        } else {
            Log.d(MSG, "================== NO MIC LOCKED ================");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(PREVIOUS_X, lastX);
        savedInstanceState.putInt(PREVIOUS_dB, db_level);
        super.onSaveInstanceState(savedInstanceState);
    }

//    public boolean isExternalWritable(){
//        String state = Environment.getExternalStorageState();
//        return Environment.MEDIA_MOUNTED.equals(state);
//    }
//    public boolean isExternalReadable(){
//        String state = Environment.getExternalStorageState();
//        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
//    }
//    public void externalState(){
//        if (isExternalReadable() && isExternalWritable()){
//            canR = canW = true;
//        }
//        else if (isExternalReadable() && !isExternalWritable()){
//            canR = true;
//            canW = false;
//        }
//        else{
//            canR = canW = false;
//        }
//    }
//    public File getAlbumStorageDir(String albumName) {
//        // Get the directory for the user's public pictures directory.
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_DOCUMENTS), albumName);
//        if (!file.mkdirs()) {
//            file.mkdirs();
//        }
//        return file;
//    }
//    public void bind_ip_value_to_location(View view){
//        Log.d(MSG, "======================== YES I HAVE BEEEN  CALLED ===============================");
//        FileOutputStream fos;
//        String name = location_name.getText().toString();
//
//        File file;
//        externalState();
//        if(canW && canR){
//            FileOutputStream outputStream;
//            try {
//                file = new File(getAlbumStorageDir("Files Generated"), FILENAME+".txt");
//
//                outputStream = new FileOutputStream(file, true);
//                outputStream.write(name.getBytes());
//                outputStream.write(" == ".getBytes());
//                outputStream.write(current_ip.getBytes());
//                outputStream.write("\n".getBytes());
//                outputStream.close();
//                makeSnackbar("Saved");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        else{
//            makeSnackbar("Cannot write to external storage now");
//        }
//
//    }
//
//    public void makeSnackbar(String snackbarText) {
//        Snackbar.make(getWindow().getDecorView().getRootView(), snackbarText, Snackbar.LENGTH_LONG)
//                .setAction("Dismiss", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {}
//                })
//                .show();
//    }
//
//    private class IPMapper implements Runnable{
//
//        public void get_gateway_ip(){
//
//            mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//
//            if(mWifiManager.isWifiEnabled()){
//                WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//                if (wifiInfo.getNetworkId() == -1){
//                    // Not connected to an access point
//                    makeSnackbar("You are not connected to any access point");
//                }
//                else{
//                    current_ip = LocationMapper.get_location(wifiInfo.getBSSID());
//                    Log.d(" MSG ", current_ip);
//                }
//            }
//            else{
//                makeSnackbar("Wifi not enabled");
//            }
//        }
//        @Override
//        public void run() {
//            while(true){
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        float av_db = 0;
//
//                        if(recordQueue.size() > 100){
//                            NoiseRecord record = recordQueue.remove();
//                            av_db += record.getDb_level();
//                            String start_date = record.getDate();
//                            for(int i=0;i<98;i++){
//                                av_db += recordQueue.remove().getDb_level();
//                            }
//                            record = recordQueue.remove();
//                            av_db += record.getDb_level();
//                            String end_date = record.getDate();
//                            av_db /= 100;
//                            NoiseRecordBundle noiseRecordBundle = new NoiseRecordBundle(record.getPlace(), av_db, start_date, end_date);
//                            if (!noiseRecordBundle.getPlace().equals("Anonymous")){
//                                try {
//                                    String encodedUrl = "&username=" + URLEncoder.encode(CURRENT_LOGGED_USER, "UTF-8") +
//                                            "&location=" + URLEncoder.encode(noiseRecordBundle.getPlace(), "UTF-8") +
//                                            "&db_level=" + URLEncoder.encode(String.valueOf(noiseRecordBundle.getAvg_db()), "UTF-8") +
//                                            "&start_time=" + URLEncoder.encode(String.valueOf(noiseRecordBundle.getStart()), "UTF-8") +
//                                            "&end_time=" + URLEncoder.encode(String.valueOf(noiseRecordBundle.getEnd()), "UTF-8");
//                                    mWifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//                                    if(mWifiManager.isWifiEnabled()){
//                                        WifiInfo wifiInfo = mWifiManager.getConnectionInfo();
//                                        if (wifiInfo.getNetworkId() == -1){
//                                            makeSnackbar("You are not connected to any access point");
//                                        }
//                                        else{
//                                            new PostTask().execute(encodedUrl);
//                                        }
//                                    }
//                                    else{
//                                        makeSnackbar("WiFi not enabled");
//                                    }
//                                } catch (UnsupportedEncodingException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }
//                        get_gateway_ip();
//                        current_location.setText(current_ip);
//                    }
//                });
//                try {
//                    // Sleep for 600 ms for next value
//                    Thread.sleep(5000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
//    class PostTask extends AsyncTask<String, Void, String> {
//        @Override
//        protected String doInBackground(String... strings)
//        {
//            String parameter = strings[0];
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            try {
//
//                URL url = new URL(POST_URL);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                urlConnection.setRequestProperty("Accept", "application/x-www-form-urlencoded");
//
//                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
//                writer.write(parameter);
//                writer.close();
//                int response_code = urlConnection.getResponseCode();
//                Log.d("DEBUGGER", "****************** RESPONSE CODE = "+response_code);
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    return null;
//                }
//
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//                String inputLine;
//                while ((inputLine = reader.readLine()) != null)
//                    buffer.append(inputLine);
//                if (buffer.length() == 0) {
//                    // Stream was empty. No point in parsing.
//                    return null;
//                }
//                String return_value = buffer.toString();
//                Log.d("RETURN", return_value);
//                return return_value;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e("TAG RESPONSE", "Error closing stream", e);
//                    }
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            /*mDialog = new ProgressDialog(LoginActivity.this);
//            mDialog.setTitle("Signing You In");
//            mDialog.show();*/
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            /*handle_login(s);
//            mDialog.dismiss();*/
//        }
//    }

    /**
     * private class for fetching amplitude and mapping graph
     */
    private class AudioListener implements Runnable {
        private final String MSG = "AudioListener Logging: ";

        /**
         * @return current amplitude if instance of MIC exist
         */
        public int getAmplitude() {
            if (mediaRecorder != null) {
                double y = 20 * Math.log10(mediaRecorder.getMaxAmplitude() / amp_ref);
                return (int) y;
            } else {
                return -1;
            }
        }

        @Override
        public void run() {

            // if recording flag is true then keep mapping graph
            while (recording_flag) {
                int raw_amp_val = getAmplitude();
                final int amp_val;
                if (raw_amp_val < 0) {
                    amp_val = 0;
                } else {
                    amp_val = raw_amp_val;
                }
                db_level = amp_val;
                final String amp_val_string = amp_val + "";
                SilencioActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        amplitude.setText(amp_val_string);
//                        db_meter.setProgress(amp_val);

                        /*
                        Provide Style to meter according to decibel values
                         */
                        if (amp_val <= 50) {
                            loud_image.setBackground(getResources().getDrawable(R.drawable.sound_level_1));
//                            db_meter.setProgressDrawable(getDrawable(R.drawable.greenprogress));
                        }
                        if (amp_val > 50 && amp_val <= 70) {
                            loud_image.setBackground(getResources().getDrawable(R.drawable.sound_level_2));
//                            db_meter.setProgressDrawable(getDrawable(R.drawable.orangeprogress));
                        }
                        if (amp_val > 70) {
                            loud_image.setBackground(getResources().getDrawable(R.drawable.sound_level_3));
//                            db_meter.setProgressDrawable(getDrawable(R.drawable.redprogress));
                        }
                        series.appendData(new DataPoint(lastX++, amp_val), true, 100);

                        NoiseRecord noiseRecord = new NoiseRecord(current_ip, (float) amp_val, dateFormat.format(new Date()));
                        recordQueue.add(noiseRecord);
                    }
                });
                Log.d(MSG, " === AMPLITUDE === " + amp_val_string);
//                long startTime = System.nanoTime();
//                long endTime = System.nanoTime();
//                long duration = (endTime - startTime)/1000000;
//                Log.d("MSG", " Time took is ============== "+duration);


                try {
                    // Sleep for 600 ms for next value
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d(MSG, "======== Thread Destroyed =========");
        }
    }

    private void checkMicPermission() {
        String permission = Manifest.permission.RECORD_AUDIO;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, MIC_PERMISSION_REQUEST_CODE);
        }
    }

    private void checkStoragePermission() {
        String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MIC_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkStoragePermission();
                }
                else {
                    makePermissionCancelDialog();
                }
            }
            case STORAGE_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {}
                else {
                    makePermissionCancelDialog();
                }
            }
        }
    }

    public void makePermissionCancelDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(SilencioActivity.this)
                .setTitle("And so it ends....")
                .setMessage("We are sorry to inform you that we cannot continue without this permission. Press OK to exit the application.")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                        System.exit(0);
                    }
                });
        builder.show();
    }

    public void startActivitySoundMap(View view){
        Intent intent = new Intent(this, SoundMapActivity.class);
        startActivity(intent);
    }

}
