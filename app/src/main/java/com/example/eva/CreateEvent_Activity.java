package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class CreateEvent_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference();
    private Event _event;
    private List<User> _users = new ArrayList<>();
    private List<String> _userStrings = new ArrayList<>();
    ArrayAdapter<String> _stringArrayAdapter;
    private ListView _listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetailview);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        final String eventId = (String)getIntent().getSerializableExtra("Extra");
        //Log.w("EventID", eventId);

        //Event eventToAlter = (Event) _dbReference.get;
        if(!eventId.isEmpty())
        {
            _dbReference.child("events").child(eventId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Event eventToAlter = dataSnapshot.getValue(Event.class);
                    _event = eventToAlter;
                }

                @Override
                public void onCancelled(DatabaseError dbError) {
                    Log.w("Read operation failed:", "Reading event " +eventId+ " from database");
                }
            });
        }

        else
        {
            _event = new Event();
        }

        _listView = (ListView)findViewById(R.id.eventuserlist);
        _stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, _userStrings){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                View view = super.getView(position, convertView, parent);

                TextView listItem = (TextView)view.findViewById(android.R.id.text1);

                int color_id = ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark);
                boolean isAlreadyInvited = false;
                //Log.w("checking for invitedList", _event.InvitedAttendance.toString());
                for(InvitedUser inv : _event.InvitedAttendance)
                {
                    if(inv.getuId().equals(_users.get(position).getuId()))
                    {
                        isAlreadyInvited = true;
                        switch(inv.getAttendance()) {
                            case "going" :
                                color_id = ContextCompat.getColor(view.getContext(), R.color.yes);
                                break;
                            case "not_going" :
                                color_id = ContextCompat.getColor(view.getContext(), R.color.no);
                                break;
                            case "unsure" :
                                color_id = ContextCompat.getColor(view.getContext(), R.color.maybe);
                                break;
                            default:
                                color_id = ContextCompat.getColor(view.getContext(), R.color.maybe);
                                break;
                        }
                    }
                }
                if(!isAlreadyInvited)
                {
                    color_id = ContextCompat.getColor(view.getContext(), R.color.uninvited);
                }

                listItem.setBackgroundColor(color_id);

                return view;
            }
        };

        _listView.setAdapter(_stringArrayAdapter);

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                InvitedUser inv = new InvitedUser(_users.get(position));
                //Log.w("clicked on", inv.getuId());
                //Log.w("currentlyInvited", _event.InvitedAttendance.toString());
                if(_event.InvitedAttendance.contains(inv))
                {
                    _event.InvitedAttendance.remove(inv);
                }
                else {
                    _event.InvitedAttendance.add(inv);
                }

                _event.refreshInvitedIDList();

                Log.w("notifying", _event.InvitedAttendance.toString());
                _stringArrayAdapter.notifyDataSetChanged();
                //_userStrings.notifyAll();
                //Log.w("aftermath", _event.InvitedAttendance.toString());
            }
        });

    }

    // [START on_start_check_user]

    @Override
    public void onStart() {
        super.onStart();
        _event.InvitedAttendance.add(new InvitedUser(mAuth.getCurrentUser().getUid(), "going"));

        _dbReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _users.clear();
                _userStrings.clear();

                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren())
                {
                    String userString = eventSnapshot.getValue(User.class).toString();
                    User userObject = eventSnapshot.getValue(User.class);

                    //cannot uninvite the creator
                    if(!userObject.getuId().equals(_event.getCreatorId())) {
                        _users.add(userObject);
                        _userStrings.add(userString);
                        _stringArrayAdapter.notifyDataSetChanged();
                        _listView.setAdapter(_stringArrayAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // [END on_start_check_user]

    public void onClick(View v) {

        int i = v.getId();

        if (i == R.id.btnSaveEvent) {
            EditText editTextName = (EditText)findViewById(R.id.eventName);
            String name = editTextName.getText().toString();

            EditText editTextLocation = (EditText)findViewById(R.id.eventLocation);
            String location = editTextLocation.getText().toString();
            EditText editTextDate = (EditText)findViewById(R.id.eventDate);
            String date = editTextDate.getText().toString();
            String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

            _event.setEventName(name);
            _event.setEventLocation(location);
            _event.setEventDate(date);
            _event.setCreatorId(currentFirebaseUser);

            if(_event.isValid())
            {
                String key = _dbReference.child("events").push().getKey();
                _event.setEventId(key);
                Map<String, Object> postValues = _event.toMap();
                Map<String, Object> childUpdates = new HashMap<>();
                childUpdates.put("/events/" + key, postValues);

                _dbReference.updateChildren(childUpdates);
                startActivity();
            }
        }

        if(i == R.id.btnCancelEvent){
            startActivity();
        }

        if(i == R.id.btnResetFields){
            resetFields();
        }
    }

    private void resetFields(){
        EditText editTextName = (EditText)findViewById(R.id.eventName);
        editTextName.setText("");

        EditText editTextLocation = (EditText)findViewById(R.id.eventLocation);
        editTextLocation.setText("");

        EditText editTextDate = (EditText)findViewById(R.id.eventDate);
        editTextDate.setText("");
    }

    private void startActivity(){
        Intent intent = new Intent(this, Overview_Activity.class);
        startActivity(intent);
    }
}
