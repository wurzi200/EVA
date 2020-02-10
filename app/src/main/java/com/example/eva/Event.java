package com.example.eva;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class Event implements Serializable, Parcelable {

    //private
    private String eventId;
    private String creatorId;
    private String eventName;
    private String eventDate;
    private String eventLocation;
    public List<String> InvitedIDs;

    @Exclude
    public List<InvitedUser> InvitedAttendance;


    //Constructors
    public Event() {
        this.eventId = "";
        this.setEventDate("");
        this.setEventName("");
        this.setEventLocation("");
        this.setCreatorId("");
        this.InvitedIDs = new ArrayList<String>();
        this.InvitedAttendance = new ArrayList<>();
        for(String inv:InvitedIDs){
            InvitedAttendance.add(new InvitedUser(inv));
        }
    }

    public Event(String eventDate, String eventLocation, String eventName) {
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.eventName = eventName;
    }

    public Event(String creatorId, String id, String name, String location, String date, List<InvitedUser> invitedUsers) {
        this.setCreatorId(creatorId);
        this.setEventId(id);
        this.setEventDate(date);
        this.setEventName(name);
        this.setEventLocation(location);

        InvitedIDs = new ArrayList<>();
        //should never be empty, as the current user is added
        if (!invitedUsers.isEmpty()) {
            for (InvitedUser user : invitedUsers) {
                InvitedIDs.add(user.toString());
            }
        }
    }

    protected Event(Parcel in) {
        eventId = in.readString();
        creatorId = in.readString();
        eventName = in.readString();
        eventDate = in.readString();
        eventLocation = in.readString();
        InvitedIDs = in.createStringArrayList();
        InvitedAttendance = new ArrayList<>();
        for(String inv:InvitedIDs){
            InvitedAttendance.add(new InvitedUser(inv));
        }
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("eventId", eventId);
        result.put("creatorId", creatorId);
        result.put("eventName", eventName);
        result.put("eventDate", eventDate);
        result.put("eventLocation", eventLocation);

        InvitedIDs = new ArrayList<>();
        for (InvitedUser user : InvitedAttendance) {
            InvitedIDs.add(user.toString());
        }
        result.put("InvitedIDs", InvitedIDs);

        return result;
    }

    //Getters and Setters
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String id) {
        this.eventId = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

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
    public String toString() {
        return this.eventName + " " + this.eventDate;
    }


    //Public methods
    public Boolean isValid() {

        if (this.getCreatorId().isEmpty()) {
            return false;
        }

        if (this.getEventName().isEmpty()) {
            return false;
        }

        if (this.getEventDate().isEmpty()) {
            return false;
        }

        if (this.getEventLocation().isEmpty()) {
            return false;
        } else
            return true;
    }

    public void refreshAttendanceList(){
        //Log.w("AttendanceList before refresh", InvitedAttendance.toString());
        InvitedAttendance = new ArrayList<>();
        //Log.w("going into refresh with IDs", InvitedIDs.toString());
        for(String inv:InvitedIDs){
            //Log.w("\t inv", inv);
            InvitedAttendance.add(new InvitedUser(inv));
        }
    }

    public void refreshInvitedIDList(){
        InvitedIDs = new ArrayList<>();
        for(InvitedUser inv:InvitedAttendance){
            InvitedIDs.add(inv.toString());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventId);
        dest.writeString(creatorId);
        dest.writeString(eventName);
        dest.writeString(eventDate);
        dest.writeString(eventLocation);
        dest.writeStringList(InvitedIDs);
    }
}
