package com.app.sociallogin;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {




    @GET(Constants.YAHOO_OPENID_URL + "userinfo")
    Call<ResponseBody> getYahooProfile(@Header("Authorization") String accessToken);


    @POST(Constants.YAHOO_OAUTH + "get_token")
    @FormUrlEncoded
    Call<YahooAccessTokenResponse> getYahooToken(@Field("client_id") String clientId,
                                                 @Field("client_secret") String clientSecret,
                                                 @Field("redirect_uri") String redirectUri,
                                                 @Field("code") String authCode,
                                                 @Field("grant_type") String grantType);


    @GET(Constants.YAHOO_CONTACTS + "user/{userId}/contacts")
    Call<ResponseBody> getYahooContacts(@Header("Authorization") String accessToken,
                                        @Query("userId") String userId,
                                        @Query("format") String json);


}

