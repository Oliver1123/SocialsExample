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
import com.example.oliver.socialsexample.R;
import com.example.oliver.socialsexample.interfaces.SocialsLoginListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
    private LoginButton mFacebookLoginButton;
    private TwitterLoginButton mTwitterLoginButton;
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

        return rootView;
    }

    private void twitterInit(View _rootView) {
        mTwitterLoginButton = (TwitterLoginButton) _rootView.findViewById(R.id.btnTwitterLogin_FL);
        mTwitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d("tag", "LoginFragment twitterInit login success");
                mLoginListener.onAccessSuccess(Constants.TWITER_ID);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "LoginFragment onResult request: " + requestCode + " result: " + resultCode + " data: " + data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        mTwitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
