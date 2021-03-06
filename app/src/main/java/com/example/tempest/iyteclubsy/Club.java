package com.example.tempest.iyteclubsy;


import java.util.ArrayList;

/**
 * Created by Bora Gültekin on 27.03.2018.
 */

public class Club {
    private String description;
    private ArrayList<String> members = new ArrayList<String>();
    private ArrayList<ClubAction> events = new ArrayList<ClubAction>();
    private ArrayList<ClubAction> announcements = new ArrayList<ClubAction>();


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public ArrayList<ClubAction> getEvents() {
        return events;
    }

    public ArrayList<ClubAction> getAnnouncements() {
        return announcements;
    }
}
