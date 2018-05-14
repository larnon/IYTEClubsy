package com.example.tempest.iyteclubsy;

/**
 * Created by Bora Gültekin on 7.05.2018.
 */

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;


import com.google.android.gms.location.places.Place;

public class CreateClubActionActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView subject;
    private TextView description;
    private TextView time;
    private TextView date;
    private TextView place;
    private TextView timeTitle;
    private TextView dateTitle;
    private TextView placeTitle;
    private Button timePickButton;
    private Button datePickButton;
    private Button placePickButton;
    private Button createButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private Bundle bundle;
    private String actionType;
    private String clubName;

    private static final int PLACE_PICKER_REQ_CODE = 2;
    private String latLng;


    /* These two methods are for Toolbar */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.menuIYTEClubsy:
                startActivity(new Intent(CreateClubActionActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.menuProfile:
                startActivity(new Intent(CreateClubActionActivity.this, ProfileActivity.class));
                finish();
                break;

            case R.id.menuClubList:
                startActivity(new Intent(CreateClubActionActivity.this, ClubListActivity.class));
                finish();
                break;

            case R.id.menuMyClubs:
                startActivity(new Intent(CreateClubActionActivity.this, MyClubsActivity.class));
                finish();
                break;

            case R.id.menuLogout:
                auth.signOut();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club_action);

        subject = (TextView) findViewById(R.id.subject);
        description = (TextView) findViewById(R.id.description);
        time = (TextView) findViewById(R.id.time);
        timeTitle = (TextView) findViewById(R.id.timeTitle);
        date = (TextView) findViewById(R.id.date);
        dateTitle = (TextView) findViewById(R.id.dateTitle);
        place = (TextView) findViewById(R.id.place);
        placeTitle = (TextView) findViewById(R.id.placeTitle);
        timePickButton = (Button) findViewById(R.id.timePickButton);
        datePickButton = (Button) findViewById(R.id.datePickButton);
        placePickButton = (Button) findViewById(R.id.placePickButton);
        createButton = (Button) findViewById(R.id.createButton);


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        bundle = getIntent().getExtras();
        if(bundle != null){
            actionType = (String) bundle.get("actionType");
            clubName = (String) bundle.get("clubName");
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(actionType.equals("event")){
            toolbar.setTitle("Create Event");
            time.setVisibility(View.VISIBLE);
            date.setVisibility(View.VISIBLE);
            timePickButton.setVisibility(View.VISIBLE);
            datePickButton.setVisibility(View.VISIBLE);
            timeTitle.setVisibility(View.VISIBLE);
            dateTitle.setVisibility(View.VISIBLE);
            place.setVisibility(View.VISIBLE);
            placePickButton.setVisibility(View.VISIBLE);
            placeTitle.setVisibility(View.VISIBLE);
        }
        if(actionType.equals("announcement")){
            toolbar.setTitle("Create Announcement");
            time.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            timePickButton.setVisibility(View.GONE);
            datePickButton.setVisibility(View.GONE);
            timeTitle.setVisibility(View.GONE);
            dateTitle.setVisibility(View.GONE);
            place.setVisibility(View.GONE);
            placePickButton.setVisibility(View.GONE);
            placeTitle.setVisibility(View.GONE);
        }
        setSupportActionBar(toolbar);


        // Get Database reference
        mDatabase = FirebaseDatabase.getInstance().getReference();
        //get firebase auth instance
        auth = FirebaseAuth.getInstance();
        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (user == null) {
                    startActivity(new Intent(CreateClubActionActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(actionType.equals("announcement")) {
                    String subjectString = subject.getText().toString().trim();
                    String descriptionString = description.getText().toString().trim();
                    if (TextUtils.isEmpty(subjectString) || TextUtils.isEmpty(descriptionString)) {
                        Toast.makeText(getApplicationContext(), "Fill in the details!", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        return;
                    }

                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("description").setValue(descriptionString);
                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("creationDate").setValue(ServerValue.TIMESTAMP);
                }
                else if(actionType.equals("event")) {
                    String subjectString = subject.getText().toString().trim();
                    String descriptionString = description.getText().toString().trim();
                    String timeString = time.getText().toString().trim();
                    String dateString = date.getText().toString().trim();
                    String placeString = place.getText().toString().trim();
                    if (TextUtils.isEmpty(subjectString) || TextUtils.isEmpty(descriptionString) || TextUtils.isEmpty(timeString) || TextUtils.isEmpty(dateString) || TextUtils.isEmpty(placeString)) {
                        Toast.makeText(getApplicationContext(), "Fill in the details!", Toast.LENGTH_SHORT).show();
                        return;
                    }

//                    Calendar calendar = Calendar.getInstance();
//                    int currentYear = calendar.get(Calendar.YEAR);
//                    int currentMonth = calendar.get(Calendar.MONTH);
//                    int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
//                    java.text.DateFormat dateTimeInstance = SimpleDateFormat.getDateInstance();
//                    String formattedDate = dateTimeInstance.format(calendar.getTime());
//                    if (currentYear == ) {
//                        Toast.makeText(getApplicationContext(), "Fill in the details!", Toast.LENGTH_SHORT).show();
//                        return;
//                    }

                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("description").setValue(descriptionString);
                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("creationDate").setValue(ServerValue.TIMESTAMP);
                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("eventTime").setValue(timeString);
                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("eventDate").setValue(dateString);
                    mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("eventPlace").setValue(latLng);
                }



                Toast.makeText(getApplicationContext(), "Action created", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

                finish();
            }
        });
    }





    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }


    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @SuppressLint("MissingPermission")
    public void showPlacePickerDialog(View v) {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQ_CODE);
        } catch (Exception e) {

        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQ_CODE) {
            if (resultCode == RESULT_OK) {
                Place selectedPlace = PlacePicker.getPlace(data, this);
                latLng = selectedPlace.getLatLng().toString();
                place.setText(selectedPlace.getAddress().toString());
            }
        }
    }

}
