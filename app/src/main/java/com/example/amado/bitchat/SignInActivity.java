package com.example.amado.bitchat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;


public class SignInActivity extends ActionBarActivity {
    private static  final String TAG = "SignInActivity";
    private EditText mUserNumber;
    private EditText mPassword;
    private EditText mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        TelephonyManager telephonyManager = (TelephonyManager)this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);

        mUserNumber = (EditText)findViewById(R.id.user_number);
        //emulator doesnt have phoneNumber
        //String phoneNumber = telephonyManager.getLine1Number();
        //provide dummi phoneNumber
        String phoneNumber= "15555215554";
        mUserNumber.setText(phoneNumber);

        mPassword = (EditText)findViewById(R.id.user_password);
        mName = (EditText)findViewById(R.id.user_name);

        Button signUpButton = (Button)findViewById(R.id.signup_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mUserNumber.getText().toString();
                String password = mPassword.getText().toString();
                ParseUser user = new ParseUser();
                user.setUsername(phoneNumber);
                user.setPassword(password);

                user.put("name",mName.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            SignInActivity.this.finish();
                        } else {
                            Log.d(TAG, "FAILED");
                        }
                    }
                });
            }
        });

        Button logInButton = (Button)findViewById(R.id.login_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(mUserNumber.getText().toString(),
                        mPassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                   SignInActivity.this.finish();
                                    ContactsActivity.slogged = true;
                                } else {
                                    Log.d(TAG, "FAILED");
                                }
                            }
                        });
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}