package com.example.eva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Change_Color_Activity extends AppCompatActivity {

    public FirebaseDatabase database = FirebaseDatabase.getInstance();
    public DatabaseReference dbReference = database.getReference("state");
    public TextView statusTextField;
    public String currentValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        statusTextField = findViewById(R.id.textView2);
        dbReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                currentValue = value;
                statusTextField.setText(currentValue);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                currentValue = "Failed to read value.";
            }
        });
    }

    public void changeColor(View view) {
        String state = view.getTag().toString();

        int color_id = ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark);
        switch(state) {
            case "yes" :
                color_id = ContextCompat.getColor(view.getContext(), R.color.yes);
                break;
            case "no" :
                color_id = ContextCompat.getColor(view.getContext(), R.color.no);
                break;
            case "maybe" :
                color_id = ContextCompat.getColor(view.getContext(), R.color.maybe);
                break;
        }
        statusTextField.setBackgroundColor(color_id);
        // DATABASE STUFF

        // Write a message to the database
        dbReference.setValue("User clicked \n" + state);

    }
}
