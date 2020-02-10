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
    private String uId;
    private String uName;
    private String uPhone;
    private String uEmail;


    //Constructors
    public User(){
       this.uId = "";
       this.uName = "";
       this.uEmail = "";
       this.uPhone = "";
    }

    public User(String id, String name, String email, String phone){
        this.uId = id;
        this.uName = name;
        this.uEmail = email;
        this.uPhone = phone;
    }

    public User(FirebaseUser fUser){
        this.uId = fUser.getUid();
        this.uName = fUser.getDisplayName();
        this.uPhone = fUser.getPhoneNumber();
        this.uEmail = fUser.getEmail();
    }

    public User(GoogleSignInAccount gUser){
        this.uId = gUser.getId();
        this.uName = gUser.getGivenName();
        this.uEmail = gUser.getEmail();
        this.uPhone = "";
    }

    //Setters
    public void setuId(String uid)
    {
        this.uId = uid;
    }
    public void setuName(String name)
    {
        this.uName = name;
    }
    public void setuEmail(String email)
    {
        this.uEmail = email;
    }
    public void setuPhone(String phone)
    {
        this.uPhone = phone;
    }

    //Getters
    public String getuId()
    {
        return uId;
    }
    public String getuName()
    {
        return uName;
    }
    public String getuPhone()
    {
        return uPhone;
    }
    public String getuEmail()
    {
        return uEmail;
    }


    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uId", uId);
        result.put("uName", uName);
        result.put("uPhone", uPhone);
        result.put("uEmail", uEmail);

        return result;
    }

}
