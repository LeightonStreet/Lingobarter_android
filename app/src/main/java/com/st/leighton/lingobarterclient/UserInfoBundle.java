package com.st.leighton.lingobarterclient;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xuhao on 2016/5/10.
 */
public class UserInfoBundle {
    public String name;
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

    public UserInfoBundle(String p_name, String p_username, String p_imageURL, String p_gender,
            String p_birthday, String p_tagline, String p_biography, String p_city, String p_nationality,
            HashSet<String> p_teach_languages, HashMap<String, Integer> p_learn_languages) {
        name = p_name;
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
