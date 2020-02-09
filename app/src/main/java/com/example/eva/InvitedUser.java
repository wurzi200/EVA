package com.example.eva;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class InvitedUser implements Serializable {

    //Privates
    private String uId;
    private String attendance;



    //Constructors
    public InvitedUser(){
        setuId("");
        setAttendance("unsure");
    }

    //InvitedUserPair is stored in database as "uID.Attendance" for simplicity reasons
    public InvitedUser(String combinedString)
    {

        if(combinedString != null && !combinedString.isEmpty()) {
            //Log.w("notempty", combinedString);
            if (!combinedString.contains("."))
            {
                combinedString += ".unsure";
            }

            String[] split = combinedString.split("[.]", 2);
            this.setuId(split[0]);
            this.setAttendance(split[1]);
        }
    }

    public InvitedUser(String uId, String attendance){
        setuId(uId);
        setAttendance(attendance);
    }

    public InvitedUser(FirebaseUser fUser){
        setuId(fUser.getUid());
        setAttendance("unsure");
    }

    public InvitedUser(GoogleSignInAccount gUser){
        setuId(gUser.getId());
        setAttendance("unsure");
    }




    //Getters and Setters
    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }


    public String getAttendance() {
        return attendance;
    }


    @Override
    public String toString(){
        return this.getuId()+"."+this.getAttendance();
    }

    //attendance is one of the following three: unsure, going, not_going
    //unsure is the default -> not seen/unsure
    public void setAttendance(String attendance) {
        if(!(attendance.equals("unsure") || attendance.equals("going") || attendance.equals("not_going")))
            attendance = "unsure";

        this.attendance = attendance;
    }

}
