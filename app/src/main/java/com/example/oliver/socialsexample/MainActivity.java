package com.example.oliver.socialsexample;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.oliver.socialsexample.fragments.MainFragment;
import com.example.oliver.socialsexample.fragments.UserInfoFragment;
import com.example.oliver.socialsexample.interfaces.UserProfileCallback;
import com.example.oliver.socialsexample.models.UserProfile;
import com.example.oliver.socialsexample.requests.FacebookRequests;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fTrans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("tag", "MainActivity onCreate");

        FacebookSdk.sdkInitialize(getApplicationContext(), new FacebookSdk.InitializeCallback() {
            @Override
            public void onInitialized() {
                Log.d("tag", "MainActivity onCreate sdk initialized");
                if (isSessionOpenFacebook()) {

                    GraphRequest infoRequest = FacebookRequests.infoRequest(AccessToken.getCurrentAccessToken(), new UserProfileCallback() {
                        @Override
                        public void onCompleted(UserProfile _profile) {
                            showUserInfo(UserInfoFragment.FACEBOOK_ID, _profile);
                        }
                    });
                    infoRequest.executeAsync();
                }

            }
        });

        Fragment mainFragment = new MainFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment_container, mainFragment, "MainFragment");
        fTrans.commit();

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
    }

    private void showUserInfo(int _id, UserProfile _profile) {
        Bundle args = new Bundle();
        args.putInt(UserInfoFragment.ARG_SOCIAL_ID, _id);
        args.putParcelable(UserInfoFragment.ARG_SOCIAL_USER, _profile);

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
        Log.d("tag", "MainActivity onResult");
    }

    public boolean isSessionOpenFacebook() {
        return (AccessToken.getCurrentAccessToken() != null);
    }
}
