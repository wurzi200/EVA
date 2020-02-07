package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import java.util.List;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;

public class Overview_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference();
    ArrayList<String> _eventStrings = new ArrayList<String>();
    ArrayList<Event> _events = new ArrayList<Event>();
    ArrayAdapter<String> _stringArrayAdapter;
   // ArrayAdapter<Event> _eventArrayAdapter;
    private ListView _listView;
    private String googleUserID;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        mAuth = FirebaseAuth.getInstance();
        googleUserID = (String)getIntent().getSerializableExtra("Extra");

        _listView = (ListView)findViewById(R.id.event_list);
        _stringArrayAdapter = new ArrayAdapter<String>(this, R.layout.layout_listitem,R.id.textview, _eventStrings);
        _listView.setAdapter(_stringArrayAdapter);

        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(Overview_Activity.this, Event_Detail_Activity.class);
                intent.putExtra("Event", (Parcelable) _events.get(position));
                startActivity(intent);
            }
        });

        findViewById(R.id.signOutButton).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Log.w("GoogleSignInClient", mGoogleSignInClient.getSignInIntent().get);
    }

    @Override
    protected void onStart() {
        super.onStart();

        _dbReference.child("events").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                _events.clear();
                _eventStrings.clear();

                for(DataSnapshot eventSnapshot: dataSnapshot.getChildren())
                {
                    String stringEvent = eventSnapshot.getValue(Event.class).toString();
                    Event eventObject = eventSnapshot.getValue(Event.class);

                    String currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser().getUid();


                    if(eventObject.getCreatorId().equals(currentFirebaseUser)) {
                        _events.add(eventObject);
                        _eventStrings.add(stringEvent);
                        _stringArrayAdapter.notifyDataSetChanged();
                        _listView.setAdapter(_stringArrayAdapter);
                    }
                    else
                    {
                        if(eventObject.InvitedIDs.isEmpty())
                            Log.w("idsEmpty", " true");
                        else if(eventObject.InvitedIDs.size() != 0)
                            Log.w("count", "greater zero");

                        for(String inv : eventObject.InvitedIDs)
                        {
                            if(inv != null && inv.equals(mAuth.getUid())){

                                //Log.w("invited", "true");
                                _events.add(eventObject);
                                _eventStrings.add(stringEvent);
                                _stringArrayAdapter.notifyDataSetChanged();
                                _listView.setAdapter(_stringArrayAdapter);
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void signOut() {

        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                    }

                });

    }

    public void onClick(View v) {

        int i = v.getId();


        if(i == R.id.add_event){
            startActivity(CreateEvent_Activity.class, "");

        }

        if (i == R.id.signOutButton) {

            signOut();
            startActivity(GoogleSignInActivity.class, "");

        }


        /*
        if(i == R.id.alter_event){
            Event selectedEvent = new Event();
            //selectedEvent = v.GetContext(event);
            startActivity(CreateEvent_Activity.class, selectedEvent.GetId);
        }
        */
    }



    private void startActivity(Class targetActivity, Serializable extra){
        Intent intent = new Intent(this, targetActivity);
        intent.putExtra("Extra", extra);
        intent.putExtra("GoogleUserID", googleUserID);
        startActivity(intent);
    }
}
