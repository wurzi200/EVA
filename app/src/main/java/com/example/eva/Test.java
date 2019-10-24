package com.example.eva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Test extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void changeColor(View view) {
        String test = view.getTag().toString();
        TextView status = findViewById(R.id.textView2);

        int color_id = ContextCompat.getColor(view.getContext(), R.color.colorPrimaryDark);
        switch(test) {
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
        status.setBackgroundColor(color_id);

        // DATABASE STUFF

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("state");

        myRef.setValue(test);

    }
}
