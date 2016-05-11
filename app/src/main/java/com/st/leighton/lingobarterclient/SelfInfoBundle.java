package com.st.leighton.lingobarterclient;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xuhao on 2016/5/10.
 */
public class SelfInfoBundle {
    private boolean isGood;

    public String username;
    public String name;
    public String gender;
    public String birthday;
    public String imageURL;

    public String nationality;
    public String location;
    public HashSet<String> nativeLanguages;
    public HashMap<String, Integer> teachLanguages;

    public String tagline;
    public String biography;

    public Boolean strictLanguageMatch;
    public Boolean sameGenderMatch;
    public Boolean nearbySearch;
    public Boolean allSearch;
    public Boolean partnerConfirmation;

    public HashSet<String> hideInformation;
    public Integer ageRangeFrom;
    public Integer ageRangeTo;

    public boolean isGood() {
        return isGood;
    }

    public SelfInfoBundle() {
        isGood = false;
    }

    public void setBundle(String p_username, String p_name, String p_gender, String p_birthday,
                          String p_imageURL, String p_nationality, String p_location,
                          HashSet<String> p_nativeLanguages, HashMap<String, Integer> p_teachLanguages,
                          String p_tagline, String p_biography,
                          Boolean p_strictLanguageMatch, Boolean p_sameGenderMatch,
                          Boolean p_nearbySearch, Boolean p_allSearch, Boolean p_partnerConfirmation,
                          HashSet<String> p_hideInformation, Integer p_ageRangeFrom, Integer p_ageRangeTo) {
        isGood = true;
        username = p_username;
        name = p_name;
        gender = p_gender;
        birthday = p_birthday;
        imageURL = p_imageURL;

        nationality = p_nationality;
        location = p_location;
        nativeLanguages = p_nativeLanguages;
        teachLanguages = p_teachLanguages;

        tagline = p_tagline;
        biography = p_biography;

        strictLanguageMatch = p_strictLanguageMatch;
        sameGenderMatch = p_sameGenderMatch;
        nearbySearch = p_nearbySearch;
        allSearch = p_allSearch;
        partnerConfirmation = p_partnerConfirmation;

        hideInformation = p_hideInformation;
        ageRangeFrom = p_ageRangeFrom;
        ageRangeTo = p_ageRangeTo;
    }
}
