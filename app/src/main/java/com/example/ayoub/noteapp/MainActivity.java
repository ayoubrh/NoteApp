package com.example.ayoub.noteapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;




import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusClient;
import com.google.android.gms.plus.PlusShare;
import com.google.android.gms.plus.model.people.Person;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Android Google+ Quickstart activity.
 *
 * Demonstrates Google+ Sign-In and usage of the Google+ APIs to retrieve a
 * users profile information.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener{

    //private GoogleApiClient mGoogleApiClient;


    private static String KEY_SUCCESS = "success";
    private static String KEY_UID = "uid";
    private static String KEY_FIRSTNAME = "fname";
    private static String KEY_LASTNAME = "lname";
    private static String KEY_USERNAME = "uname";
    private static String KEY_EMAIL = "email";
    private static String KEY_CREATED_AT = "created_at";
    private static String KEY_ERROR = "errors";

    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;
    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;
    Button ShareButton,GetData,ok_btn;
    Dialog get_data_dialog;
    TextView name,url,id;
    String lname,email;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       /* mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(MainActivity.this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, null)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();*/

        System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");

        mPlusClient = new PlusClient.Builder(this, this, this)
                .setActions("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .build();


        setContentView(R.layout.activity_main);


        System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");

        mConnectionProgressDialog = new ProgressDialog(this);
        System.out.println("ccccccccccccccccccccccccccccccccccc");

        mConnectionProgressDialog.setMessage("Signing in...");
        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        System.out.println("fffffffffffffffffffffffffffffffffff");

        ShareButton = (Button) findViewById(R.id.post_button);
        System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

        GetData = (Button)findViewById(R.id.get_data_button);
        System.out.println("llllllllllllllllllllllllllllllllll");

        GetData.setOnClickListener(new View.OnClickListener() {
            String disp_name,disp_url,disp_id;
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if( mPlusClient.isConnected())
                {
                    System.out.println("iiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii");

                    get_data_dialog = new Dialog(MainActivity.this);
                    get_data_dialog.setContentView(R.layout.get_data_frag);
                    get_data_dialog.setTitle("User Details");
                    System.out.println("jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjj");

                    name = (TextView)get_data_dialog.findViewById(R.id.get_name);
                    url = (TextView)get_data_dialog.findViewById(R.id.get_url);
                    id = (TextView)get_data_dialog.findViewById(R.id.get_id);
                    ok_btn = (Button)get_data_dialog.findViewById(R.id.ok_button);
                    System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");

                    ok_btn.setOnClickListener(new OnClickListener(){
                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            get_data_dialog.cancel();
                        }
                    });
                    System.out.println("llllllllllllllllllllllllllllllllllllll");

                    Person currentPerson = mPlusClient.getCurrentPerson();
                    System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

                    disp_name = currentPerson.getDisplayName();
                    lname = currentPerson.getDisplayName();
                    disp_url = currentPerson.getUrl();
                    disp_id = currentPerson.getId();
                    System.out.println("nnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");

                    name.setText(disp_name);
                    System.out.println("Nome : "+lname);
                    url.setText(disp_url);
                    id.setText(disp_id);
                    email=mPlusClient.getAccountName();
                    System.out.println("Email : "+email);

                    NetAsync(v);

                    get_data_dialog.show();
                }else{
                    Toast.makeText(getApplicationContext(), "Please Sign In", Toast.LENGTH_LONG).show();
                }}
        });
        ShareButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the Google+ share dialog with attribution to your app.
                Intent shareIntent = new PlusShare.Builder(MainActivity.this)
                        .setType("text/plain")
                        .setText("")
                        //.setContentUrl(Uri.parse("http://www.learn2crack.com"))
                        .getIntent();
                startActivityForResult(shareIntent, 0);
            }
        });
    }



    private class NetCheck extends AsyncTask<String, String, Boolean>{
        private ProgressDialog nDialog;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbb00000000000000000000000");

            nDialog = new ProgressDialog(MainActivity.this);
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb11111111111111111");

            nDialog.setMessage("Loading..");
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb+++++++++++++++++++++++++++++");

            nDialog.setTitle("Checking Network");
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb-------------------------");

            nDialog.setIndeterminate(false);
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb*******************************");

            nDialog.setCancelable(true);
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbbbbbb///////////////////////////////////1");

            nDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... args){
/**
  * Gets current device state and checks for working internet connection by trying Google.
  **/
            System.out.println("bbbbbbbbbbbbbbbbbbbbb22222222222222222222222222");

            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbb333333333333333333333");

            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbbb444444444444444444444");

            if (netInfo != null && netInfo.isConnected()) {
                try {
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbb555555555555555555555");

                    URL url = new URL("http://www.google.com");
                    HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbb6666666666666666666666666");

                    urlc.setConnectTimeout(3000);
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbbb77777777777777777777777");

                    urlc.connect();
                    System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb88888888888888888888888");

                    if (urlc.getResponseCode() == 200) {
                        return true;
                    }
                } catch (MalformedURLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                    System.out.println("Error b 1 : "+ e1.getMessage());

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("Error b 2 : "+e.getMessage());

                }
            }
            return false;
        }
        @Override
        protected void onPostExecute(Boolean th){
            if(th == true){
                nDialog.dismiss();
                System.out.println("bbbbbbbbbbbbbb999999999999999999");

                new ProcessRegister().execute();
                System.out.println("bbbbbbbbbbbbbbbbbbbbb101010101010101010010");

            }
            else{
                nDialog.dismiss();
            }
        }
    }
    private class ProcessRegister extends AsyncTask<String, String, JSONObject>{
/**
  * Defining Process dialog
  **/
        private ProgressDialog pDialog;
        //String email,password,fname,lname,uname;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            System.out.println("cccccccccccccccccccc00000000000000000");

            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setTitle("Contacting Servers");
            pDialog.setMessage("Registering ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            System.out.println("cccccccccccccccccccccccc11111111111111111111111");

            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            System.out.println("cccccccccccccccccccccccc222222222222222222222");

            UserFunctions userFunction = new UserFunctions();
            System.out.println("ccccccccccccccccccccccccccccc33333333333333333333");

            JSONObject json = userFunction.registerProf(lname,lname,email);
            System.out.println("ccccccccccccccccccccc44444444444444444444");

            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            /**
                     * Checks for success message.
                     **/
            try {
                System.out.println("ecscsv : "+json.getString(KEY_SUCCESS));
                if (json.getString(KEY_SUCCESS) != null) {
                    System.out.println("cccccccccccccccccccc55555555555555555");

                    String res = json.getString(KEY_SUCCESS);
                    System.out.println("ccccccccccccccc66666666666666666666");

                    String red = json.getString(KEY_ERROR);
                    System.out.println("cccccccccccccccccccc777777777777777777777");

                    if(Integer.parseInt(res) == 1){
                        pDialog.setTitle("Getting Data");
                        pDialog.setMessage("Loading Info");
                        //DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        System.out.println("cccccccccccccccccccc888888888888888888888");

                        //JSONObject json_user = json.getJSONObject("Professeur");
                        /**
                                                      * Removes all the previous data in the SQlite database
                                                      **/
                        //UserFunctions logout = new UserFunctions();
                        //logout.logoutUser(getApplicationContext());
                        //db.addUser(json_user.getString(KEY_FIRSTNAME),json_user.getString(KEY_LASTNAME),json_user.getString(KEY_EMAIL),json_user.getString(KEY_USERNAME),json_user.getString(KEY_UID),json_user.getString(KEY_CREATED_AT));
                        /**
                                                      * Stores registered data in SQlite Database
                                                      * Launch Registered screen
                                                      **/
                        //Intent registered = new Intent(getApplicationContext(), Registered.class);
                        /**
                                                      * Close all views before launching Registered screen
                                                     **/
                        //registered.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        pDialog.dismiss();
                        //startActivity(registered);
                        finish();
                    }
                    else if (Integer.parseInt(red) ==2){
                        pDialog.dismiss();
                    }
                    else if (Integer.parseInt(red) ==3){
                        pDialog.dismiss();
                    }
                }
                else{
                    pDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error c 1 : "+e.getMessage());

            }
        }
    }


    public void NetAsync(View view){
        System.out.println("aaaaaaaaaaaaaaaa00000000000000000000");

        new ProcessRegister().execute();
        System.out.println("aaaaaaaaaaaaaa1111111111111111111111");

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mPlusClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (mConnectionProgressDialog.isShowing()) {
            // The user clicked the sign-in button already. Start to resolve
            // connection errors. Wait until onConnected() to dismiss the
            // connection dialog.
            if (result.hasResolution()) {
                try {
                    result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (SendIntentException e) {
                    mPlusClient.connect();
                }
            }
        }
        // Save the result and resolve the connection failure upon a user click.
        mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mPlusClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDisconnected() {
        Toast.makeText(this," Disconnected.", Toast.LENGTH_LONG).show();
        Log.d(TAG, "disconnected");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in_button && !mPlusClient.isConnected()) {
            if (mConnectionResult == null) {
                mConnectionProgressDialog.show();
            } else {
                try {
                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                } catch (SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
    }

}


