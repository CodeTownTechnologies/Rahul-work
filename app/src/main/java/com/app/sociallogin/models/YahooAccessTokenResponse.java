package com.app.sociallogin.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class YahooAccessTokenResponse {

    @SerializedName("access_token")
    @Expose
    private String accessToken;
    @SerializedName("token_type")
    @Expose
    private String tokenType;
    @SerializedName("expires_in")
    @Expose
    private int expiresIn;
    @SerializedName("refresh_token")
    @Expose
    private String refreshToken;
    @SerializedName("xoauth_yahoo_guid")
    @Expose
    private String xoauthYahooGuid;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public YahooAccessTokenResponse withAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public YahooAccessTokenResponse withTokenType(String tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }

    public YahooAccessTokenResponse withExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        return this;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public YahooAccessTokenResponse withRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
        return this;
    }

    public String getXoauthYahooGuid() {
        return xoauthYahooGuid;
    }

    public void setXoauthYahooGuid(String xoauthYahooGuid) {
        this.xoauthYahooGuid = xoauthYahooGuid;
    }

    public YahooAccessTokenResponse withXoauthYahooGuid(String xoauthYahooGuid) {
        this.xoauthYahooGuid = xoauthYahooGuid;
        return this;
    }

}