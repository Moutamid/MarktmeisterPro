package com.moutamid.marktmeisterpro.models;

public class EventModel {
    String ID, city;

    public EventModel(String ID, String city) {
        this.ID = ID;
        this.city = city;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
