package com.example.oliver.socialsexample;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    private LoginButton mLoginButton;
    private CallbackManager callbackManager;
    private TextView mUserInfoTextView;

    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        callbackManager = CallbackManager.Factory.create();

        mUserInfoTextView = (TextView) rootView.findViewById(R.id.tvUserInfo_FM);
        mUserInfoTextView.setOnClickListener(this);
        mLoginButton = (LoginButton) rootView.findViewById(R.id.btnFacebookLogin_FM);

        mLoginButton.setFragment(this);

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

    public void setUserInfo(Profile _profile) {
        String userInfo = "First Name: " + _profile.getFirstName() +
                "\nLastName: " + _profile.getLastName() +
                "\nName: " + _profile.getName();
        mUserInfoTextView.setText(userInfo);
    }

    @Override
    public void onClick(View v) {
        setUserInfo(Profile.getCurrentProfile());
    }
}
