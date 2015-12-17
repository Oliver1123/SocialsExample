package com.example.oliver.socialsexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.oliver.socialsexample.fragments.LoginFragment;
import com.example.oliver.socialsexample.fragments.UserInfoFragment;
import com.example.oliver.socialsexample.interfaces.SocialsLoginListener;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements SocialsLoginListener{


    private FragmentTransaction fTrans;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("tag", "MainActivity onCreate");

        showLoginFragment();

        facebookInitialize();
        twitterInitialize();
        googleInitialize();
    }

    private void googleInitialize() {

    }

    private void twitterInitialize() {
        TwitterAuthConfig authConfig = new TwitterAuthConfig(getString(R.string.twitter_api_key), getString(R.string.twitter_api_secret));
        Fabric.with(this, new Twitter(authConfig));
        Log.d("tag", "MainActivity twitterInitialize session: " + Twitter.getSessionManager().getActiveSession());
        if (Twitter.getSessionManager().getActiveSession() != null) {
            showUserInfoFragment(Constants.TWITTER_ID);
        }
    }

    private void facebookInitialize() {
        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                Log.d("tag", "MainActivity facebook sdk initialize");
                if (AccessToken.getCurrentAccessToken() != null) {
                    showUserInfoFragment(Constants.FACEBOOK_ID);
                }

            }
        });
    }

    public void showLoginFragment() {
        Fragment frag = getSupportFragmentManager().findFragmentByTag("UserInfoFragment");

        fTrans = getSupportFragmentManager().beginTransaction();
        Fragment mainFragment = new LoginFragment();
        if (frag != null) {
            fTrans.replace(R.id.fragment_container, mainFragment, "LoginFragment");
        } else {
            fTrans.add(R.id.fragment_container, mainFragment, "LoginFragment");
        }
        fTrans.commit();
    }


    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    private void showUserInfoFragment(int _socialID) {
        Bundle args = new Bundle();
        args.putInt(Constants.ARG_SOCIAL_ID, _socialID);

        Fragment userInfoFragment = new UserInfoFragment();
        userInfoFragment.setArguments(args);

        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.replace(R.id.fragment_container, userInfoFragment, "UserInfoFragment");
        fTrans.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();

        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "MainActivity onResult request: " + requestCode + " result: " + resultCode + " data: " + data.getExtras());
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("LoginFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onAccessSuccess(int id) {
        Log.d("tag", "MainActivity onAccessSuccess id: " + id);
        showUserInfoFragment(id);
    }

    public GoogleApiClient getGoogleApiClient() {
        if (mGoogleApiClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestProfile()
                    .build();

             mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult _connectionResult) {
                            Log.d("tag", "Google onConnection Failed");
                        }
                    }/* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .build();
        }
        return mGoogleApiClient;
    }
}
