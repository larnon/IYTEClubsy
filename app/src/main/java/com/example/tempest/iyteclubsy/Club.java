package com.example.tempest.iyteclubsy;

import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

/**
 * Created by Tempest on 27.03.2018.
 */

public class Club {
    private String name;
    private String desc;
    private ArrayList<String> members = new ArrayList<String>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

}
