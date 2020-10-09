package com.app.sociallogin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProfileImages {

    @SerializedName("image32")
    @Expose
    private String image32;
    @SerializedName("image64")
    @Expose
    private String image64;
    @SerializedName("image128")
    @Expose
    private String image128;
    @SerializedName("image192")
    @Expose
    private String image192;

    public String getImage32() {
        return image32;
    }

    public void setImage32(String image32) {
        this.image32 = image32;
    }

    public ProfileImages withImage32(String image32) {
        this.image32 = image32;
        return this;
    }

    public String getImage64() {
        return image64;
    }

    public void setImage64(String image64) {
        this.image64 = image64;
    }

    public ProfileImages withImage64(String image64) {
        this.image64 = image64;
        return this;
    }

    public String getImage128() {
        return image128;
    }

    public void setImage128(String image128) {
        this.image128 = image128;
    }

    public ProfileImages withImage128(String image128) {
        this.image128 = image128;
        return this;
    }

    public String getImage192() {
        return image192;
    }

    public void setImage192(String image192) {
        this.image192 = image192;
    }

    public ProfileImages withImage192(String image192) {
        this.image192 = image192;
        return this;
    }

}