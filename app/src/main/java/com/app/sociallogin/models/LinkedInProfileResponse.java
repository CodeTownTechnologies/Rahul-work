
package com.app.sociallogin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LinkedInProfileResponse {

    @SerializedName("localizedLastName")
    @Expose
    private String localizedLastName;
    @SerializedName("profilePicture")
    @Expose
    private ProfilePicture profilePicture;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("localizedFirstName")
    @Expose
    private String localizedFirstName;

    public String getLocalizedLastName() {
        return localizedLastName;
    }

    public void setLocalizedLastName(String localizedLastName) {
        this.localizedLastName = localizedLastName;
    }

    public LinkedInProfileResponse withLocalizedLastName(String localizedLastName) {
        this.localizedLastName = localizedLastName;
        return this;
    }

    public ProfilePicture getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
    }

    public LinkedInProfileResponse withProfilePicture(ProfilePicture profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LinkedInProfileResponse withId(String id) {
        this.id = id;
        return this;
    }

    public String getLocalizedFirstName() {
        return localizedFirstName;
    }

    public void setLocalizedFirstName(String localizedFirstName) {
        this.localizedFirstName = localizedFirstName;
    }

    public LinkedInProfileResponse withLocalizedFirstName(String localizedFirstName) {
        this.localizedFirstName = localizedFirstName;
        return this;
    }

}
