package com.example.tempest.iyteclubsy

/**
 * Created by Tempest on 29.04.2018.
 */


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> FragmentOne.newInstance()
        1 -> FragmentTwo.newInstance()
        else -> null
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Tab 1 Item"
        1 -> "Tab 2 Item"
        else -> ""
    }

    override fun getCount(): Int = 2
}