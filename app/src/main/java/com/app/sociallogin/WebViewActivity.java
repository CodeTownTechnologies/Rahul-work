package com.app.sociallogin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.app.sociallogin.api.ApiClient;
import com.app.sociallogin.api.ApiInterface;
import com.app.sociallogin.models.LinkedInTokenResponse;
import com.app.sociallogin.models.MicrosoftTokenResponse;
import com.app.sociallogin.models.YahooAccessTokenResponse;
import com.app.sociallogin.util.Constants;
import com.app.sociallogin.util.LoadingDialog;
import com.app.sociallogin.util.Util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebViewActivity extends AppCompatActivity {

    Intent mainIntent;

    private Context context;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);

        context = this;

        mainIntent = getIntent();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }

        if (mainIntent.hasExtra("type")) {

            if (mainIntent.getStringExtra("type").equals("linkedin")
                    && mainIntent.hasExtra("url")) {

                String url = mainIntent.getStringExtra("url");
                String state = mainIntent.getStringExtra("state");
                renderLinkedInLogin(url, state);

            } else if (mainIntent.getStringExtra("type").equals("yahoo")
                    && mainIntent.hasExtra("url")) {

                String url = mainIntent.getStringExtra("url");
                String state = mainIntent.getStringExtra("state");
                renderYahooLogin(url, state);

            }else if (mainIntent.getStringExtra("type").equals("outlook")
                    && mainIntent.hasExtra("url")) {

                String url = mainIntent.getStringExtra("url");
                String state = mainIntent.getStringExtra("state");
                renderOutlookLogin(url, state);

            }
        }

    }

    private void renderLinkedInLogin(String url, String state) {

        final WebView webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webview, url);
                LoadingDialog.cancelLoading();
                if (url.contains(Constants.LINKEDIN_REDIRECT_URI)) {
                    try {
                        URL redirectUrl = new URL(url);
                        Map<String, String> queries = Util.getQueryMap(redirectUrl.getQuery());
                        if (queries.get("state") != null && queries.get("state").equals(state)) {
                            if (queries.get("code") != null) {
                                String code = queries.get("code");
                                LoadingDialog.showLoadingDialog(context, getString(R.string.loading));

                                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                Call<LinkedInTokenResponse> apiCall = apiInterface.getLinkedInAccessToken(Constants.LINKEDIN_ACCESS_TOKEN_URL,
                                        "authorization_code", code,
                                        Constants.LINKEDIN_REDIRECT_URI, Constants.CLIENT_ID_LINKEDIN,
                                        Constants.CLIENT_SECRET_LINKEDIN);
                                apiCall.enqueue(new Callback<LinkedInTokenResponse>() {
                                    @Override
                                    public void onResponse(Call<LinkedInTokenResponse> call, Response<LinkedInTokenResponse> response) {
                                        LoadingDialog.cancelLoading();
                                        if (response.isSuccessful()) {
                                            LinkedInTokenResponse linkedInTokenResponse = response.body();

                                            if (linkedInTokenResponse != null && linkedInTokenResponse.getAccessToken() != null) {
                                                Intent resultIntent = new Intent();
                                                resultIntent.putExtra("access_token", linkedInTokenResponse.getAccessToken());
                                                resultIntent.putExtra("state_match", true);
                                                setResult(Activity.RESULT_OK, resultIntent);
                                                finish();
                                            } else {
                                                someError();
                                            }
                                        }else {
                                            someError();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LinkedInTokenResponse> call, Throwable t) {
                                        Log.e("onFailure: ", t.getLocalizedMessage());
                                        LoadingDialog.cancelLoading();

                                       someError();
                                    }
                                });


                            } else if (queries.get("error") != null) {
                                String errorDescription = queries.get("error_description");

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("error_description", errorDescription);
                                resultIntent.putExtra("state_match", true);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        } else {

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("state_match", false);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingDialog.showLoadingDialog(context, getString(R.string.loading));
            }
        });

        webview.loadUrl(url);


    }

    private void renderYahooLogin(String url, String state) {

        final WebView webview = (WebView) findViewById(R.id.webview);


        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webview, url);
                LoadingDialog.cancelLoading();
                if (url.contains(Constants.LINKEDIN_REDIRECT_URI)) {
                    try {
                        URL redirectUrl = new URL(url);
                        Map<String, String> queries = Util.getQueryMap(redirectUrl.getQuery());
                        if (queries.get("state") != null && queries.get("state").equals(state)) {
                            if (queries.get("code") != null) {
                                String code = queries.get("code");
                                LoadingDialog.showLoadingDialog(context, getString(R.string.loading));

                                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                Call<YahooAccessTokenResponse> apiCall = apiInterface.getYahooToken(Constants.YAHOO_OAUTH_GET_TOKEN_URL,
                                        "authorization_code",
                                        Constants.YAHOO_REDIRECT_URI,
                                        code,
                                        Constants.CONSUMER_KEY_YAHOO_WITHOUT_FIREBASE,
                                        Constants.CONSUMER_SECRET_YAHOO_WITHOUT_FIREBASE);
                                apiCall.enqueue(new Callback<YahooAccessTokenResponse>() {
                                    @Override
                                    public void onResponse(Call<YahooAccessTokenResponse> call, Response<YahooAccessTokenResponse> response) {
                                        LoadingDialog.cancelLoading();
                                        if (response.isSuccessful()) {
                                            YahooAccessTokenResponse yahooAccessTokenResponse = response.body();

                                            if (yahooAccessTokenResponse != null && yahooAccessTokenResponse.getAccessToken() != null) {
                                                Intent resultIntent = new Intent();
                                                resultIntent.putExtra("access_token", yahooAccessTokenResponse.getAccessToken());
                                                resultIntent.putExtra("state_match", true);
                                                setResult(Activity.RESULT_OK, resultIntent);
                                                finish();
                                            } else {
                                                someError();
                                            }
                                        } else {
                                            someError();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<YahooAccessTokenResponse> call, Throwable t) {
                                        LoadingDialog.cancelLoading();
                                        someError();
                                    }
                                });


                            } else {
                                someError();
                            }
                        } else {

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("state_match", false);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingDialog.showLoadingDialog(context, getString(R.string.loading));
            }
        });

        webview.loadUrl(url);
    }

    private void renderOutlookLogin(String url, String state) {

        final WebView webview = (WebView) findViewById(R.id.webview);

        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {


            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webview, url);
                LoadingDialog.cancelLoading();
                if (url.contains(Constants.MICROSOFT_REDIRECT_URI)) {
                    try {
                        URL redirectUrl = new URL(url);
                        Map<String, String> queries = Util.getQueryMap(redirectUrl.getQuery());
                        if (queries.get("state") != null && queries.get("state").equals(state)) {
                            if (queries.get("code") != null) {
                                String code = queries.get("code");
                                LoadingDialog.showLoadingDialog(context, getString(R.string.loading));

                                ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                                Call<MicrosoftTokenResponse> apiCall = apiInterface.getOutlookAccessToken(Constants.MICROSOFT_ACCESS_TOKEN_URL,
                                        "authorization_code", code,
                                        Constants.MICROSOFT_REDIRECT_URI, Constants.CLIENT_ID_MICROSOFT,
                                        Constants.CLIENT_SECRET_MICROSOFT);
                                apiCall.enqueue(new Callback<MicrosoftTokenResponse>() {
                                    @Override
                                    public void onResponse(Call<MicrosoftTokenResponse> call, Response<MicrosoftTokenResponse> response) {
                                        LoadingDialog.cancelLoading();
                                        if (response.isSuccessful()) {

                                            MicrosoftTokenResponse microsoftTokenResponse = response.body();

                                            if (microsoftTokenResponse != null && microsoftTokenResponse.getAccessToken() != null) {
                                                Intent resultIntent = new Intent();
                                                resultIntent.putExtra("access_token", microsoftTokenResponse.getAccessToken());
                                                resultIntent.putExtra("state_match", true);
                                                setResult(Activity.RESULT_OK, resultIntent);
                                                finish();
                                            } else {
                                                someError();
                                            }
                                        }else {
                                            someError();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MicrosoftTokenResponse> call, Throwable t) {
                                        Log.e("onFailure: ", t.getLocalizedMessage());
                                        LoadingDialog.cancelLoading();

                                        someError();
                                    }
                                });


                            } else if (queries.get("error") != null) {
                                String errorDescription = queries.get("error_description");

                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("error_description", errorDescription);
                                resultIntent.putExtra("state_match", true);
                                setResult(Activity.RESULT_OK, resultIntent);
                                finish();
                            }
                        } else {

                            Intent resultIntent = new Intent();
                            resultIntent.putExtra("state_match", false);
                            setResult(Activity.RESULT_OK, resultIntent);
                            finish();
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                LoadingDialog.showLoadingDialog(context, getString(R.string.loading));
            }
        });

        webview.loadUrl(url);


    }

    private void someError(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra("error_description", "Some error occurred");
        resultIntent.putExtra("state_match", true);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}