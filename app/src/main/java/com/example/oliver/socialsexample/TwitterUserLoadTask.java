package com.example.oliver.socialsexample;

import android.os.AsyncTask;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by oliver on 16.12.15.
 */
public  class TwitterUserLoadTask extends AsyncTask<Twitter, Void, User> {

    private final Callback<User> mCallback;

    public TwitterUserLoadTask(Callback<User> _callback) {
        mCallback = _callback;
    }

    @Override
    protected User doInBackground(twitter4j.Twitter... params) {
        try {
            return params[0].verifyCredentials();
        } catch (TwitterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(User result) {
        super.onPostExecute(result);
        if (result == null) {
            mCallback.failure(new com.twitter.sdk.android.core.TwitterException("Error while user loading"));
        } else {
            mCallback.success(new Result<User>(result, null));
        }

    }
}