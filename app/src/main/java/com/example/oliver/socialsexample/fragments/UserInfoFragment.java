package com.example.oliver.socialsexample.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliver.socialsexample.Constants;
import com.example.oliver.socialsexample.MainActivity;
import com.example.oliver.socialsexample.R;
import com.example.oliver.socialsexample.interfaces.UserProfileCallback;
import com.example.oliver.socialsexample.models.UserProfile;
import com.example.oliver.socialsexample.requests.FacebookRequests;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {
    private static final int SELECT_PICTURE = 1;
    private Uri mSelectedImageUri;

    private TextView mUserInfoText, mPhotoPath;
    private ImageView mUserPhoto;
    private EditText mPostText;
    private Button mChoosePhotoButton, mShareButton, mLogOutButton;
    private int mSocialID;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);
        Log.d("tag", "UserInfoFragment onCreateView");

        initUI(rootView);
        setUIListeners();
        return rootView;
    }

    private void initUI(View _rootView) {
        mUserInfoText = (TextView) _rootView.findViewById(R.id.tvUserInfo_FUI);
        mUserPhoto = (ImageView) _rootView.findViewById(R.id.ivUserPhoto_FUI);

        mPhotoPath = (TextView) _rootView.findViewById(R.id.tvPhotoPath_FUI);
        mPostText = (EditText) _rootView.findViewById(R.id.etPostText_FUI);
        mShareButton = (Button) _rootView.findViewById(R.id.btnShare_FUI);

        mChoosePhotoButton = (Button) _rootView.findViewById(R.id.btnChoosePhoto_FUI);

        mLogOutButton = (Button) _rootView.findViewById(R.id.btnLogOut_FUI);
    }

    private void setUIListeners() {
        mChoosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Picture"), SELECT_PICTURE);
            }
        });
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mSocialID) {
                    case Constants.FACEBOOK_ID:
                        Log.d("tag", "UserInfoFragment Facebook logout");
                        LoginManager.getInstance().logOut();
                        ((MainActivity) getActivity()).showLoginFragment();
                        break;
                    case Constants.TWITTER_ID:
                        Log.d("tag", "UserInfoFragment Twitter logout");
                        Twitter.getSessionManager().clearActiveSession();
                        ((MainActivity) getActivity()).showLoginFragment();
                        break;
                }
            }
        });
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mPostText.getText().toString();
                switch (mSocialID) {
                    case Constants.FACEBOOK_ID:
                        facebookShare(message, mSelectedImageUri);
                        break;
                    case Constants.TWITTER_ID:
                        twitterShare(message, mSelectedImageUri);
                        break;
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mSocialID = args.getInt(Constants.ARG_SOCIAL_ID);
            switch (mSocialID) {
                case Constants.FACEBOOK_ID:
                    loadFacebookUser();
                    break;
                case Constants.TWITTER_ID:
                    loadTwitterUser();
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag", "UserInfoFragment onActivityResult data: " + data.getExtras());
        switch (requestCode) {
            case SELECT_PICTURE:
                if (resultCode == Activity.RESULT_OK) {
                    mSelectedImageUri = data.getData();
                    mPhotoPath.setText(mSelectedImageUri.getLastPathSegment());
                    mPhotoPath.setVisibility(View.VISIBLE);
                    Log.d("tag", "onActivityResult pickImage data:" + mSelectedImageUri);
                }
                break;
        }
    }

    private void showUserInfo(int _socialID, UserProfile _userProfile) {
        String userInfo = "";
        switch (_socialID) {
            case Constants.FACEBOOK_ID:
                userInfo = "Access from Facebook\n";
                break;
            case Constants.TWITTER_ID:
                userInfo = "Access from Twitter\n";
                break;
            case Constants.GOOGLE_ID:
                userInfo = "Access from Google+\n";
                break;
        }
        userInfo += _userProfile;
        mUserInfoText.setText(userInfo);
        Picasso.with(getActivity()).load(_userProfile.getPictureUrl()).into(mUserPhoto);
    }

    private void facebookShare(String message, Uri photo){
        if (photo != null) {
            SharePhoto sharePhoto = new SharePhoto.Builder()
                    .setImageUrl(photo)
                    .build();
            SharePhotoContent content = new SharePhotoContent.Builder()
                    .addPhoto(sharePhoto)
                    .build();

            ShareDialog.show(getActivity(), content);
        }
        //////////////////////////////
//                FacebookRequests.postRequest(AccessToken.getCurrentAccessToken(), message, new GraphRequest.Callback() {
//                    @Override
//                    public void onCompleted(GraphResponse _graphResponse) {
//                        Log.d("tag", "postRequest onCompleted response: " + _graphResponse);
//                    }
//                })
//                        .executeAsync();
    }

    private void twitterShare(String _message, Uri _selectedImageUri) {
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                .text(_message);
        if (_selectedImageUri != null) {
            builder.image(_selectedImageUri);
        }
        builder.show();
    }

    public void loadFacebookUser() {
        FacebookRequests.infoRequest(AccessToken.getCurrentAccessToken(), new UserProfileCallback() {
            @Override
            public void onCompleted(UserProfile _profile) {
                showUserInfo(Constants.FACEBOOK_ID, _profile);
            }
        }).executeAsync();
    }

    private void loadTwitterUser() {
        String name = Twitter.getSessionManager().getActiveSession().getUserName();
        Log.d("tag", "Name: " + name);

//        TwitterAuthClient authClient = new TwitterAuthClient();
//
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//
//        authClient.requestEmail(Twitter.getSessionManager().getActiveSession(), new Callback<String>() {
//            @Override
//            public void success(Result<String> result) {
//                Log.d("tag", "getEmail " + result.data);
//            }
//
//            @Override
//            public void failure(TwitterException exception) {
//                Log.d("tag", "getEmail failure ", exception);
//            }
//        });
//        showUserInfo(Constants.TWITTER_ID, new UserProfile(name, "email", "date", null));

        twitter4j();
    }
    public void twitter4j() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(getString(R.string.twitter_api_key))
                .setOAuthConsumerSecret(getString(R.string.twitter_api_secret))
                .setOAuthAccessToken(Twitter.getSessionManager().getActiveSession().getAuthToken().token)
                .setOAuthAccessTokenSecret(Twitter.getSessionManager().getActiveSession().getAuthToken().secret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        final twitter4j.Twitter twitter = tf.getInstance();
        new TwitterUserLoader().execute(twitter);
    }

    class TwitterUserLoader extends AsyncTask<twitter4j.Twitter, Void, UserProfile> {
        @Override
        protected UserProfile doInBackground(twitter4j.Twitter... params) {
            try {
                User user = params[0].verifyCredentials();
                UserProfile profile = new UserProfile();
                profile.setPictureUrl(user.getBiggerProfileImageURL());
                profile.setName(user.getName() + "\nScreen name: @" + user.getScreenName());
                return  profile;
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(UserProfile result) {
            super.onPostExecute(result);
            showUserInfo(Constants.TWITTER_ID, result);
        }
    }
}
