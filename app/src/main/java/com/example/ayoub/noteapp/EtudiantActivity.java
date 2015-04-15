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
import android.widget.EditText;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.HashMap;
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










public class EtudiantActivity extends ActionBarActivity implements View.OnClickListener {



    Button btnSave ,  btnDelete, btnClose;

    TextView Nom,Prenom,Email;
    EditText NoteC1;
    EditText NoteC2;
    EditText Autre;
    EditText NoteEx;
    EditText NoteF;
    private int Etudiant_Id = 0;

    JSONObject etd=null;


    ListView list;
    TextView ver;
    TextView name;
    TextView api;
    Button Btngetdata;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<HashMap<String, String>>();
    //URL to get JSON Array
    private static String url = "http://10.0.3.2/APIRest/web/app_dev.php/api/etudiants/";
    //JSON Node Names
    private static final String TAG_Etd = "etudiant";
    private static final String TAG_Nom = "_nom";
    private static final String TAG_Prenom = "_prenom";
    private static final String TAG_Email = "_email";
    private static final String TAG_NoteC1 = "_note_c1";
    private static final String TAG_NoteC2 = "_note_c2";
    private static final String TAG_Autre = "_autre";
    private static final String TAG_NoteEx = "_note_ex";
    private static final String TAG_NoteF = "_note_f";


    JSONArray android = null;

    private int prof_id=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etudiant);
        oslist = new ArrayList<HashMap<String, String>>();

        /*Intent intent = getIntent();
        prof_id =intent.getIntExtra("PROF_ID", 0);

        if(prof_id!=0){
            new JSONParse().execute();
        }*/

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnClose = (Button) findViewById(R.id.btnClose);


        Nom = (TextView)findViewById(R.id.Nom);
        Prenom = (TextView)findViewById(R.id.Prenom);
        Email = (TextView)findViewById(R.id.Email);

        NoteC1 = (EditText) findViewById(R.id.editNoteC1);
        NoteC2 = (EditText) findViewById(R.id.editNoteC2);
        Autre = (EditText) findViewById(R.id.editAutre);
        NoteEx = (EditText) findViewById(R.id.editNoteEx);
        NoteF = (EditText) findViewById(R.id.editNoteF);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        btnClose.setOnClickListener(this);


        //Etudiant_Id =0;
        Intent intent = getIntent();
        Etudiant_Id =intent.getIntExtra("etudiant_Id", 0);
        System.out.println("*****************ETD ID : "+Etudiant_Id);
        new JSONParse().execute();
        /*EtudiantRepo repo = new EtudiantRepo(this);
        Etudiant etudiant = new Etudiant();
        etudiant = repo.getEtudiantById(_Etudiant_Id);
        ArrayList<Professeur> professeurs = new ArrayList<Professeur>();
        //professeurs=MainActivity.getProfesseurs();


        editTextNom.setText(professeurs.get(0).getNom());
        editTextPrenom.setText(professeurs.get(0).getPrenom());
        editTextEmail.setText(professeurs.get(0).getEmail());
        */








    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_etudiant, menu);
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





    public void onClick(View view) {
        if (view == findViewById(R.id.btnSave)){
            /*EtudiantRepo repo = new EtudiantRepo(this);
            Etudiant etudiant = new Etudiant();
            etudiant.age= Integer.parseInt(editTextAge.getText().toString());
            etudiant.email=editTextEmail.getText().toString();
            etudiant.nom=editTextNom.getText().toString();
            etudiant.prenom=editTextPrenom.getText().toString();
            etudiant.filiere=editTextfiliere.getText().toString();
            etudiant.etudiant_ID=_Etudiant_Id;

            if (Etudiant_Id==0){
                Etudiant_Id = repo.insert(etudiant);

                Toast.makeText(this,"Nouveau Etudiant Enregister",Toast.LENGTH_SHORT).show();
            }else{

                repo.update(etudiant);
                Toast.makeText(this,"Etduiant Modifier",Toast.LENGTH_SHORT).show();
            }*/
        }else if (view== findViewById(R.id.btnDelete)){
            /*EtudiantRepo repo = new EtudiantRepo(this);
            repo.delete(_Etudiant_Id);
            Toast.makeText(this, "Etudiant Supprimer", Toast.LENGTH_SHORT).show();
            finish();*/
        }else if (view== findViewById(R.id.btnClose)){
            finish();
        }


    }













    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*ver = (TextView)findViewById(R.id.vers);
            name = (TextView)findViewById(R.id.name);
            api = (TextView)findViewById(R.id.api);*/
            pDialog = new ProgressDialog(EtudiantActivity.this);
            pDialog.setMessage("Chargement ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            // Getting JSON from URL
            JSONObject json = jParser.getJSONFromUrl(url+Etudiant_Id);
            return json;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            try {
                // Getting JSON Array from URL
                //android = json.getJSONArray(TAG_OS);
                //for(int i = 0; i < android.length(); i++){
                    //JSONObject c = android.getJSONObject(i);
                    // Storing  JSON item in a Variable


                //android = json.getJSONArray(TAG_Etd);
                //for (int i = 0; i < android.length(); i++) {
                    //JSONObject etd = android.getJSONObject(0);
                etd = json.getJSONObject(TAG_Etd);


                String nom = etd.getString(TAG_Nom);
                String prnom = etd.getString(TAG_Prenom);
                String email = etd.getString(TAG_Email);
                String notec1 = etd.getString(TAG_NoteC1);
                String notc2 = etd.getString(TAG_NoteC2);
                String noteex = etd.getString(TAG_NoteEx);
                String autre = etd.getString(TAG_Autre);
                String notef = etd.getString(TAG_NoteF);
                    // Adding value HashMap key => value

                Nom.setText(nom);
                Prenom.setText(prnom);
                Email.setText(email);

                NoteC1.setText(notec1);
                NoteC2.setText(notc2);
                Autre.setText(noteex);
                NoteEx.setText(autre);
                NoteF.setText(notef);

                } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }
}
