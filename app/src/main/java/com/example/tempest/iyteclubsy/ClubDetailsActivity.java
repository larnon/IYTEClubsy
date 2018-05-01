package com.example.tempest.iyteclubsy;

import android.content.Intent;
import android.graphics.PorterDuff;
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
    private Button unSubButton;
    private ProgressBar progressBar;

    private DatabaseReference mDatabase;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;

    private Intent intent;
    private Bundle bundle;
    private String clubNameToQuery;

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

            case R.id.menuProfile:
                startActivity(new Intent(ClubDetailsActivity.this, ProfileActivity.class));
                finish();
                break;

            case R.id.menuClubList:
                startActivity(new Intent(ClubDetailsActivity.this, ClubListActivity.class));
                finish();
                break;

            case R.id.menuMyClubs:
                startActivity(new Intent(ClubDetailsActivity.this, MyClubsActivity.class));
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
        bundle = intent.getExtras();
        if(bundle != null){
            clubNameToQuery = (String) bundle.get("clubName");
        }

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        progressBar.setVisibility(View.VISIBLE);

        clubDesc = (TextView) findViewById(R.id.clubDesc);
        clubName = (TextView) findViewById(R.id.clubName);
        subButton = (Button) findViewById(R.id.subButton);
        unSubButton = (Button) findViewById(R.id.unSubButton);

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
//            int counter = -1;
//            int position = intent.getIntExtra("position", 0);
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.VISIBLE);

                if(dataSnapshot.getKey().equals(clubNameToQuery.toLowerCase())){
                    String name = dataSnapshot.getKey().substring(0, 1).toUpperCase() + dataSnapshot.getKey().substring(1);
                    String desc = dataSnapshot.child("description").getValue().toString();
                    clubName.setText(name);
                    clubDesc.setText(desc);
                }

//                counter++;
//                if(counter == position){
//                    System.out.println(dataSnapshot.getKey());
//                    String name = dataSnapshot.getKey().substring(0, 1).toUpperCase() + dataSnapshot.getKey().substring(1);
//                    String desc = dataSnapshot.child("description").getValue().toString();
//                    clubName.setText(name);
//                    clubDesc.setText(desc);
//                }

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

    @Override
    protected void onStart() {
        super.onStart();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query2 = mDatabase.child("users").child(user.getUid()).child("clubs");
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                unSubButton.setVisibility(View.GONE);
                subButton.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                if (dataSnapshot.hasChild(clubNameToQuery.toLowerCase())) {
                    unSubButton.setVisibility(View.VISIBLE);
                }
                else{
                    subButton.setVisibility(View.VISIBLE);
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });

        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(user.getUid()).child("clubs").child(clubNameToQuery.toLowerCase()).setValue("member");
                mDatabase.child("clubs").child(clubNameToQuery.toLowerCase()).child("members").child(user.getUid()).setValue("member");
            }
        });

        unSubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase.child("users").child(user.getUid()).child("clubs").child(clubNameToQuery.toLowerCase()).removeValue();
                mDatabase.child("clubs").child(clubNameToQuery.toLowerCase()).child("members").child(user.getUid()).removeValue();
            }
        });

    }
}

