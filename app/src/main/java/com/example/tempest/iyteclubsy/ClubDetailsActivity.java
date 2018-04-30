package com.example.tempest.iyteclubsy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ClubDetailsActivity extends AppCompatActivity {

    private TextView clubName;
    private TextView clubDesc;
    private Button subButton;
    private ProgressBar progressBar;

    private DatabaseReference mDatabase;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private Intent intent;

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
                startActivity(new Intent(ClubDetailsActivity.this, MainActivity.class));
                finish();
                break;

            case R.id.menuClubList:
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
        setContentView(R.layout.activity_club_details);

        intent = getIntent();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        clubDesc = (TextView) findViewById(R.id.clubDesc);
        clubName = (TextView) findViewById(R.id.clubName);
        subButton = (Button) findViewById(R.id.subButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Club Details");
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
                    startActivity(new Intent(ClubDetailsActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };


        Query query = mDatabase.child("clubs");
        query.orderByKey().addChildEventListener(new ChildEventListener() {
            int counter = -1;
            int position = intent.getIntExtra("position", 0);
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                counter++;
                progressBar.setVisibility(View.VISIBLE);
                if(counter == position){
//                    clubs.add(dataSnapshot.getValue(Club.class));
                    System.out.println(dataSnapshot.getKey());
//                    descText.setText(dataSnapshot.child("desc").getValue().toString());
//                    nameText.setText(dataSnapshot.child("name").getValue().toString());
//                    textView.setText(dataSnapshot.child("name").getValue().toString());
                    String name = dataSnapshot.getKey().substring(0, 1).toUpperCase() + dataSnapshot.getKey().substring(1);
                    String desc = dataSnapshot.child("description").getValue().toString();
                    clubName.setText(name);
                    clubDesc.setText(desc);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

