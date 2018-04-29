package com.example.tempest.iyteclubsy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

public class ClubListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ArrayList<String> listItems = new ArrayList<String>();
    private ArrayList<Club> clubs = new ArrayList<Club>();
    private ArrayAdapter<String> adapter;
    private ListView listview;

    private ProgressBar progressBar;
    private DatabaseReference mDatabase;

    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseAuth auth;




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
            case R.id.menuProfile:
                startActivity(new Intent(ClubListActivity.this, Main2Activity.class));
                finish();
                break;

            case R.id.menuClubList:
                break;

            case R.id.menuLogout:
                auth.signOut();
                startActivity(new Intent(ClubListActivity.this, LoginActivity.class));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Club List");
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
                    startActivity(new Intent(ClubListActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };

        Query query = mDatabase.child("clubs");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                progressBar.setVisibility(View.VISIBLE);
                adapter.add(dataSnapshot.getKey().toString());
                clubs.add(dataSnapshot.getValue(Club.class));
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


        listview = (ListView) findViewById(R.id.listView);
        listview.setOnItemClickListener(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        listview.setAdapter(adapter);


    }

    public void onItemClick(AdapterView<?> l, View v, int position, long id) {
        Intent intent = new Intent(ClubListActivity.this, ClubDetailsActivity.class);
        intent.putExtra("position", position);
        System.out.println(position);
        intent.putExtra("id", id);
        startActivity(intent);
        finish();
    }


}
