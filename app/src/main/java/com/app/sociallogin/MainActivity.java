package com.app.sociallogin;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.app.sociallogin.api.ApiClient;
import com.app.sociallogin.api.ApiInterface;
import com.app.sociallogin.models.LinkedInProfileResponse;
import com.app.sociallogin.models.YahooProfileResponse;
import com.app.sociallogin.util.Constants;
import com.app.sociallogin.util.Util;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.OAuthProvider;
import com.google.gson.Gson;
import com.microsoft.identity.client.AuthenticationCallback;
import com.microsoft.identity.client.IAccount;
import com.microsoft.identity.client.IAuthenticationResult;
import com.microsoft.identity.client.IPublicClientApplication;
import com.microsoft.identity.client.ISingleAccountPublicClientApplication;
import com.microsoft.identity.client.PublicClientApplication;
import com.microsoft.identity.client.exception.MsalClientException;
import com.microsoft.identity.client.exception.MsalException;
import com.microsoft.identity.client.exception.MsalServiceException;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();


    private static final int GOOGLE_SIGN_IN_REQUEST = 12;
    private static final int LINKEDIN_LOGIN_REQUEST = 13;
    private static final int YAHOO_LOGIN_REQUEST = 14;


    private GoogleSignInClient googleSignInClient;

    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    Button btnGoogleLoginWithFirebase;
    Button btnFacebookLoginWithoutFirebase;
    Button btnOutlookLoginWithoutFirebase;
    Button btnOutlookLoginWithFirebase;
    Button btnYahooLoginWithoutFirebase;
    Button btnYahooLoginWithFirebase;
    Button btnLinkedInLoginWithoutFirebase;
    Button btnLogout;

    TextView tvMessage;

    private ProgressDialog progress;
    private String yahooAccessToken = "";
    private FirebaseAuth mAuth;

    private CallbackManager callbackManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());

        btnGoogleLoginWithFirebase = findViewById(R.id.btnGoogleLoginWithFirebase);
        btnFacebookLoginWithoutFirebase = findViewById(R.id.btnFacebookLoginWithoutFirebase);
        btnOutlookLoginWithoutFirebase = findViewById(R.id.btnOutlookLoginWithoutFirebase);
        btnOutlookLoginWithFirebase = findViewById(R.id.btnOutlookLoginWithFirebase);
        btnYahooLoginWithoutFirebase = findViewById(R.id.btnYahooLoginWithoutFirebase);
        btnYahooLoginWithFirebase = findViewById(R.id.btnYahooLoginWithFirebase);
        btnLinkedInLoginWithoutFirebase = findViewById(R.id.btnLinkedInLoginWithoutFirebase);
        btnLogout = findViewById(R.id.btnLogout);
        tvMessage = findViewById(R.id.tvMessage);

        Intent intent = getIntent();


        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            //yahoo code
            /*if (uri != null) {
                String authCode = uri.getQueryParameter("code");
                Toast.makeText(this, authCode, Toast.LENGTH_LONG).show();
                requestyahooAccessToken(authCode);
            }*/
        }

        mAuth = FirebaseAuth.getInstance();

        onLogout();

        setUpOutlookWithoutFirebase();

        btnGoogleLoginWithFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onGoogleLoginWithFirebase();
            }
        });

        btnFacebookLoginWithoutFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFbLoginWithoutFirebase();
            }
        });

        btnOutlookLoginWithoutFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOutlookLoginWithoutFirebase();
            }
        });

        btnOutlookLoginWithFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOutlookLoginWithFirebase();
            }
        });

        btnYahooLoginWithoutFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onYahooLoginWithoutFirebase();
            }
        });

        btnYahooLoginWithFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onYahooLoginWithFirebase();

            }
        });

        btnLinkedInLoginWithoutFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLinkedInLoginWithoutFirebase();
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogout();
            }
        });


        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.app.sociallogin", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }

    private void onLogout() {

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            FirebaseAuth.getInstance().signOut();
        }

        if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            CookieManager.getInstance().removeAllCookies(null);
        } else {
            CookieManager.getInstance().removeAllCookie();
        }

        tvMessage.setText("");

    }


    /*Google Login with Firebase*/
    private void onGoogleLoginWithFirebase() {


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .requestId()
                .build();


        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST);
    }


    /*Facebook Login without Firebase*/
    private void onFbLoginWithoutFirebase() {

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        startProgressBar();
                        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject user1, GraphResponse graphResponse) {
                                dismissProgressBar();
                                try {

                                    String fbId = user1.getString("id");
                                    String name = user1.optString("name");
                                    String email = user1.optString("email");
                                    JSONObject picture = user1.optJSONObject("picture");
                                    JSONObject pictureData = picture.optJSONObject("data");
                                    //String imageUrl = pictureData.optString("url");
                                    String imageUrl = String.format("http://graph.facebook.com/%s/picture?type=large", fbId);

                                    tvMessage.setText("Facebook Details " +
                                            "\n\n" +
                                            name +
                                            "\n" +
                                            email /*+
                                "\n" +
                                account.getIdToken()*/);


                                } catch (Exception e) {
                                    dismissProgressBar();
                                    e.printStackTrace();
                                }
                            }
                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,first_name,last_name,email,gender,birthday,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (com.facebook.AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                    }
                });

    }


    /*Outlook Login with Firebase*/
    private void onOutlookLoginWithFirebase() {

        OAuthProvider.Builder provider = OAuthProvider.newBuilder("microsoft.com");

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        add("Contacts.Read");
                    }
                };
        provider.setScopes(scopes);

        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth ID token can also be retrieved:
                                    // authResult.getCredential().getIdToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.

            mAuth.startActivityForSignInWithProvider(/* activity= */ this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can also be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // The OAuth ID token can also be retrieved:
                                    // authResult.getCredential().getIdToken().
                                    if (authResult.getAdditionalUserInfo() != null &&
                                            authResult.getAdditionalUserInfo().getProfile() != null) {
                                        for (int i = 0; i < authResult.getAdditionalUserInfo().getProfile().size(); i++) {
                                            authResult.getUser();
                                        }
                                        if (mAuth.getCurrentUser() != null) {
                                            //String userId = firebaseAuth.getCurrentUser().getProviderData().get(0).getUid();

                                            authResult.getUser().getProviderData().get(1).getUid();


                                            //getYahooProfile("");

                                        }
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        }


    }


    /*Outlook Login without Firebase*/
    private void setUpOutlookWithoutFirebase() {

        // Creates a PublicClientApplication object with res/raw/auth_config_single_account.json
        PublicClientApplication.createSingleAccountPublicClientApplication(this,
                R.raw.auth_config_single_account,
                new IPublicClientApplication.ISingleAccountApplicationCreatedListener() {
                    @Override
                    public void onCreated(ISingleAccountPublicClientApplication application) {
                        /**
                         * This test app assumes that the app is only going to support one account.
                         * This requires "account_mode" : "SINGLE" in the config json file.
                         **/
                        mSingleAccountApp = application;
                        loadAccount();
                    }

                    @Override
                    public void onError(MsalException exception) {

                    }
                });

    }

    private void loadAccount() {
        if (mSingleAccountApp == null) {
            return;
        }

        mSingleAccountApp.getCurrentAccountAsync(new ISingleAccountPublicClientApplication.CurrentAccountCallback() {
            @Override
            public void onAccountLoaded(@Nullable IAccount activeAccount) {
                // You can use the account data to update your UI or your app database.
                mAccount = activeAccount;
                //updateUI();
            }

            @Override
            public void onAccountChanged(@Nullable IAccount priorAccount, @Nullable IAccount currentAccount) {
                if (currentAccount == null) {
                    // Perform a cleanup task as the signed-in account changed.
                    //showToastOnSignOut();
                }
            }

            @Override
            public void onError(@NonNull MsalException exception) {
                //displayError(exception);
            }
        });
    }

    private void onOutlookLoginWithoutFirebase() {

        mSingleAccountApp.signIn(this, null, new String[]{"email", "contacts.read"}, getAuthInteractiveCallback());

    }

    private AuthenticationCallback getAuthInteractiveCallback() {
        return new AuthenticationCallback() {

            @Override
            public void onSuccess(IAuthenticationResult authenticationResult) {
                /* Successfully got a token, use it to call a protected resource - MSGraph */
                Log.d(TAG, "Successfully authenticated");
                Log.d(TAG, "ID Token: " + authenticationResult.getAccount().getClaims().get("id_token"));

                /* Update account */
                mAccount = authenticationResult.getAccount();
                //updateUI();

                /* call graph */
                callGraphAPI(authenticationResult);
            }

            @Override
            public void onError(MsalException exception) {
                /* Failed to acquireToken */
                Log.d(TAG, "Authentication failed: " + exception.toString());
                //displayError(exception);

                if (exception instanceof MsalClientException) {
                    /* Exception inside MSAL, more info inside MsalError.java */
                } else if (exception instanceof MsalServiceException) {
                    /* Exception when communicating with the STS, likely config issue */
                }
            }

            @Override
            public void onCancel() {
                /* User canceled the authentication */
                Log.d(TAG, "User cancelled login.");
            }
        };
    }

    private void callGraphAPI(final IAuthenticationResult authenticationResult) {


        MSGraphRequestWrapper.callGraphAPIUsingVolley(
                this,
                "",
                authenticationResult.getAccessToken(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        /* Successfully called graph, process data and send to UI */
                        Log.d(TAG, "Response: " + response.toString());
                        tvMessage.setText(response.toString());
                    }
                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error: " + error.toString());
                        //displayError(error);
                    }
                });
    }



    /*Yahoo Login with Firebase*/

    private void onYahooLoginWithFirebase() {
        OAuthProvider.Builder provider = OAuthProvider.newBuilder("yahoo.com");

        // Prompt user to re-authenticate to Yahoo.
        provider.addCustomParameter("prompt", "login");

        // Localize to French.
        provider.addCustomParameter("language", "en");

        List<String> scopes =
                new ArrayList<String>() {
                    {
                        // This must be preconfigured in the app's API permissions.
                        add("sdct-r");
                        add("profile");
                        add("email");
                    }
                };
        provider.setScopes(scopes);


        Task<AuthResult> pendingResultTask = mAuth.getPendingAuthResult();
        if (pendingResultTask != null) {
            // There's something already here! Finish the sign-in for your user.
            pendingResultTask
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // Yahoo OAuth ID token can be retrieved:
                                    // authResult.getCredential().getIdToken().
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        } else {
            // There's no pending result so you need to start the sign-in flow.
            // See below.

            mAuth
                    .startActivityForSignInWithProvider(this, provider.build())
                    .addOnSuccessListener(
                            new OnSuccessListener<AuthResult>() {
                                @Override
                                public void onSuccess(AuthResult authResult) {
                                    // User is signed in.
                                    // IdP data available in
                                    // authResult.getAdditionalUserInfo().getProfile().
                                    // The OAuth access token can be retrieved:
                                    // authResult.getCredential().getAccessToken().
                                    // Yahoo OAuth ID token can also be retrieved:
                                    // authResult.getCredential().getIdToken().
                                    if (authResult.getAdditionalUserInfo() != null &&
                                            authResult.getAdditionalUserInfo().getProfile() != null) {

                                        tvMessage.setText("Yahoo Details " +
                                                "\n\n" +
                                                authResult.getAdditionalUserInfo().getProfile().get("name").toString() +
                                                "\n" +
                                                authResult.getAdditionalUserInfo().getProfile().get("email").toString());

                                        //getYahooProfile("");
                                    }
                                }
                            })
                    .addOnFailureListener(
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure.
                                }
                            });
        }
    }




    /*Yahoo login without firebase*/

    private void onYahooLoginWithoutFirebase() {

        String url = Constants.YAHOO_OAUTH_REQUEST_AUTH_URL +
                "?" +
                "client_id=" + Constants.CONSUMER_KEY_YAHOO_WITHOUT_FIREBASE +
                "&redirect_uri=" + Constants.YAHOO_REDIRECT_URI +
                "&response_type=code" +
                "&state=" + Constants.YAHOO_STATE +
                "&language=en-us";
        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra("type", "yahoo");
        browserIntent.putExtra("url", url);
        browserIntent.putExtra("state", Constants.YAHOO_STATE);
        startActivityForResult(browserIntent, YAHOO_LOGIN_REQUEST);

    }

    private void getYahooProfile(String accessToken) {

        startProgressBar();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> loginCall = apiInterface.getYahooProfile(Constants.YAHOO_OPENID_USERINFO_URL,
                "Bearer " + accessToken);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressBar();
                if (response.body() != null) {
                    String text = Util.convertStreamToString(response.body().byteStream());
                    Log.d("Result", "text = " + text);
                    YahooProfileResponse yahooProfileResponse = new Gson().fromJson(text,
                            YahooProfileResponse.class);

                    String name = yahooProfileResponse.getName();
                    String email = yahooProfileResponse.getEmail();

                    tvMessage.setText("Yahoo Details " +
                            "\n\n" +
                            name +
                            "\n" +
                            email /*+
                                "\n" +
                    account.getIdToken()*/);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                dismissProgressBar();
            }
        });
    }

    private void getYahooContacts(String authToken, String userId) {

        startProgressBar();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> loginCall = apiInterface.getYahooContacts(Constants.YAHOO_CONTACTS_URL,
                "Bearer " + authToken, userId, "json");
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressBar();
                if (response.body() != null) {
                    String text = Util.convertStreamToString(response.body().byteStream());
                    Log.d("Result", "text = " + text);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                dismissProgressBar();
            }
        });
    }


    /*LinkedIn login without firebase*/

    private void onLinkedInLoginWithoutFirebase() {

        String url = Constants.LINKEDIN_AUTHORIZATION_URL + "" +
                "?" +
                "client_id=" + Constants.CLIENT_ID_LINKEDIN +
                "&redirect_uri=" + Constants.LINKEDIN_REDIRECT_URI +
                "&response_type=code" +
                "&state=" + Constants.LINKEDIN_STATE +
                "&scope=" + Constants.LINKEDIN_SCOPES;

        Intent browserIntent = new Intent(this, WebViewActivity.class);
        browserIntent.putExtra("type", "linkedin");
        browserIntent.putExtra("url", url);
        browserIntent.putExtra("state", Constants.LINKEDIN_STATE);
        startActivityForResult(browserIntent, LINKEDIN_LOGIN_REQUEST);

    }

    private void getLinkedInProfile(String accessToken) {
        startProgressBar();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> apiCall = apiInterface.getLinkedInProfile(Constants.LINKEDIN_GET_PROFILE_URL,
                "Bearer " + accessToken);
        apiCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dismissProgressBar();
                if (response.body() != null) {

                    String text = Util.convertStreamToString(response.body().byteStream());
                    Log.d("Result", "text = " + text);
                    LinkedInProfileResponse linkedInProfileResponse = new Gson().fromJson(text,
                            LinkedInProfileResponse.class);

                    String name = linkedInProfileResponse.getLocalizedFirstName();
                    String lastName = linkedInProfileResponse.getLocalizedLastName();

                    tvMessage.setText("LinkedIn Details " +
                            "\n\n" +
                            name + " " + lastName /*+
                                                    "\n" +
                                                    email +
                                "\n" +
                                account.getIdToken()*/);

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                dismissProgressBar();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {

            case GOOGLE_SIGN_IN_REQUEST:

                if (resultCode == RESULT_OK) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);


                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);

                        String googleId = account.getId();
                        String email = account.getEmail();
                        String name = account.getDisplayName();

                        String imageUrl = "";

                        if (account.getPhotoUrl() != null) {
                            imageUrl = account.getPhotoUrl().toString();
                        }

                        tvMessage.setText("Google Details " +
                                "\n\n" +
                                account.getDisplayName() +
                                "\n" +
                                account.getEmail() /*+
                                "\n" +
                                account.getIdToken()*/);


                        dismissProgressBar();


                    } catch (ApiException e) {
                        dismissProgressBar();
                        e.printStackTrace();
                    }
                }

                break;

            case LINKEDIN_LOGIN_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    if (data != null && data.hasExtra("state_match")) {
                        if (data.getBooleanExtra("state_match", false)) {
                            if (data.hasExtra("access_token")) {
                                getLinkedInProfile(data.getStringExtra("access_token"));

                            } else if (data.hasExtra("error_description")) {
                                Toast.makeText(this, data.getStringExtra("error_description"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                break;

            case YAHOO_LOGIN_REQUEST:

                if (resultCode == Activity.RESULT_OK) {

                    if (data != null && data.hasExtra("state_match")) {
                        if (data.getBooleanExtra("state_match", false)) {
                            if (data.hasExtra("access_token")) {
                                getYahooProfile(data.getStringExtra("access_token"));

                            } else if (data.hasExtra("error_description")) {
                                Toast.makeText(this, data.getStringExtra("error_description"), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Some error occurred", Toast.LENGTH_SHORT).show();
                        }
                    }

                }

                break;

        }


    }


    private void startProgressBar() {
        progress = new ProgressDialog(this);
        progress.setTitle(R.string.app_name);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
    }

    private void dismissProgressBar() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}