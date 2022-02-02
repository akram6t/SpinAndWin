package com.spinandwin.quizme.models;

public class RankModels {
    private String pos, uid;
    private long point;
    private String name, profile;

    public RankModels(String pos, String uid,long point, String name, String profile) {
        this.pos = pos;
        this.uid = uid;
        this.point = point;
        this.name = name;
        this.profile = profile;
    }

    public RankModels() {}

    public long getPoint() {
        return point;
    }

    public void setPoint(long point) {
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
