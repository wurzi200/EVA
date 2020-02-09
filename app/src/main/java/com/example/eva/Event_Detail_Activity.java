package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Event_Detail_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference();
    private Event _event;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_detail_view);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
            mAuth = FirebaseAuth.getInstance();

            Intent intent = getIntent();
            _event = intent.getParcelableExtra("Event");

            String name = _event.getEventName();
            TextView detailEventName = (TextView)findViewById(R.id.event_detail_name);
            detailEventName.setText(name);

            String location = _event.getEventLocation();
            TextView detailEventLocation = (TextView)findViewById(R.id.event_detail_location);
            detailEventLocation.setText(location);

            String date = _event.getEventDate();
            TextView detailEventDate= (TextView)findViewById(R.id.event_detail_date);
            detailEventDate.setText(date);

            //Buttons
            ImageView cancelButton = (ImageView)findViewById(R.id.btnCancelEvent);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity();
                }
            });

            Button goingButton = (Button)findViewById(R.id.btnGoing);
            goingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateEvent("going");
                }
            });

            Button unsureButton = (Button)findViewById(R.id.btnUnsure);
            unsureButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateEvent("unsure");
                }
            });

            Button notgoingButton = (Button)findViewById(R.id.btnNot_Going);
            notgoingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateEvent("not_going");
                }
            });
        }


        private void updateEvent(String attendance){

            for(InvitedUser inv : _event.InvitedAttendance){

                if(inv != null && inv.getuId().equals(mAuth.getUid()))
                {
                    inv.setAttendance(attendance);
                }
            }

            _event.refreshInvitedIDList();
            Map<String, Object> postValues = _event.toMap();
            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/events/" + _event.getEventId(), postValues);

            _dbReference.updateChildren(childUpdates);
            startActivity();
        }

        private void startActivity(){
            Intent intent = new Intent(this, Overview_Activity.class);
            startActivity(intent);
        }
    }