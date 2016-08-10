package com.example.chung.rest2example;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private static final String TAG = "MainActivity";
    public final static String EXTRA_MESSAGE = "com.example.chung.restexample.BANDS";
    private ArrayList<String> bands = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        //Facebook Authentication
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "user_likes"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("GraphResponse", "-------------" + response.toString());
                                //Get Facebook Music Likes
                                try {
                                    JSONArray jsonArray = response.getJSONObject().getJSONObject("music").getJSONArray("data");
                                    for (int i = 0, size = jsonArray.length(); i < size; i++) {
                                        JSONObject objectInArray = jsonArray.getJSONObject(i);
                                        bands.add(objectInArray.getString("name"));
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "unexpected JSON exception", e);
                                }
                                // Compare Facebook music likes to SoundCloud playlist
                                processPlaylist(bands);
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,music");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Login attempt canceled.",Toast.LENGTH_SHORT);
            }

            @Override
            public void onError(FacebookException e) {
                Toast.makeText(getApplicationContext(), "Login attempt failed.", Toast.LENGTH_SHORT);
            }
        });

        //bands.add("U2");
        //bands.add("Pink Floyd");
        //bands.add("RadioHead");
        //processPlaylist(bands);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void processPlaylist(ArrayList<String> bands) {
        Intent intent = new Intent(this, SoundCloudActivity.class);
        intent.putStringArrayListExtra(EXTRA_MESSAGE, bands);
        startActivity(intent);
    }

}
