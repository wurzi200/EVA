package com.example.eva;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Map;

public class Event implements Serializable {

    //private
    private String _eventId;
    private String _eventName;
    private String _eventDate;
    private String _eventLocation;
    public List<FirebaseUser> InvitedIDs;


    //Constructors
    public Event(){
        this._eventId = "";
        this.SetEventDate("");
        this.SetEventName("");
        this.SetEventLocation("");
        this.InvitedIDs = new ArrayList<FirebaseUser>();
    }

    public Event(String id, String name, String location, String date, List<FirebaseUser> invitedIDs){
        this.SetEventId(id);
        this.SetEventDate(date);
        this.SetEventName(name);
        this.SetEventLocation(location);
        this.InvitedIDs = invitedIDs;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("_eventId", _eventId);
        result.put("_eventName", _eventName);
        result.put("_eventDate", _eventDate);
        result.put("_eventLocation", _eventLocation);

        return result;
    }

    //Getters and Setters
    public String GetEventId(){
        return _eventId;
    }

    public void SetEventId(String id){
        this._eventId = id;
    }

    public String GetEventName() {
        return _eventName;
    }

    public void SetEventName(String eventName) {
        this._eventName = eventName;
    }

    public String GetEventDate() {
        return _eventDate;
    }

    public void SetEventDate(String eventDate) {
        this._eventDate = eventDate;
    }

    public String GetEventLocation() {
        return _eventLocation;
    }

    public void SetEventLocation(String eventLocation) {
        this._eventLocation = eventLocation;
    }


    //Public methods
    public Boolean isValid(){
        if(this.GetEventName().isEmpty())
        {
            return false;
        }

        if(this.GetEventDate().isEmpty())
        {
            return false;
        }

        if(this.GetEventLocation().isEmpty())
        {
            return false;
        }

        else
            return true;
    }
}
