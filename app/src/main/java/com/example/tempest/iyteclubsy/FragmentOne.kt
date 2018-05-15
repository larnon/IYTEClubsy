package com.example.tempest.iyteclubsy

/**
 * Created by Bora Gültekin on 29.04.2018.
 */

import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class FragmentOne : Fragment() {

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
        clubActionAdapter = ClubActionAdapter(clubActionItems, context)
        listview!!.adapter = clubActionAdapter

        return rootView
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
                clubList.add(dataSnapshot.key)
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


        val query = mDatabase!!.child("clubs")
        query.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
                progressBar!!.visibility = View.VISIBLE

                if (dataSnapshot.key in clubList) {
                    for (uniqueUserSnapshot in dataSnapshot.children) {
                        if (uniqueUserSnapshot.key.equals("announcements")) {
                            for (snapshot in uniqueUserSnapshot.children) {
                                val actionType = "announcement"
                                val subject = snapshot.key.toString()
                                val description = snapshot.child("description").value.toString()
                                val creationDate = snapshot.child("creationDate").value.toString()
                                clubActionItems.add(ClubAction(actionType, subject, description, null, null, creationDate, null))
                                Collections.sort(clubActionItems, object : Comparator<ClubAction> {
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

            override fun onChildMoved(dataSnapshot: DataSnapshot, s: String?) {

            }

            override fun onCancelled(databaseError: DatabaseError) {
                println("The read failed: " + databaseError.code)
            }

        })

        listview = rootView!!.findViewById<View>(R.id.listView) as ListView
        clubActionAdapter = ClubActionAdapter(clubActionItems, context)
        listview!!.adapter = clubActionAdapter

    }

}