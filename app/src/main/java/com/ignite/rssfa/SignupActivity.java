package com.ignite.rssfa;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    EditText mNameText;
    EditText mEmailText;
    EditText mPasswordText;
    Button mSignupButton;
    TextView mLoginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mSignupButton = findViewById(R.id.signup);
        mNameText = findViewById(R.id.name);
        mEmailText = findViewById(R.id.email);
        mPasswordText = findViewById(R.id.password);
        mLoginLink = findViewById(R.id.login);
        mSignupButton.setOnClickListener(v -> signup());

        mLoginLink.setOnClickListener(v -> {
            finish();
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.creating_account));
        progressDialog.show();

        String username = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();
        HttpRequest.createUser(getBaseContext(), username, email, password, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.w(TAG, "Signup succes");
                progressDialog.dismiss();
                onSignupSuccess();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.w(TAG, "Signup failed");
                progressDialog.dismiss();
                onSignupFailed();
            }
        });
    }


    public void onSignupSuccess() {
        mSignupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Utils.longToast(getBaseContext(), "Login failed");
        mSignupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = mNameText.getText().toString();
        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            mNameText.setError(getString(R.string.at_least_3_characters));
            valid = false;
        } else {
            mNameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError(getString(R.string.enter_a_valid_email));
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError(getString(R.string.between_4_and_10_alphanum_char));
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
}