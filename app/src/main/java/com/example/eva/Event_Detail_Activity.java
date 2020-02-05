package com.example.eva;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

    public class Event_Detail_Activity extends AppCompatActivity {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_event_detail_view);

            Intent intent = getIntent();
            Event event = intent.getParcelableExtra("Event");

            String name = event.getEventName();
            TextView detailEventName = (TextView)findViewById(R.id.event_detail_name);
            detailEventName.setText(name);

            String location = event.getEventLocation();
            TextView detailEventLocation = (TextView)findViewById(R.id.event_detail_location);
            detailEventLocation.setText(location);

            String date = event.getEventDate();
            TextView detailEventDate= (TextView)findViewById(R.id.event_detail_date);
            detailEventDate.setText(date);

            Button cancelButton = (Button)findViewById(R.id.btnCancelEvent);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity();
                }
            });
        }

        private void startActivity(){
            Intent intent = new Intent(this, Overview_Activity.class);
            startActivity(intent);
        }
    }