package com.example.tempest.iyteclubsy;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class CreateClubActionActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView subject;
    private TextView description;
    private TextView time;
    private Button createButton;
    private DatabaseReference mDatabase;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private Bundle bundle;
    private String actionType;
    private String clubName;


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
        }
        if(actionType.equals("announcement")){
            toolbar.setTitle("Create Announcement");
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
                String subjectString = subject.getText().toString().trim();
                String descriptionString = description.getText().toString().trim();
                if (TextUtils.isEmpty(subjectString) || TextUtils.isEmpty(descriptionString)) {
                    Toast.makeText(getApplicationContext(), "Fill in the details!", Toast.LENGTH_SHORT).show();
                    return;
                }

                mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("description").setValue(descriptionString);
                mDatabase.child("clubs").child(clubName).child(actionType + "s").child(subjectString).child("creationDate").setValue(ServerValue.TIMESTAMP);



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
}
