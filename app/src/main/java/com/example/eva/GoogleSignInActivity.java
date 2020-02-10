package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class GoogleSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    private FirebaseDatabase _database = FirebaseDatabase.getInstance();
    private DatabaseReference _dbReference = _database.getReference();
    // [START declare_auth]
    private FirebaseAuth mAuth;
    private String googleID;
    // [END declare_auth]



    private GoogleSignInClient mGoogleSignInClient;
    // private TextView mDetailTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Intent intent = getIntent();

        // Views

        //TextView mStatusTextView = findViewById(R.id.status);
       // mDetailTextView = findViewById(R.id.detail);

        // Button listeners

        findViewById(R.id.signInButton).setOnClickListener(this);
        //findViewById(R.id.signOutButton).setOnClickListener(this);



        // [START config_signin]
        //Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    // [START on_start_check_user]

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {

                // Google Sign In was successful, authenticate with Firebase

                GoogleSignInAccount account = task.getResult(ApiException.class);

                firebaseAuthWithGoogle(account);


            } catch (ApiException e) {

                // Google Sign In failed, update UI appropriately

                Log.w(TAG, "Google sign in failed", e);

                // [START_EXCLUDE]

                // [END_EXCLUDE]

            }

        }

    }

    // [END onactivityresult]
    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {

        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        googleID = acct.getId();
        // [START_EXCLUDE silent]

        // [END_EXCLUDE]



        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update database with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");

                            User user = new User(mAuth.getCurrentUser());
                            String key = user.getuId();
                            Map<String, Object> postValues = user.toMap();
                            Map<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put("/users/" + key, postValues);
                            _dbReference.updateChildren(childUpdates);

                            startActivity();

                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithCredential:failure", task.getException());


                        }

                        // [START_EXCLUDE]
                        // [END_EXCLUDE]

                    }
                });
    }

    // [END auth_with_google]

    // [START signin]

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // [END signin]

    /*private void signOut() {

        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override

                    public void onComplete(@NonNull Task<Void> task) {

                    }

                });

    }*/



    private void revokeAccess() {

        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,

                new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }



    @Override
    public void onClick(View v) {

        int i = v.getId();

        if (i == R.id.signInButton) {

            signIn();

        } /*else if (i == R.id.signOutButton) {

            signOut();

        }*/

    }

    private void startActivity(){
        Intent intent = new Intent(this, Overview_Activity.class);
        intent.putExtra("Extra", googleID);
        startActivity(intent);
    }

}