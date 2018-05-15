package com.example.tempest.iyteclubsy

/**
 * Created by Bora Gültekin on 29.04.2018.
 */

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem


import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    private var authListener: FirebaseAuth.AuthStateListener? = null
    private var auth: FirebaseAuth? = null

    /* These two methods are for Toolbar */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuIYTEClubsy -> {
            }

            R.id.menuProfile -> {
                startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
            }

            R.id.menuClubList -> {
                startActivity(Intent(this@MainActivity, ClubListActivity::class.java))
            }

            R.id.menuMyClubs -> {
                startActivity(Intent(this@MainActivity, MyClubsActivity::class.java))
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
        setContentView(R.layout.activity_main)


        //get firebase auth instance
        auth = FirebaseAuth.getInstance()

        //get current user
        val user = FirebaseAuth.getInstance().currentUser


        auth!!.addAuthStateListener {
            if(auth!!.currentUser == null){
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                this.finish()
            }
        }

        initToolbar()

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        val adapter = Adapter(supportFragmentManager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.title = "IYTE Clubsy"
    }

}