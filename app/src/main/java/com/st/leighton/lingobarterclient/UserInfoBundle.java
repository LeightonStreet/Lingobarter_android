package com.st.leighton.lingobarterclient;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xuhao on 2016/5/10.
 */
public class UserInfoBundle {
    public String name;
    public String userid;
    public String username;
    public String imageURL;
    public String gender;
    public String birthday;
    public String tagline;
    public String biography;
    public String city;
    public String nationality;
    public HashSet<String> teach_languages;
    public HashMap<String, Integer> learn_languages;

    public void setName(String name) {
        this.name = name;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setTeach_languages(HashSet<String> teach_languages) {
        this.teach_languages = teach_languages;
    }

    public void setLearn_languages(HashMap<String, Integer> learn_languages) {
        this.learn_languages = learn_languages;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getName() {
        return name;
    }

    public String getUserid() {
        return userid;
    }

    public String getUsername() {
        return username;
    }

    public String getGender() {
        return gender;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getBiography() {
        return biography;
    }

    public String getTagline() {
        return tagline;
    }

    public String getCity() {
        return city;
    }

    public HashSet<String> getTeach_languages() {
        return teach_languages;
    }

    public HashMap<String, Integer> getLearn_languages() {
        return learn_languages;
    }

    public String getNationality() {
        return nationality;
    }

    public UserInfoBundle() {
        name = "";
        userid = "";
        username = "";
        imageURL = "";
        gender = "";
        birthday = "";
        tagline = "";
        biography = "";
        city = "";
        nationality = "";
        teach_languages = new HashSet<>();
        learn_languages = new HashMap<>();
    }

    public UserInfoBundle(String p_name, String p_userid, String p_username, String p_imageURL, String p_gender,
                          String p_birthday, String p_tagline, String p_biography, String p_city, String p_nationality,
                          HashSet<String> p_teach_languages, HashMap<String, Integer> p_learn_languages) {
        name = p_name;
        userid = p_userid;
        username = p_username;
        imageURL = p_imageURL;
        gender = p_gender;
        birthday = p_birthday;
        tagline = p_tagline;
        biography = p_biography;
        city = p_city;
        nationality = p_nationality;
        teach_languages = p_teach_languages;
        learn_languages = p_learn_languages;
    }
}
