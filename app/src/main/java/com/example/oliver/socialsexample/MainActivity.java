package com.example.oliver.socialsexample;



import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fTrans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FacebookSdk.sdkInitialize(getApplicationContext());
        Fragment mainFragment = new MainFragment();
        fTrans = getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.fragment_container, mainFragment, "mainFragment");
        fTrans.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AppEventsLogger.activateApp(this);
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
}
