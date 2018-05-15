package com.example.tempest.iyteclubsy

/**
 * Created by Bora Gültekin on 16.04.2018.
 */

import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import java.util.ArrayList
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MyClubsActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private val listItems = ArrayList<String>()
    private val clubs = ArrayList<Club>()
    private var adapter: ArrayAdapter<String>? = null
    private var listview: ListView? = null

    private var progressBar: ProgressBar? = null
    private var mDatabase: DatabaseReference? = null

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    private var clubList = ArrayList<String>()


    /* These two methods are for Toolbar */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuIYTEClubsy -> finish()

            R.id.menuProfile -> {
                startActivity(Intent(this@MyClubsActivity, ProfileActivity::class.java))
                finish()
            }

            R.id.menuClubList -> {
                startActivity(Intent(this@MyClubsActivity, ClubListActivity::class.java))
                finish()
            }

            R.id.menuMyClubs -> {
            }

            R.id.menuLogout -> {
                auth!!.signOut()
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_club_list)

        val progressBar = findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar.getIndeterminateDrawable().setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
        progressBar.visibility = View.VISIBLE

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        toolbar.title = "My Clubs"
        setSupportActionBar(toolbar)

        // Get Database reference
        mDatabase = FirebaseDatabase.getInstance().reference
        //get firebase auth instance
        auth = FirebaseAuth.getInstance()
        //get current user
        val user = FirebaseAuth.getInstance().currentUser


        authListener = FirebaseAuth.AuthStateListener {
            if (user == null) {
                startActivity(Intent(this@MyClubsActivity, LoginActivity::class.java))
                finish()
            }
        }



        val query = mDatabase!!.child("users").child(user!!.uid).child("clubs")
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                progressBar!!.visibility = View.VISIBLE
                val clubName = dataSnapshot.key.substring(0, 1).toUpperCase() + dataSnapshot.key.substring(1)
                adapter!!.add(clubName)
                clubList.add(clubName)
                progressBar!!.visibility = View.GONE
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


        listview = findViewById<View>(R.id.listView) as ListView
        listview!!.onItemClickListener = this
        adapter = ArrayAdapter(this, R.layout.list_white_text, R.id.list_content, listItems)
        listview!!.adapter = adapter


    }

    override fun onItemClick(l: AdapterView<*>, v: View, position: Int, id: Long) {
        val intent = Intent(this@MyClubsActivity, ClubDetailsActivity::class.java)
        val clubName = clubList[position]
        intent.putExtra("clubName", clubName)
        startActivity(intent)
        finish();
    }


}
