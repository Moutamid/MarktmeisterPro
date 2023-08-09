package com.moutamid.marktmeisterpro.models;

public class UserModel {
    String name, surName, position, profileLink;

    public UserModel() {
    }

    public UserModel(String name, String surName, String position, String profileLink) {
        this.name = name;
        this.surName = surName;
        this.position = position;
        this.profileLink = profileLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }
}
