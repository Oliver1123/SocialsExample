package com.example.oliver.socialsexample.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.oliver.socialsexample.R;
import com.example.oliver.socialsexample.models.UserProfile;

import org.w3c.dom.Text;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {
    public static final String ARG_SOCIAL_ID = "social_id";
    public static final String ARG_SOCIAL_USER = "social_user";

    public static final int FACEBOOK_ID = 0;
    public static final int TWITER_ID   = 1;
    public static final int GOOGLE_ID   = 2;

    private TextView mUserInfoText;
    private ImageView mUserPhoto;

    public UserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_user_info, container, false);

        mUserInfoText = (TextView) rootView.findViewById(R.id.tvUserInfo_FUI);
        mUserPhoto = (ImageView) rootView.findViewById(R.id.ivUserPhoto_FUI);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            int socialID = args.getInt(ARG_SOCIAL_ID);
            switch (socialID) {
                case FACEBOOK_ID:
                    facebookAccess((UserProfile)args.getParcelable(ARG_SOCIAL_USER));
                    break;
            }
        }
    }

    private void facebookAccess(UserProfile _userProfile) {
        mUserInfoText.setText("Access from facebook \n" + _userProfile);

    }
}
