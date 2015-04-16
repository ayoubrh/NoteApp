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
import com.google.android.gms.plus.model.people.PersonBuffer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Android Google+ Quickstart activity.
 *
 * Demonstrates Google+ Sign-In and usage of the Google+ APIs to retrieve a
 * users profile information.
 */
public class MainActivity extends ActionBarActivity implements View.OnClickListener,
        ConnectionCallbacks, OnConnectionFailedListener, PlusClient.OnPeopleLoadedListener{

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
    TextView name,url;
    String fname,lname,email;
    String id = null;

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

        mConnectionProgressDialog.setMessage("Connexion En Cours ...");
        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");

        findViewById(R.id.sign_in_button).setOnClickListener(this);
        System.out.println("fffffffffffffffffffffffffffffffffff");


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
            pDialog.setMessage("Chargement ...");
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

            Person currentPerson = mPlusClient.getCurrentPerson();

            fname = currentPerson.getName().getFamilyName();
            lname = currentPerson.getName().getGivenName();
            email=mPlusClient.getAccountName();

            JSONObject json = userFunction.registerProf(fname,lname,email);
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

                    if(res == "true"){
                        id = json.getString("id");
                        System.out.println("id  : "+id);
                    }

                    else{
                        id=identif(json);
                        System.out.println("id  : "+id);
                    }

                    //String red = json.getString(KEY_ERROR);
                    System.out.println("cccccccccccccccccccc777777777777777777777");
                    pDialog.dismiss();


                    Intent objIndent = new Intent(getApplicationContext(),ListEtudiant.class);
                    objIndent.putExtra("PROF_ID", Integer.parseInt(id));
                    startActivity(objIndent);



                }else{
                    pDialog.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error c 1 : "+e.getMessage());

            }
        }
    }


    public void NetAsync(){
        System.out.println("aaaaaaaaaaaaaaaa00000000000000000000");

        new ProcessRegister().execute();
        System.out.println("aaaaaaaaaaaaaa1111111111111111111111");

    }



    public String identif(JSONObject json) {
        JSONArray prof = null;
        String id = null;
        try {
            prof = json.getJSONArray("professeur");
            JSONObject c = prof.getJSONObject(0);
            id = c.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //JSONObject c = innerArray.getJSONObject(0);
        // Storing  JSON item in a Variable

        return id;
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
        System.out.println("ssssssssssssssssssssss00000000000000");

        super.onStart();
        System.out.println("ssssssssssssssssssssss11111111111111111111111111");

        //mPlusClient.connect();
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
            System.out.println("6666666666666666666666666666666666");

            mConnectionResult = null;
            System.out.println("777777777777777777777777777777777");

            mPlusClient.connect();
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        String accountName = mPlusClient.getAccountName();
        System.out.println("444444444444444444444444444444444444");

        Toast.makeText(this, accountName + " est connecter.", Toast.LENGTH_LONG).show();
        System.out.println("555555555555555555555555555555555555");
        NetAsync();


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
                System.out.println("000000000000000000000000000000000");

                mConnectionProgressDialog.show();
                System.out.println("11111111111111111111111111111111111111");
                mPlusClient.connect();


            } else {
                try {
                    System.out.println("22222222222222222222222222222222222222222222");

                    mConnectionResult.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
                    System.out.println("3333333333333333333333333333333333");
                    NetAsync();
                    System.out.println("101010101010101010101010101010101010");


                } catch (SendIntentException e) {
                    // Try connecting again.
                    mConnectionResult = null;
                    mPlusClient.connect();
                }
            }
        }
    }

    @Override
    public void onPeopleLoaded(ConnectionResult status, PersonBuffer personBuffer,
                               String nextPageToken) {
        if (status.getErrorCode() == ConnectionResult.SUCCESS) {
            try {
                int i=0;
                while (personBuffer.get(i)!=null) {
                    Log.d(TAG, "Display Name: " + personBuffer.get(i).getDisplayName());
                    System.out.println("looaaaaaaaaaaaaaaaadddddd"+personBuffer.get(i).getDisplayName());
                    System.out.println("looaaaaaaaaaaaaaaaadddddd"+personBuffer.get(i).getName().getFamilyName());
                    //System.out.println("looaaaaaaaaaaaaaaaadddddd"+personBuffer.get(i).getName().getGivenName());
                    //System.out.println("looaaaaaaaaaaaaaaaadddddd"+personBuffer.get(i).getAboutMe());
                    i++;
                }
            } finally {
                personBuffer.close();
            }
        } else {
            Log.e(TAG, "Error listing people: " + status.getErrorCode());
        }
    }



}


