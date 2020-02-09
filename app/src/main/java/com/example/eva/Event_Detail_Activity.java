package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import java.util.List;
import java.util.Map;

public class Event_Detail_Activity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference();
    private Event _event;

    private List<User> _allUsers = new ArrayList<>();
    private List<InvitedUser> _invitedUsers = new ArrayList<>();
    private List<String> _invitedStrings = new ArrayList<>();
    ArrayAdapter<String> _stringArrayAdapter;
    private ListView _listView;

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
            _event.refreshAttendanceList();

            String name = _event.getEventName();
            TextView detailEventName = (TextView)findViewById(R.id.event_detail_name);
            detailEventName.setText(name);

            String location = _event.getEventLocation();
            TextView detailEventLocation = (TextView)findViewById(R.id.event_detail_location);
            detailEventLocation.setText(location);

            String date = _event.getEventDate();
            TextView detailEventDate= (TextView)findViewById(R.id.event_detail_date);
            detailEventDate.setText(date);


            _listView = (ListView)findViewById(R.id.invitedusers);
            _stringArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, android.R.id.text1, _invitedStrings){
                @Override
                public View getView(int position, View convertView, ViewGroup parent){

                    View view = super.getView(position, convertView, parent);

                    TextView listItem = (TextView)view.findViewById(android.R.id.text1);

                    int color_id = ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark);
                    Log.w("checking for invitedAtt", _event.InvitedAttendance.toString());
                    for(InvitedUser inv : _event.InvitedAttendance)
                    {

                        Log.w("\t checking for invitedUsers", _invitedUsers.toString());
                        if(inv.getuId().equals(_invitedUsers.get(position).getuId()))
                        {

                            Log.w("\t \t found match", inv.toString());
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

                    listItem.setBackgroundColor(color_id);

                    return view;
                }
            };
            _listView.setAdapter(_stringArrayAdapter);
            _stringArrayAdapter.notifyDataSetChanged();


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


    @Override
    public void onStart() {
        super.onStart();
        _event.refreshAttendanceList();

        Log.w("attendanceList", _event.InvitedAttendance.toString());
        _dbReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _allUsers.clear();
                _invitedUsers.clear();
                _invitedStrings.clear();

                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren())
                {
                    User userObject = eventSnapshot.getValue(User.class);
                    Log.w("userObjectName", userObject.getuName());
                    _allUsers.add(userObject);
                    for(InvitedUser inv : _event.InvitedAttendance)
                    {
                        if(inv.getuId().equals(userObject.getuId()))
                        {
                            Log.w("found match", "true");
                            _invitedStrings.add(userObject.getuName());
                            _invitedUsers.add(inv);

                            //_stringArrayAdapter.notifyDataSetChanged();
                        }
                    }
                }
                _stringArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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