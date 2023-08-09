package com.moutamid.marktmeisterpro.models;

import java.util.ArrayList;

public class Stall {
    String name, applicationID;
    ArrayList<StallModel> stall;

    public Stall(String name, String applicationID, ArrayList<StallModel> stall) {
        this.name = name;
        this.applicationID = applicationID;
        this.stall = stall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public ArrayList<StallModel> getStall() {
        return stall;
    }

    public void setStall(ArrayList<StallModel> stall) {
        this.stall = stall;
    }
}
