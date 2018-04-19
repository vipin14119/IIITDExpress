package com.dhcs.vipin.iiitdexpress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcs.vipin.iiitdexpress.directory.ViewPagerDirectoryActivity;
import com.dhcs.vipin.iiitdexpress.mess.ViewPagerMessMenuActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.jaredrummler.android.widget.AnimatedSvgView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private ProgressDialog mDialog;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.

        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_WIDE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                    // ...
                }
            }
        });

//        AnimatedSvgView svgView = (AnimatedSvgView) findViewById(R.id.animated_svg_view);
//        svgView.start();

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
//            updateUI(account);
            Log.d("DEBUG", "IM DONE NOW " + account.getEmail());
            String username = account.getEmail();
//            String password = account.getIdToken();
            Config.USERNAME = username;
//            Config.PASSWORD = password;

            final JSONObject mainObject = new JSONObject();
            try {
                mainObject.put("username", Config.USERNAME);
                mainObject.put("password", Config.PASSWORD);
                new RegisterTask().execute(mainObject.toString()).get();
            } catch (Exception e) {
                e.printStackTrace();
            }

//            try{
//                String encodedUrl = "&username=" + URLEncoder.encode(Config.USERNAME, "UTF-8") +
//                        "&password=" + URLEncoder.encode(Config.PASSWORD, "UTF-8");
//                new SignUpTask().execute(encodedUrl);
//            }
//            catch (Exception e){
//                Log.d("DEBUG", "Some error occured in starting Async Task");
//                e.printStackTrace();
//            }

            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("DEBUG", "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
            Log.d("DEBUG", "IM NOT DONE");
        }
    }

    public void startDashboardActivity(View view) {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Log.d("DEBUG", "IM ALREADY LOGGED IN");
            Log.d("DEBUG", account.getEmail());
            String username = account.getEmail();
//            String password = account.getIdToken();
            Config.USERNAME = username;
//            Config.PASSWORD = password;
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
        }
//        updateUI(account);
    }

    private class RegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                String url_string = getResources().getString(R.string.server_ip) + getResources().getString(R.string.register_user);
                URL url = new URL(url_string);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(params[0]);
                writer.flush();
                writer.close();
                os.close();
                int responseCode=conn.getResponseCode();
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    in.close();
                    return sb.toString();
                }
                else {
                    return "false : " + responseCode;
                }
            }
            catch(Exception e){
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("TAG", result);
            handleSignUpData(result);
        }
    }

//    class SignUpTask extends AsyncTask<String, Void, String> {
//
//
//        @Override
//        protected String doInBackground(String... strings)
//        {
//            String parameter = strings[0];
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            try {
//
//                String url_string = getResources().getString(R.string.server_ip) + getResources().getString(R.string.register_user);
//                URL url = new URL(url_string);
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
//                Log.d("DEBUG", return_value);
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
//            mDialog = new ProgressDialog(LoginActivity.this);
//            mDialog.setMessage("Registering you on IIITD Express");
//            mDialog.show();
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            Log.d("DEBUG", s);
//            handleSignUpData(s);
//            mDialog.dismiss();
//        }
//    }

    public void handleSignUpData(String string){
        try{
            JSONObject raw = new JSONObject(string);
            if(raw.getInt("code") == 1 || raw.getInt("code") == 2){
                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
            }
            else{
                finish();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

}

