package com.example.oliver.socialsexample.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliver.socialsexample.Constants;
import com.example.oliver.socialsexample.MainActivity;
import com.example.oliver.socialsexample.R;
import com.example.oliver.socialsexample.interfaces.SocialsLoginListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class LoginFragment extends Fragment{
    private static final int GOOGLE_SIGN_IN_REQUEST = 0;
    private LoginButton mFacebookLoginButton;
    private TwitterLoginButton mTwitterLoginButton;
    private SignInButton mGoogleLoginButton;
    private CallbackManager callbackManager;
    private SocialsLoginListener mLoginListener;

    public LoginFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mLoginListener = (SocialsLoginListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("tag", "LoginFragment onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);

        facebookInit(rootView);

        twitterInit(rootView);

        googleInit(rootView);

        return rootView;
    }

    private void twitterInit(View _rootView) {
        mTwitterLoginButton = (TwitterLoginButton) _rootView.findViewById(R.id.btnTwitterLogin_FL);
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("tag", "LoginFragment twitterInit login success");
                mLoginListener.onAccessSuccess(Constants.TWITTER_ID);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("tag", "LoginFragment twitterInit  login failure", exception);
            }
        });
    }

    private void facebookInit(View _rootView) {
        callbackManager = CallbackManager.Factory.create();

        mFacebookLoginButton = (LoginButton) _rootView.findViewById(R.id.btnFacebookLogin_FL);

        mFacebookLoginButton.setFragment(this);
        mFacebookLoginButton.setReadPermissions(Arrays.asList("email", "user_birthday"));

        // Callback registration
        mFacebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("tag", "LoginFragment mFacebookLoginButton onSuccess token: " + AccessToken.getCurrentAccessToken().getToken());
                mLoginListener.onAccessSuccess(Constants.FACEBOOK_ID);

            }

            @Override
            public void onCancel() {
                Log.d("tag", "LoginFragment mFacebookLoginButton onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("tag", "LoginFragment mFacebookLoginButton onError");
            }
        });
    }

    private void googleInit(View _rootView) {

        mGoogleLoginButton = (SignInButton) _rootView.findViewById(R.id.btnGoogleLogin_FL);
        mGoogleLoginButton.setColorScheme(SignInButton.COLOR_DARK);
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(((MainActivity)getActivity()).getGoogleApiClient());
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "LoginFragment onResult request: " + requestCode + " result: " + resultCode + " data: " + data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            googleSignInResult(result);
        }
    }

    private void googleSignInResult(GoogleSignInResult result) {
        Log.d("tag", "googleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            mLoginListener.onAccessSuccess(Constants.GOOGLE_ID);
            // Signed in successfully, show authenticated UI.
//            GoogleSignInAccount acct = result.getSignInAccount();
//            Log.d("tag", "Granted scopes: " + acct.getGrantedScopes());
//            Log.d("tag", "Google account Name: " + acct.getDisplayName() + ", email: " + acct.getEmail() + ", photo: " + acct.getPhotoUrl());
        } else {
            // Signed out, show unauthenticated UI.
        }
    }

}
