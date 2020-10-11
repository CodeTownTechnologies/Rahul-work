package com.app.sociallogin.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MicrosoftProfileResponse {

    @SerializedName("@odata.context")
    @Expose
    private String odataContext;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("surname")
    @Expose
    private String surname;
    @SerializedName("givenName")
    @Expose
    private String givenName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("userPrincipalName")
    @Expose
    private String userPrincipalName;
    @SerializedName("businessPhones")
    @Expose
    private List<Object> businessPhones = null;
    @SerializedName("jobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("mail")
    @Expose
    private String mail;
    @SerializedName("mobilePhone")
    @Expose
    private String mobilePhone;
    @SerializedName("officeLocation")
    @Expose
    private String officeLocation;
    @SerializedName("preferredLanguage")
    @Expose
    private String preferredLanguage;

    public String getOdataContext() {
        return odataContext;
    }

    public void setOdataContext(String odataContext) {
        this.odataContext = odataContext;
    }

    public MicrosoftProfileResponse withOdataContext(String odataContext) {
        this.odataContext = odataContext;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public MicrosoftProfileResponse withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public MicrosoftProfileResponse withSurname(String surname) {
        this.surname = surname;
        return this;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public MicrosoftProfileResponse withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MicrosoftProfileResponse withId(String id) {
        this.id = id;
        return this;
    }

    public String getUserPrincipalName() {
        return userPrincipalName;
    }

    public void setUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
    }

    public MicrosoftProfileResponse withUserPrincipalName(String userPrincipalName) {
        this.userPrincipalName = userPrincipalName;
        return this;
    }

    public List<Object> getBusinessPhones() {
        return businessPhones;
    }

    public void setBusinessPhones(List<Object> businessPhones) {
        this.businessPhones = businessPhones;
    }

    public MicrosoftProfileResponse withBusinessPhones(List<Object> businessPhones) {
        this.businessPhones = businessPhones;
        return this;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public MicrosoftProfileResponse withJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
        return this;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public MicrosoftProfileResponse withMail(String mail) {
        this.mail = mail;
        return this;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public MicrosoftProfileResponse withMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
        return this;
    }

    public String getOfficeLocation() {
        return officeLocation;
    }

    public void setOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
    }

    public MicrosoftProfileResponse withOfficeLocation(String officeLocation) {
        this.officeLocation = officeLocation;
        return this;
    }

    public String getPreferredLanguage() {
        return preferredLanguage;
    }

    public void setPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
    }

    public MicrosoftProfileResponse withPreferredLanguage(String preferredLanguage) {
        this.preferredLanguage = preferredLanguage;
        return this;
    }

}