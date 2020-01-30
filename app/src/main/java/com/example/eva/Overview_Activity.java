package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;

public class Overview_Activity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        mAuth = FirebaseAuth.getInstance();

        listView = findViewById(R.id.event_list);

        findViewById(R.id.signOutButton).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
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
            startActivity(CreateEvent_Activity.class, -1);

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
        startActivity(intent);
    }
}
