package com.example.eva;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Event implements Serializable {

    //private
    private String eventId;
    //private String _creatorId;
    private String eventName;
    private String eventDate;
    private String eventLocation;
    public List<String> InvitedIDs;

    @Exclude
    public List<User> InvitedUsers;


    //Constructors
    public Event(){
        this.eventId = "";
        this.setEventDate("");
        this.setEventName("");
        this.setEventLocation("");

        this.InvitedUsers = new ArrayList<User>();
        this.InvitedIDs = new ArrayList<String>();
    }

    public Event(String eventDate, String eventLocation, String eventName){
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventName = eventName;
    }

    public Event(String creator, String id, String name, String location, String date, List<User> invitedUsers){
        //this._creatorId = creator;
        this.setEventId(id);
        this.setEventDate(date);
        this.setEventName(name);
        this.setEventLocation(location);

        //should never be empty, as the current user is added
        if(!invitedUsers.isEmpty()) {
            for (User user : invitedUsers){
                InvitedIDs.add(user.GetId());
            }
        }

        this.InvitedUsers = invitedUsers;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventId", eventId);
        result.put("eventName", eventName);
        result.put("eventDate", eventDate);
        result.put("eventLocation", eventLocation);

        InvitedIDs.clear();
        for (User user: InvitedUsers) {
            InvitedIDs.add(user.GetId());
        }
        result.put("InvitedIDs", InvitedIDs);

        return result;
    }

    //Getters and Setters
    public String getEventId(){
        return eventId;
    }

    public void setEventId(String id){
        this.eventId = id;
    }

    /*
    public String GetCreatorId(){
        return _creatorId;
    }
     */

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    @Override
    public String toString(){
        return this.eventName + " " + this.eventDate;
    }


    //Public methods
    public Boolean isValid(){
        if(this.getEventName().isEmpty())
        {
            return false;
        }

        if(this.getEventDate().isEmpty())
        {
            return false;
        }

        if(this.getEventLocation().isEmpty())
        {
            return false;
        }

        else
            return true;
    }
}
