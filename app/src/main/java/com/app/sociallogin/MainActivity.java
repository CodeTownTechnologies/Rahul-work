package com.app.sociallogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
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

    private static final String CONSUMER_KEY_YAHOO =
            "dj0yJmk9MlFLc1lKRGRya2huJmQ9WVdrOVdqYzFNMFpTZVdnbWNHbzlNQT09JnM9Y29uc3VtZXJzZWNyZXQmc3Y9MCZ4PTBi";

    private static final String CONSUMER_SECRET_YAHOO =
            "ad5d41fe988c894dfdfdbdf9b7e43105764fd8d6";

    private static final String REDIRECT_URI = "com.app.yahoologin://login";

    private static final int REQUEST = 4;

    private static final int GOOGLE_SIGN_IN_REQUEST = 12;
    private GoogleSignInClient googleSignInClient;
    private String name, email, fbId, googleId, imageUrl, phone;

    private ISingleAccountPublicClientApplication mSingleAccountApp;
    private IAccount mAccount;

    Button btnGoogleLoginWithFirebase;
    Button btnFacebookLoginWithoutFirebase;
    Button btnOutlookLoginWithoutFirebase;
    Button btnOutlookLoginWithFirebase;
    Button btnYahooLoginWithFirebase;
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
        btnYahooLoginWithFirebase = findViewById(R.id.btnYahooLoginWithFirebase);
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

        btnYahooLoginWithFirebase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onYahooLoginWithFirebase();

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

                                    fbId = user1.getString("id");
                                    name = user1.optString("name");
                                    email = user1.optString("email");
                                    JSONObject picture = user1.optJSONObject("picture");
                                    JSONObject pictureData = picture.optJSONObject("data");
                                    //String imageUrl = pictureData.optString("url");
                                    imageUrl = String.format("http://graph.facebook.com/%s/picture?type=large", fbId);

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

                        googleId = account.getId();
                        email = account.getEmail();
                        name = account.getDisplayName();

                        imageUrl = "";

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

        }


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

    private void onOutlookLoginWithoutFirebase(){

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

    private void getYahooProfile(String authToken) {

        progress = new ProgressDialog(this);
        progress.setTitle(R.string.app_name);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> loginCall = apiInterface.getYahooProfile(
                "Bearer " + authToken);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progress.dismiss();
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                progress.dismiss();
            }
        });
    }



    /*Yahoo login without firebase*/

    private void yahooLoginWithoutFirebase() {

        String url = Constants.YAHOO_BASE_URL +
                Constants.YAHOO_OAUTH + "" +
                "request_auth?" +
                "client_id=" + CONSUMER_KEY_YAHOO +
                "&redirect_uri=" + REDIRECT_URI +
                "&response_type=code" +
                "&language=en-us";
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        if (browserIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(browserIntent);
        }

    }

    private void requestYahooAccessToken(String authCode) {

        progress = new ProgressDialog(this);
        progress.setTitle(R.string.app_name);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<YahooAccessTokenResponse> apiCall = apiInterface.getYahooToken(CONSUMER_KEY_YAHOO,
                CONSUMER_SECRET_YAHOO, REDIRECT_URI,
                authCode, "authorization_code");
        apiCall.enqueue(new Callback<YahooAccessTokenResponse>() {
            @Override
            public void onResponse(Call<YahooAccessTokenResponse> call, Response<YahooAccessTokenResponse> response) {
                progress.dismiss();
                if (response.isSuccessful()) {
                    YahooAccessTokenResponse yahooAccessTokenResponse = response.body();

                    if (yahooAccessTokenResponse != null && yahooAccessTokenResponse.getAccessToken() != null) {
                        getYahooContacts(yahooAccessTokenResponse.getAccessToken(),
                                yahooAccessTokenResponse.getXoauthYahooGuid());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<YahooAccessTokenResponse> call, Throwable t) {
                progress.dismiss();
            }
        });

    }

    private void getYahooContacts(String authToken, String userId) {

        progress = new ProgressDialog(this);
        progress.setTitle(R.string.app_name);
        progress.setMessage("Loading...");
        progress.setCancelable(false);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ResponseBody> loginCall = apiInterface.getYahooContacts(
                "Bearer " + authToken, userId, "json");
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progress.dismiss();
                if (response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("onFailure: ", t.getLocalizedMessage());
                progress.dismiss();
            }
        });
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