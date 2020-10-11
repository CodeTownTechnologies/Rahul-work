package com.app.sociallogin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MicrosoftTokenResponse {

    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("scope")
    @Expose
    private String scope;
    @SerializedName("expires_in")
    @Expose
    private int expiresIn;
    @SerializedName("ext_expires_in")
    @Expose
    private int extExpiresIn;
    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("id_token")
    @Expose
    private String idToken;

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public MicrosoftTokenResponse withTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public MicrosoftTokenResponse withScope(String scope) {
        this.scope = scope;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public MicrosoftTokenResponse withExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public int getExtExpiresIn() {
        return extExpiresIn;
    }

    public void setExtExpiresIn(int extExpiresIn) {
        this.extExpiresIn = extExpiresIn;
    }

    public MicrosoftTokenResponse withExtExpiresIn(int extExpiresIn) {
        this.extExpiresIn = extExpiresIn;
        return this;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public MicrosoftTokenResponse withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public MicrosoftTokenResponse withIdToken(String idToken) {
        this.idToken = idToken;
        return this;
    }

}