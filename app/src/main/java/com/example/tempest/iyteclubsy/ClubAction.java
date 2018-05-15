package com.example.tempest.iyteclubsy;

/**
 * Created by Bora Gültekin on 30.04.2018.
 */

public class ClubAction {

    private String clubName;
    private String actionType;
    private String subject;
    private String description;
    private String eventTime;
    private String eventDate;
    private String creationDate;
    private String latLng;

    public ClubAction(String clubName, String actionType, String subject, String description, String eventTime, String eventDate, String creationDate, String latLng) {
        this.clubName = clubName;
        this.actionType = actionType;
        this.subject = subject;
        this.description = description;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.creationDate = creationDate;
        this.latLng = latLng;
    }

    public String getClubName() {
        return clubName;
    }
    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getActionType() {
        return actionType;
    }
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventTime() {
        return eventTime;
    }
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventDate() {
        return eventDate;
    }
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getCreationDate() {
        return creationDate;
    }
    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getLatLng() {
        return latLng;
    }
    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }



}
