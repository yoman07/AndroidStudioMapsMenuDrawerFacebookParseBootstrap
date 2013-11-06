package com.zdobywacz;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Settings;
import com.facebook.model.GraphUser;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

public class NonLoggedActivity extends ActionBarActivity {
    public Dialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        printFacebookHash();
        Log.i("MainActivity", "test logowania");
        Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
        Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);

        Request request = Request.newGraphPathRequest(null, "/4", new Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                if(response.getError() != null) {
                    Log.i("MainActivity", String.format("Error making request: %s", response.getError()));
                } else {
                    GraphUser user = response.getGraphObjectAs(GraphUser.class);
                    Log.i("MainActivity", String.format("Name: %s", user.getName()));
                }
            }
        });
        request.executeAsync();

        Parse.initialize(this, getResources().getString(R.string.PARSE_APPLICATION_ID), getResources().getString(R.string.PARSE_CLIENT_KEY));

        ParseFacebookUtils.initialize(getResources().getString(R.string.app_id));

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_logged);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new NonLoggedChooseFragment())
                    .commit();
        }


        // Check if there is a currently logged in user
        // and they are linked to a Facebook account.
        ParseUser currentUser = ParseUser.getCurrentUser();
        if ((currentUser != null) && ParseFacebookUtils.isLinked(currentUser)) {
            // Go to the user info activity
            Log.d("Log in",
                    "User currently logged.");
            showUserLoggedActivity();
        }


    }

    private void printFacebookHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.zdobywacz",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("printFacebookHash:", e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Log.e("printFacebookHash:", e.getMessage());
        }
    }

    public void onLoginButtonClicked() {


        NonLoggedActivity.this.progressDialog = ProgressDialog.show(
                NonLoggedActivity.this, "", "Logging in...", true);

        List<String> permissions = Arrays.asList("basic_info", "user_about_me",
                "user_relationships", "user_birthday", "user_location");


        ParseFacebookUtils.logIn(permissions, NonLoggedActivity.this, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                NonLoggedActivity.this.progressDialog.dismiss();


                if (user == null) {
                    Log.d("Log in",
                            "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("Log in",
                            "User signed up and logged in through Facebook!");
                    NonLoggedActivity.this.showUserLoggedActivity();
                } else {
                    Log.d("Log in",
                            "User logged in through Facebook!");
                    NonLoggedActivity.this.showUserLoggedActivity();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.finishAuthentication(requestCode, resultCode, data);
    }

    public void showUserLoggedActivity() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }





}
