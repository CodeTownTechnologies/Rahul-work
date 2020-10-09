
package com.app.sociallogin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfilePicture {

    @SerializedName("displayImage")
    @Expose
    private String displayImage;

    public String getDisplayImage() {
        return displayImage;
    }

    public void setDisplayImage(String displayImage) {
        this.displayImage = displayImage;
    }

    public ProfilePicture withDisplayImage(String displayImage) {
        this.displayImage = displayImage;
        return this;
    }

}
