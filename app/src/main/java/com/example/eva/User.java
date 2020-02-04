package com.example.eva;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class User implements Serializable {

    //private core values
    @Exclude
    private String _uId;

    private String _uName;
    private String _uPhone;
    private String _uEmail;


    //Constructors
    public User(){
       this._uId = "";
       this. _uName = "";
       this._uEmail = "";
       this._uPhone = "";
    }

    public User(String id, String name, String email, String phone){
        this._uId = id;
        this. _uName = name;
        this._uEmail = email;
        this._uPhone = phone;
    }

    public User(FirebaseUser fUser){
        this._uId = fUser.getUid();
        this. _uName = fUser.getDisplayName();
        this._uPhone = fUser.getPhoneNumber();
        this._uEmail = fUser.getEmail();
    }

    public User(GoogleSignInAccount gUser){
        this._uId = gUser.getId();
        this._uName = gUser.getGivenName();
        this._uEmail = gUser.getEmail();
        this._uPhone = "";
    }

    //Setters
    @Exclude
    public void SetId(String uid){
        this._uId = uid;
    }
    public void SetName(String name){
        this._uName = name;
    }
    public void SetEmail(String email){
        this._uEmail = email;
    }
    public void SetPhone(String phone){
        this._uPhone = phone;
    }

    //Getters
    @Exclude
    public String GetId(){
        return _uId;
    }
    public String GetName(){
        return _uName;
    }
    public String GetPhone(){
        return _uPhone;
    }
    public String GetEmail(){
        return _uEmail;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        //result.put("_eventId", _eventId);
        result.put("_uName", _uName);
        result.put("_uPhone", _uPhone);
        result.put("_uEmail", _uEmail);

        return result;
    }

}
