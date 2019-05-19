package com.ignite.rssfa;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import cz.msebera.android.httpclient.Header;

public class MyAccountFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private String mUsername = "";
    private String mPassword = "";
    private GoogleSignInClient mGoogleSignInClient;
    private Button mSigninButton;
    private Button mSigninWithGoogleButton;
    private LoginButton mSigninWithFacebookButton;
    private Button mSingOutButton;
    private TextView mEmail;
    private TextView mEmailHolder;
    private SessionManager mSession;
    private Context mContext;
    private CallbackManager mCallbackManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        mCallbackManager = CallbackManager.Factory.create();

        EditText usernameEditText = view.findViewById(R.id.username);
        EditText passwordEditText = view.findViewById(R.id.password);
        mContext = getActivity().getApplicationContext();
        mSession = new SessionManager(getActivity().getApplicationContext());
        mSigninButton = view.findViewById(R.id.signinButton);
        mSigninWithGoogleButton = view.findViewById(R.id.signinWithGoogleButton);
        mSigninWithFacebookButton = view.findViewById(R.id.signinWithFacebookButton);
        mSingOutButton = view.findViewById(R.id.signoutButton);
        mEmail = view.findViewById(R.id.email);
        mEmailHolder = view.findViewById(R.id.emailHolder);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity().getApplicationContext(), gso);

        mSigninButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                mUsername = usernameEditText.getText().toString();
                mPassword = passwordEditText.getText().toString();
                if (!mUsername.equals("") && !mPassword.equals("")) {
                    HttpRequest.login(mUsername, mPassword, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.i("STATUS CODE", String.valueOf(statusCode));
                            try {
                                String token = new JSONObject(new String(responseBody)).getString("token");
                                mSession.createLoginSession(mUsername, token);
                                updateUI("", true);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Utils.longToast(mContext, "Authentification failed! Wrong username or password!");
                        }
                    });
                } else {
                    Utils.longToast(mContext, "Username or Password shouldn't be empty!");
                }
            }
        });

        mSigninWithGoogleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });


        mSigninWithFacebookButton.setPermissions(Arrays.asList("public_profile", "email"));
        mSigninWithFacebookButton.setFragment(this);

        // Callback registration
        mSigninWithFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                Log.v("LoginActivity", response.toString());
                                try {
                                    String email = object.getString("email");
                                    String name = object.getString("name");
                                    handleSignInResult(name, email, token);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {
                Log.i("test", "error");
            }
        });


        mSigninWithFacebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile"));
            }
        });

        mSingOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSession.isLoggedIn()) {
                    mSession.logoutUser();
                }
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI("", false);
                    }
                });
            }
        });

        return view;
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("request code", String.valueOf(requestCode));

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            GoogleSignInAccount account = null;
            try {
                account = task.getResult(ApiException.class);
            } catch (ApiException e) {
                e.printStackTrace();
            }
            String idToken = account.getIdToken();
            String displayName = account.getDisplayName();
            String email = account.getEmail();
            handleSignInResult(displayName, idToken, email);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(String name, String email, String token) {
        HttpRequest.login(name, token.substring(0, 15), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String token = new JSONObject(new String(responseBody)).getString("token");
                    mSession.createLoginSession(name, token);
                    updateUI(email, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                HttpRequest.createUser(getActivity().getApplicationContext(), name, email, token.substring(0, 15), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("DG", "creating google user");
                        updateUI(email, true);
                        mSession.createLoginSession(name, token);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.w("Google user server auth", "signInResult:failed code=" + statusCode);
                        try {

                            String str = new String(responseBody, "UTF-8");
                            Log.w("Body", str);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Utils.shortToast(mContext, "Server Error: Couldn't add user");
                        updateUI("", false);
                    }
                });

            }
        });
    }

    private void updateUI(String email, boolean isLoggedIn) {
        if (isLoggedIn) {
            mSingOutButton.setVisibility(View.VISIBLE);
            mEmailHolder.setVisibility(View.VISIBLE);
            if (!email.equals("")) {
                mEmail.setText(email);
            } else {
                mEmail.setText(mSession.getUserDetails().get("UserName"));
            }
            mEmail.setVisibility(View.VISIBLE);
            mSigninWithGoogleButton.setVisibility(View.GONE);
            mSigninWithFacebookButton.setVisibility(View.GONE);
            getView().findViewById(R.id.tableLayout).setVisibility(View.GONE);
        } else {
            mSingOutButton.setVisibility(View.GONE);
            mEmailHolder.setVisibility(View.GONE);
            mEmail.setVisibility(View.GONE);
            mSigninWithGoogleButton.setVisibility(View.VISIBLE);
            mSigninWithFacebookButton.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.tableLayout).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getActivity().getApplicationContext());
        mSession = new SessionManager(getActivity().getApplicationContext());
        if (account != null) {
            updateUI(account.getEmail(), true);
        } else if (mSession.isLoggedIn()) {
//            updateUI(mSession.getUserDetails().get("userName"), true);
        } else {
            if (mSession.isLoggedIn()) {
                updateUI("", true);
            } else {
                updateUI("", false);
            }
        }
    }
}
