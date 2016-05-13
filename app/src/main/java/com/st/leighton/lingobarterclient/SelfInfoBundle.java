package com.st.leighton.lingobarterclient;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by xuhao on 2016/5/10.
 */
public class SelfInfoBundle {
    public String username;
    public String name;
    public String gender;
    public String birthday;
    public String imageURL;

    public String nationality;
    public String location;
    public HashSet<String> nativeLanguages;
    public HashMap<String, Integer> learnLanguages;

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

    public SelfInfoBundle() {
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getGender() {
        return gender;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getNationality() {
        return nationality;
    }

    public String getLocation() {
        return location;
    }

    public HashSet<String> getNativeLanguages() {
        return nativeLanguages;
    }

    public HashMap<String, Integer> getLearnLanguages() {
        return learnLanguages;
    }

    public String getTagline() {
        return tagline;
    }

    public String getBiography() {
        return biography;
    }

    public Boolean getStrictLanguageMatch() {
        return strictLanguageMatch;
    }

    public Boolean getSameGenderMatch() {
        return sameGenderMatch;
    }

    public Boolean getNearbySearch() {
        return nearbySearch;
    }

    public Boolean getAllSearch() {
        return allSearch;
    }

    public Boolean getPartnerConfirmation() {
        return partnerConfirmation;
    }

    public HashSet<String> getHideInformation() {
        return hideInformation;
    }

    public Integer getAgeRangeFrom() {
        return ageRangeFrom;
    }

    public Integer getAgeRangeTo() {
        return ageRangeTo;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setNativeLanguages(HashSet<String> nativeLanguages) {
        this.nativeLanguages = nativeLanguages;
    }

    public void setLearnLanguages(HashMap<String, Integer> learnLanguages) {
        this.learnLanguages = learnLanguages;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setStrictLanguageMatch(Boolean strictLanguageMatch) {
        this.strictLanguageMatch = strictLanguageMatch;
    }

    public void setSameGenderMatch(Boolean sameGenderMatch) {
        this.sameGenderMatch = sameGenderMatch;
    }

    public void setNearbySearch(Boolean nearbySearch) {
        this.nearbySearch = nearbySearch;
    }

    public void setAllSearch(Boolean allSearch) {
        this.allSearch = allSearch;
    }

    public void setPartnerConfirmation(Boolean partnerConfirmation) {
        this.partnerConfirmation = partnerConfirmation;
    }

    public void setHideInformation(HashSet<String> hideInformation) {
        this.hideInformation = hideInformation;
    }

    public void setAgeRangeFrom(Integer ageRangeFrom) {
        this.ageRangeFrom = ageRangeFrom;
    }

    public void setAgeRangeTo(Integer ageRangeTo) {
        this.ageRangeTo = ageRangeTo;
    }

    public void setBundle(String p_username, String p_name, String p_gender, String p_birthday,
                          String p_imageURL, String p_nationality, String p_location,
                          HashSet<String> p_nativeLanguages, HashMap<String, Integer> p_teachLanguages,
                          String p_tagline, String p_biography,
                          Boolean p_strictLanguageMatch, Boolean p_sameGenderMatch,
                          Boolean p_nearbySearch, Boolean p_allSearch, Boolean p_partnerConfirmation,
                          HashSet<String> p_hideInformation, Integer p_ageRangeFrom, Integer p_ageRangeTo) {
        username = p_username;
        name = p_name;
        gender = p_gender;
        birthday = p_birthday;
        imageURL = p_imageURL;

        nationality = p_nationality;
        location = p_location;
        nativeLanguages = p_nativeLanguages;
        learnLanguages = p_teachLanguages;

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
