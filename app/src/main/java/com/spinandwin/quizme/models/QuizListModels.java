package com.spinandwin.quizme.models;

public class QuizListModels {
  private String cat_id, name, logo;

    public QuizListModels(String name, String logo, String cat_id) {
        this.name = name;
        this.logo = logo;
        this.cat_id = cat_id;
    }

    public QuizListModels() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }
}
