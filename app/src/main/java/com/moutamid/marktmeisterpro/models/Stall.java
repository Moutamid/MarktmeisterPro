package com.moutamid.marktmeisterpro.models;

import java.util.ArrayList;

public class Stall {
    String name;
    ArrayList<StallModel> stall;

    public Stall(String name, ArrayList<StallModel> stall) {
        this.name = name;
        this.stall = stall;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<StallModel> getStall() {
        return stall;
    }

    public void setStall(ArrayList<StallModel> stall) {
        this.stall = stall;
    }
}
