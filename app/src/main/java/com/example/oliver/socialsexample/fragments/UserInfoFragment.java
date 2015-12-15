package com.example.oliver.socialsexample.fragments;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.example.oliver.socialsexample.models.UserProfile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {
    private static final int SHARE_REQUEST  = 999;
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

        return rootView;
    }

    private void initUI(View _rootView) {
        mUserInfoText = (TextView) _rootView.findViewById(R.id.tvUserInfo_FUI);
        mUserPhoto = (ImageView) _rootView.findViewById(R.id.ivUserPhoto_FUI);

        mPhotoPath = (TextView) _rootView.findViewById(R.id.tvPhotoPath_FUI);
        mPostText = (EditText) _rootView.findViewById(R.id.etPostText_FUI);
        mShareButton = (Button) _rootView.findViewById(R.id.btnShare_FUI);

        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mPostText.getText().toString();
                switch (mSocialID) {
                    case Constants.FACEBOOK_ID:
                        facebookShare(message, mSelectedImageUri);
                }
            }
        });
        mChoosePhotoButton = (Button) _rootView.findViewById(R.id.btnChoosePhoto_FUI);
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

        mLogOutButton = (Button) _rootView.findViewById(R.id.btnLogOut_FUI);
        mLogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (mSocialID) {
                    case Constants.FACEBOOK_ID:
                        Log.d("tag", "UserInfoFragment Facebook logout");
                        LoginManager.getInstance().logOut();
                        ((MainActivity) getActivity()).showLoginFragment();
                        break;
                    case Constants.TWITER_ID:
                        Log.d("tag", "UserInfoFragment Twitter logout");
                        Twitter.getSessionManager().clearActiveSession();
                        ((MainActivity) getActivity()).showLoginFragment();
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
                    facebookAccess((UserProfile)args.getParcelable(Constants.ARG_SOCIAL_USER));
                    break;
                case Constants.TWITER_ID:
                    twitterAccess((UserProfile) args.getParcelable(Constants.ARG_SOCIAL_USER));
                    break;
            }
        }
    }

    private void twitterAccess(UserProfile _userProfile) {
        mUserInfoText.setText("Access from twitter \n" + _userProfile);
        Picasso.with(getActivity()).load(_userProfile.getPictureUrl()).into(mUserPhoto);
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

    private void facebookAccess(UserProfile _userProfile) {
        mUserInfoText.setText("Access from facebook \n" + _userProfile);
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
}
