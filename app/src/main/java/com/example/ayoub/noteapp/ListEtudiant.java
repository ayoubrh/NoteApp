package com.example.ayoub.noteapp;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.SynchronousQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class ListEtudiant extends ActionBarActivity implements View.OnClickListener{

    private static String KEY_SUCCESS = "success";

    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button btnEmail;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://10.0.3.2/APIRest/web/app_dev.php/api/listetudiants/";
    private static String mailurl = "http://10.0.3.2/APIRest/web/app_dev.php/api/allnotes/";

    //JSON Node Names
    private static final String TAG_OS = "etudiants";
    private static final String TAG_VER = "_nom";
    private static final String TAG_NAME = "_prenom";
    private static final String TAG_API = "_email";
    JSONArray android = null;

    private int prof_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_etudiant);

        oslist = new ArrayList<HashMap<String, String>>();
        btnEmail = (Button)findViewById(R.id.btnEmail);
        btnEmail.setOnClickListener(this);

        Intent intent = getIntent();
        prof_id = intent.getIntExtra("PROF_ID", 0);
        System.out.println("id prof : "+prof_id);

        if (prof_id != 0) {
            new JSONParse().execute();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_etudiant, menu);
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
    public void onClick(View v) {
        if(v == findViewById(R.id.btnEmail)){
            ///
            new mailtask().execute();
        }

    }


    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ver = (TextView) findViewById(R.id.vers);
            name = (TextView) findViewById(R.id.name);
            api = (TextView) findViewById(R.id.api);
            pDialog = new ProgressDialog(ListEtudiant.this);
            pDialog.setMessage("Chargement ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url + prof_id);
            return json;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                android = json.getJSONArray(TAG_OS);
                for (int i = 0; i < android.length(); i++) {
                    JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable
                    String ver = c.getString(TAG_VER);
                    String etd_id = c.getString("id");
                    String name = c.getString(TAG_NAME);
                    String api = c.getString(TAG_API);
                    // Adding value HashMap key => value
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(TAG_VER, ver);
                    map.put(TAG_NAME, name);
                    map.put(TAG_API, api);
                    map.put("id", etd_id);
                    oslist.add(map);
                    list = (ListView) findViewById(R.id.list);
                    ListAdapter adapter = new SimpleAdapter(ListEtudiant.this, oslist, R.layout.list_v,
                            new String[]{TAG_VER, TAG_NAME, TAG_API}, new int[]{R.id.vers, R.id.name, R.id.api});
                    list.setAdapter(adapter);
                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(ListEtudiant.this, "You Clicked at " + oslist.get(+position).get("_email"), Toast.LENGTH_SHORT).show();
                            Intent objIndent = new Intent(getApplicationContext(),EtudiantActivity.class);
                            objIndent.putExtra("etudiant_Id", Integer.parseInt(oslist.get(+position).get("id")));
                            objIndent.putExtra("PROF_ID", prof_id);
                            startActivity(objIndent);
                        }
                    });
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private class mailtask extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*ver = (TextView)findViewById(R.id.vers);
            name = (TextView)findViewById(R.id.name);
            api = (TextView)findViewById(R.id.api);*/
            pDialog = new ProgressDialog(ListEtudiant.this);
            pDialog.setMessage("Chargement ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(mailurl+prof_id);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();

            try {
                System.out.println("ecscsv : "+json.getString(KEY_SUCCESS));
                if (json.getString(KEY_SUCCESS) != null) {
                    System.out.println("cccccccccccccccccccc55555555555555555");

                    String res = json.getString(KEY_SUCCESS);
                    System.out.println("ccccccccccccccc66666666666666666666");

                    if(res == "true"){
                        //id = json.getString("id");
                        //System.out.println("id  : "+id);
                        Toast.makeText(ListEtudiant.this, "Resultat envoyer", Toast.LENGTH_SHORT).show();
                    }

                    else{
                        //id=identif(json);
                        //System.out.println("id  : "+id);
                        Toast.makeText(ListEtudiant.this, "Erreur réessayez plus tard", Toast.LENGTH_SHORT).show();
                    }

                    //String red = json.getString(KEY_ERROR);
                    System.out.println("cccccccccccccccccccc777777777777777777777");


                    /*Intent objIndent = new Intent(getApplicationContext(),ListEtudiant.class);
                    objIndent.putExtra("PROF_ID", Integer.parseInt(id));
                    startActivity(objIndent);*/



                }else{
                    Toast.makeText(ListEtudiant.this, "Erreur", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("Error c 1 : "+e.getMessage());

            }

        }
    }
}
