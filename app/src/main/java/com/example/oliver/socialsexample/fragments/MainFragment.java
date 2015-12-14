package com.example.oliver.socialsexample.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oliver.socialsexample.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment{
    private LoginButton mLoginButton;
    private CallbackManager callbackManager;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        callbackManager = CallbackManager.Factory.create();

        mLoginButton = (LoginButton) rootView.findViewById(R.id.btnFacebookLogin_FM);

        mLoginButton.setFragment(this);
        mLoginButton.setReadPermissions(Arrays.asList("email", "user_birthday"));

        // Callback registration
        mLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d("tag", "MainFragment mLoginButton onSuccess token: " + AccessToken.getCurrentAccessToken().getToken());

            }

            @Override
            public void onCancel() {
                // App code
                Log.d("tag", "MainFragment mLoginButton onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("tag", "MainFragment mLoginButton onError");
            }
        });
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "MainFragment onResult");
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
