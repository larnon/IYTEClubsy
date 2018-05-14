package com.example.tempest.iyteclubsy

/**
 * Created by Bora Gültekin on 29.04.2018.
 */

import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.DataSnapshot
import java.util.*
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener




class FragmentOne : Fragment(), AdapterView.OnItemClickListener {


    private var listview: ListView? = null
    private var progressBar: ProgressBar? = null
    private var mDatabase: DatabaseReference? = null
    private var auth: FirebaseAuth? = null
    private val clubList = ArrayList<String>()
    private val clubActionItems = ArrayList<ClubAction>()
    private var clubActionAdapter: ClubActionAdapter? = null
    private var rootView: View? = null



    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_one, container, false)

        progressBar = rootView!!.findViewById<View>(R.id.progressBar) as ProgressBar
        progressBar!!.indeterminateDrawable.setColorFilter(resources.getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN)
        progressBar!!.visibility = View.VISIBLE

        listview = rootView!!.findViewById<View>(R.id.listView) as ListView
        listview!!.onItemClickListener = this
        clubActionAdapter = ClubActionAdapter(clubActionItems, context)
        listview!!.adapter = clubActionAdapter

        return rootView
    }


    override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        val latLng = clubActionItems[p2].latLng
        val latLngString = latLng.substring(latLng.indexOf("(") + 1, latLng.indexOf(")"))
        System.out.println(latLngString)

        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("geo:" + latLngString + "?q=" + latLngString + "?z=17")

        // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)

        // Make the Intent explicit by setting the Google Maps package
        mapIntent.`package` = "com.google.android.apps.maps"

        // Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }



    companion object {
        fun newInstance(): FragmentOne = FragmentOne()
    }


    override fun onStart() {
        super.onStart()

        clubActionAdapter!!.clear()
        clubActionItems.clear()

        // Get Database reference
        mDatabase = FirebaseDatabase.getInstance().reference
        //get firebase auth instance
        auth = FirebaseAuth.getInstance()
        //get current user
        val user = FirebaseAuth.getInstance().currentUser


        val query2 = mDatabase!!.child("users").child(user!!.uid).child("clubs")
        query2.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                progressBar!!.visibility = View.VISIBLE
                System.out.println(dataSnapshot.key)
                clubList.add(dataSnapshot.key)
                progressBar!!.visibility = View.GONE
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }
        })


        val query = mDatabase!!.child("clubs")
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                progressBar!!.visibility = View.VISIBLE

                if(dataSnapshot.key in clubList){
                    for (uniqueUserSnapshot in dataSnapshot.children) {
                        if (uniqueUserSnapshot.key.equals("events")){
                            for (snapshot in uniqueUserSnapshot.children) {
                                val actionType = "event"
                                val subject = snapshot.key.toString()
                                val description = snapshot.child("description").value.toString()
                                val eventTime = snapshot.child("eventTime").value.toString()
                                val eventDate = snapshot.child("eventDate").value.toString()
                                val creationDate = snapshot.child("creationDate").value.toString()
                                val latLng = snapshot.child("eventPlace").value.toString()
                                clubActionItems.add(ClubAction(actionType, subject, description, eventTime, eventDate, creationDate, latLng))
                                Collections.sort(clubActionItems , object : Comparator<ClubAction> {
                                    override fun compare(lhs: ClubAction, rhs: ClubAction): Int {
                                        // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                        return if (lhs.creationDate > rhs.creationDate) -1 else if (lhs.creationDate < rhs.creationDate) 1 else 0
                                    }
                                })
                                clubActionAdapter!!.notifyDataSetChanged()
                            }
                        }
                    }
                }
                progressBar!!.visibility = View.GONE

            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {

            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }

        })

        listview = rootView!!.findViewById<View>(R.id.listView) as ListView
        listview!!.onItemClickListener = this
        clubActionAdapter = ClubActionAdapter(clubActionItems, context)
        listview!!.adapter = clubActionAdapter


    }

}