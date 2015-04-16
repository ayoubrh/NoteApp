package com.example.ayoub.noteapp;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;

public class UserFunctions {
    private JSONParser jsonParser;
    //URL of the PHP API
    private static String loginURL = "http://192.168.1.200/learn2crack_login_api/";
    private static String registerURL = "http://192.168.1.200/APIRest/web/app_dev.php/api/addprofs";
    private static String forpassURL = "http://192.168.1.200/learn2crack_login_api/";
    private static String NoteURL = "http://192.168.1.200/APIRest/web/app_dev.php/api/modifetudiants/";
    private static String login_tag = "login";
    private static String register_tag = "register";
    private static String forpass_tag = "forpass";
    private static String chgpass_tag = "chgpass";
    // constructor
    public UserFunctions(){
        jsonParser = new JSONParser();
    }

    /**
     * Function to change password
     **/
    public JSONObject saveNote(String c1, String c2, String ex, String au, String f,int id){
        List params = new ArrayList();
        //params.add(new BasicNameValuePair("tag", note_tag));
        params.add(new BasicNameValuePair("NoteC1", c1));
        params.add(new BasicNameValuePair("NoteC2", c2));
        params.add(new BasicNameValuePair("NoteEx", ex));
        params.add(new BasicNameValuePair("Autre", au));
        params.add(new BasicNameValuePair("NoteF", f));
        JSONObject json = jsonParser.getJSONFromUrl(NoteURL+id, params);
        return json;
    }

     /**
      * Function to  Register
      **/
    public JSONObject registerProf(String fname, String lname, String email){
        // Building Parameters
        List params = new ArrayList();
        //params.add(new BasicNameValuePair("tag", register_tag));
        params.add(new BasicNameValuePair("Nom", fname));
        params.add(new BasicNameValuePair("Prenom", lname));
        params.add(new BasicNameValuePair("Email", email));
        JSONObject json = jsonParser.getJSONFromUrl(registerURL,params);
        return json;
    }
    /**
     * Function to logout user
     * Resets the temporary data stored in SQLite Database
     *
    public boolean logoutUser(Context context){
        DatabaseHandler db = new DatabaseHandler(context);
        db.resetTables();
        return true;
    }*/
}