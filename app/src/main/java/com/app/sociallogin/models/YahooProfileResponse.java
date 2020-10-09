package com.app.sociallogin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooProfileResponse {

    @SerializedName("sub")
    @Expose
    private String sub;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("given_name")
    @Expose
    private String givenName;
    @SerializedName("family_name")
    @Expose
    private String familyName;
    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("email_verified")
    @Expose
    private boolean emailVerified;
    @SerializedName("birthdate")
    @Expose
    private String birthdate;
    @SerializedName("profile_images")
    @Expose
    private ProfileImages profileImages;
    @SerializedName("nickname")
    @Expose
    private String nickname;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("picture")
    @Expose
    private String picture;

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public YahooProfileResponse withSub(String sub) {
        this.sub = sub;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public YahooProfileResponse withName(String name) {
        this.name = name;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public YahooProfileResponse withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public YahooProfileResponse withFamilyName(String familyName) {
        this.familyName = familyName;
        return this;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public YahooProfileResponse withLocale(String locale) {
        this.locale = locale;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public YahooProfileResponse withEmail(String email) {
        this.email = email;
        return this;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public YahooProfileResponse withEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
        return this;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public YahooProfileResponse withBirthdate(String birthdate) {
        this.birthdate = birthdate;
        return this;
    }

    public ProfileImages getProfileImages() {
        return profileImages;
    }

    public void setProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
    }

    public YahooProfileResponse withProfileImages(ProfileImages profileImages) {
        this.profileImages = profileImages;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public YahooProfileResponse withNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public YahooProfileResponse withGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public YahooProfileResponse withPicture(String picture) {
        this.picture = picture;
        return this;
    }

}