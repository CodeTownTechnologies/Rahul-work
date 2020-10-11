package com.app.sociallogin.api;

import com.app.sociallogin.models.LinkedInTokenResponse;
import com.app.sociallogin.models.MicrosoftTokenResponse;
import com.app.sociallogin.models.YahooAccessTokenResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface ApiInterface {


    @GET()
    Call<ResponseBody> getYahooProfile(@Url String url, @Header("Authorization") String accessToken);


    @POST()
    @FormUrlEncoded
    Call<YahooAccessTokenResponse> getYahooToken(@Url String url,
                                                 @Field("grant_type") String grantType,
                                                 @Field("redirect_uri") String redirectUri,
                                                 @Field("code") String authCode,
                                                 @Field("client_id") String clientId,
                                                 @Field("client_secret") String clientSecret);


    @GET()
    Call<ResponseBody> getYahooContacts(@Url String url,
                                        @Header("Authorization") String accessToken,
                                        @Query("userId") String userId,
                                        @Query("format") String json);


    @GET()
    Call<LinkedInTokenResponse> getLinkedInAccessToken(@Url String url,
                                                       @Query("grant_type") String grantType,
                                                       @Query("code") String code,
                                                       @Query("redirect_uri") String redirect_uri,
                                                       @Query("client_id") String clientId,
                                                       @Query("client_secret") String clientSecret);

    @GET()
    Call<ResponseBody> getLinkedInProfile(@Url String url,
                                          @Header("Authorization") String accessToken);


    @FormUrlEncoded
    @POST()
    Call<MicrosoftTokenResponse> getOutlookAccessToken(@Url String url,
                                                       @Field("grant_type") String grantType,
                                                       @Field("code") String code,
                                                       @Field("redirect_uri") String redirect_uri,
                                                       @Field("client_id") String clientId,
                                                       @Field("client_secret") String clientSecret);

    @GET()
    Call<ResponseBody> getOutlookProfile(@Url String url,
                                          @Header("Authorization") String accessToken);


}

