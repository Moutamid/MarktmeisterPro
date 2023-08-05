package com.moutamid.marktmeisterpro.models;

public class StallModel {
    String applicationID, stallName, item, beschreibung, nr, date, imageURL;
    boolean add;

    public StallModel(String applicationID, String stallName, String item, String beschreibung, String nr, String date, String imageURL, boolean add) {
        this.applicationID = applicationID;
        this.stallName = stallName;
        this.item = item;
        this.beschreibung = beschreibung;
        this.nr = nr;
        this.date = date;
        this.imageURL = imageURL;
        this.add = add;
    }

    public String getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(String applicationID) {
        this.applicationID = applicationID;
    }

    public String getStallName() {
        return stallName;
    }

    public void setStallName(String stallName) {
        this.stallName = stallName;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public boolean isAdd() {
        return add;
    }

    public void setAdd(boolean add) {
        this.add = add;
    }
}
