package com.example.oliver.socialsexample.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oliver on 11.12.15.
 */
public class UserProfile implements Parcelable {
    private String mName;
    private String mEmail;
    private String mBirthDate;
    private String mPictureUrl;

    public UserProfile(){
    }

    public UserProfile(String _name, String _email, String _birthDate, String _pictureUrl) {
        mName = _name;
        mEmail = _email;
        mBirthDate = _birthDate;
        mPictureUrl = _pictureUrl;
    }

    public String getName() {
        return mName;
    }

    public void setName(String _name) {
        mName = _name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String _email) {
        mEmail = _email;
    }

    public String getBirthDate() {
        return mBirthDate;
    }

    public void setBirthDate(String _birthDate) {
        mBirthDate = _birthDate;
    }

    public String getPictureUrl() {
        return mPictureUrl;
    }

    public void setPictureUrl(String _pictureUrl) {
        mPictureUrl = _pictureUrl;
    }

    @Override
    public String toString() {
        return "Name: " + mName +
                "\nEmail: " + mEmail +
                "\nBirth Date: " + mBirthDate +
                "\nPicture: " + mPictureUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mEmail);
        dest.writeString(mBirthDate);
        dest.writeString(mPictureUrl);
    }

    public static final Parcelable.Creator<UserProfile> CREATOR = new Parcelable.Creator<UserProfile>() {

        public UserProfile createFromParcel(Parcel in) {
            String name = in.readString();
            String email = in.readString();
            String birthday = in.readString();
            String pictureUrl = in.readString();
            return new UserProfile(name, email, birthday, pictureUrl);
        }

        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

}
