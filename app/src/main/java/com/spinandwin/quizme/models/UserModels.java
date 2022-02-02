package com.spinandwin.quizme.models;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class UserModels {
    private String id, name, email, profile;
    private int spin = 10;
    private int scratch = 10;
    private long coins = 100;
    private String todayDate;
    private String createdBy;

    public UserModels() {}

    public UserModels(String id, String name, String email, String profile, String todayDate, String createdBy) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profile = profile;
        this.todayDate = todayDate;
        this.createdBy = createdBy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public int getSpin() {
        return spin;
    }

    public int getScratch() {
        return scratch;
    }

    public String getTodayDate() {
        return todayDate;
    }

    public void setTodayDate(String todayDate) {
        this.todayDate = todayDate;
    }

    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
