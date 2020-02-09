package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CreateEvent_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference();
    private Event _event;

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
        Log.w("EventID", eventId);

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

    }

    // [START on_start_check_user]

    @Override
    public void onStart() {
        super.onStart();
        _event.InvitedAttendance.add(new InvitedUser(mAuth.getCurrentUser().getUid(), "going"));
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

    private void addEvent(){
        //read values from textboxes

        //this._dbReference.setValue();
    }

    private void alterEvent(){
    }

    private void startActivity(){
        Intent intent = new Intent(this, Overview_Activity.class);
        startActivity(intent);
    }
}
