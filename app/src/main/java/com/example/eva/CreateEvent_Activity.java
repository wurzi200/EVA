package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CreateEvent_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference("state");
    private Event _event;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eventdetailview);
        mAuth = FirebaseAuth.getInstance();
        int eventId = (int)getIntent().getSerializableExtra("Extra");
        //Event eventToAlter = (Event) _dbReference.get;
        Event eventToAlter;
        if(eventId > -1)
        {
            //eventToAlter = (Event) _dbReference.get
            //eventToAlter = new Event(eventId, String name, String location, Date date, List<FirebaseUser> invitedIDs);
            eventToAlter = new Event();
        }
        else
        {
            eventToAlter = new Event();
        }

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if (eventToAlter.GetEventId() == -1)
        {
            this._event = new Event();
        }
        else
        {
            this._event = eventToAlter;
        }
    }

    // [START on_start_check_user]

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        _event.InvitedIDs.add(currentUser);
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
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-YYYY");
            Date date = new java.util.Date();
            try{
                date = (Date)df.parse(editTextDate.getText().toString());
            }
            catch (ParseException e) {
                e.printStackTrace();
            }

            _event.SetEventName(name);
            _event.SetEventLocation(location);
            _event.SetEventDate(date);

            if(_event.isValid())
            {
                _dbReference.setValue(_event);
                startActivity();
            }

        }

        if(i == R.id.btnCancelEvent){
            startActivity();
        }

        if(i == R.id.btnResetFields){
            EditText editTextName = (EditText)findViewById(R.id.eventName);
            editTextName.setText("");
            EditText editTextLocation = (EditText)findViewById(R.id.eventLocation);
            editTextLocation.setText("");
            EditText editTextDate = (EditText)findViewById(R.id.eventDate);
            editTextDate.setText("");
            //reset Invited to only creator
        }

    }

    private void resetFields(){

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
