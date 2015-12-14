package com.example.oliver.socialsexample.requests;

import android.os.Bundle;
import android.util.Log;

import com.example.oliver.socialsexample.interfaces.UserProfileCallback;
import com.example.oliver.socialsexample.models.UserProfile;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by oliver on 14.12.15.
 */
public class FacebookRequests {

    public static GraphRequest infoRequest(AccessToken _accessToken, final UserProfileCallback _callback){
        GraphRequest request = GraphRequest.newMeRequest(_accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject _jsonObject, GraphResponse _graphResponse) {
                UserProfile userProfile = null;
                try {
                    String name =  _jsonObject.getString("name");
                    String email = _jsonObject.has("email") ? _jsonObject.getString("email") : "unknown";
                    String birthday = _jsonObject.has("birthday") ? _jsonObject.getString("birthday") : "unknown";
                    String pictureUrl = _jsonObject.getJSONObject("picture").
                            getJSONObject("data").getString("url");
                    userProfile = new UserProfile(name, email, birthday, pictureUrl);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                _callback.onCompleted(userProfile);
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "picture{url},name,birthday,email");
        request.setParameters(parameters);
        return request;
    }



}
