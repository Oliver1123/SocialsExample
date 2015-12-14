package com.example.oliver.socialsexample.models;

/**
 * Created by oliver on 11.12.15.
 */
public class UserProfile {
    private String mName;
    private String mEmail;
    private String mBirthDate;

    public UserProfile(){
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

    @Override
    public String toString() {
        return "Name: " + mName +
                "\nEmail: " + mEmail +
                "\nBirth Date: " + mBirthDate;
    }
}
