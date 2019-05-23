package com.ignite.rssfa;

import android.app.ProgressDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.ignite.rssfa.db.AlreadyReadViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;

public class MyAccountFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    private String mUsername = "";
    private String mPassword = "";
    private GoogleSignInClient mGoogleSignInClient;
    private Button mSigninButton;
    private SignInButton mSigninWithGoogleButton;
    private LoginButton mSigninWithFacebookButton;
    private Button mSingOutButton;
    private TextView mEmail;
    private TextView mEmailHolder;
    private TextView mUsernameLogged;
    private TextView mUsernameHolder;
    private SessionManager mSession;
    private Context mContext;
    private CallbackManager mCallbackManager;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private TextView mReadCount;
    private TextView mReadCountHolder;
    private AlreadyReadViewModel mAlreadyReadViewModel;
    private TextView mSignup;
    private static final int REQUEST_SIGNUP = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        mContext = inflater.getContext();

        mCallbackManager = CallbackManager.Factory.create();

        usernameEditText = view.findViewById(R.id.username);
        passwordEditText = view.findViewById(R.id.password);
        mSession = new SessionManager(mContext);
        mSigninButton = view.findViewById(R.id.signinButton);
        mSigninWithGoogleButton = view.findViewById(R.id.signinWithGoogleButton);
        mSigninWithFacebookButton = view.findViewById(R.id.signinWithFacebookButton);
        mSingOutButton = view.findViewById(R.id.signoutButton);
        mEmail = view.findViewById(R.id.email);
        mEmailHolder = view.findViewById(R.id.emailHolder);
        mUsernameLogged = view.findViewById(R.id.usernameLogged);
        mUsernameHolder = view.findViewById(R.id.usernameHolder);
        mReadCount = view.findViewById(R.id.readCount);
        mReadCountHolder = view.findViewById(R.id.readCountHolder);
        mAlreadyReadViewModel = ViewModelProviders.of(this).get(AlreadyReadViewModel.class);
        mSignup = view.findViewById(R.id.signup);

        mReadCount.setText(String.valueOf(mAlreadyReadViewModel.count()));
        usernameEditText.addTextChangedListener(watcher);
        passwordEditText.addTextChangedListener(watcher);
        mSigninButton.setEnabled(false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(mContext, gso);

        mSigninButton.setOnClickListener(view12 -> {
            Utils.hideSoftKeyBoard(Objects.requireNonNull(getActivity()));
            final ProgressDialog progressDialog = new ProgressDialog(mContext,
                    R.style.MyDialogTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage(getString(R.string.authentificating));
            progressDialog.show();

            mUsername = usernameEditText.getText().toString();
            mPassword = passwordEditText.getText().toString();
            new android.os.Handler().postDelayed(
                    () -> {
                        HttpRequest.login(mUsername, mPassword, new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Log.i("STATUS CODE", String.valueOf(statusCode));
                                try {
                                    String token = new JSONObject(new String(responseBody)).getString("token");
                                    mSession.createLoginSession(mUsername, token);
                                    updateUI("", mUsername, true);
                                    progressDialog.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                progressDialog.dismiss();
                                Utils.longToast(mContext, "Authentification failed! Wrong username or password!");
                            }
                        });
                    }, 3000);
        });

        mSigninWithGoogleButton.setOnClickListener(view1 -> signInWithGoogle());


        mSigninWithFacebookButton.setPermissions(Arrays.asList("public_profile", "email"));
        mSigninWithFacebookButton.setFragment(this);

        mSigninWithFacebookButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                String token = loginResult.getAccessToken().getToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            try {
                                String email = object.getString("email");
                                String name = object.getString("name");
                                handleSignInResult(name, email, token);
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                Log.i("FAE", exception.toString());
            }
        });


        mSigninWithFacebookButton.setOnClickListener(v -> LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile")));

        mSingOutButton.setOnClickListener(view13 -> {
            if (mSession.isLoggedIn()) {
                mSession.logoutUser();
            }
            mGoogleSignInClient.signOut().addOnCompleteListener(task -> updateUI("", "", false));
        });

        mSignup.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, SignupActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        });

        return view;
    }

    private final TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (usernameEditText.getText().toString().length() == 0 || passwordEditText.getText().toString().length() == 0) {
                mSigninButton.setEnabled(false);
            } else {
                mSigninButton.setEnabled(true);
            }
        }
    };

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
            handleSignInResult(displayName, email, idToken);
        } else {
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(String name, String email, String token) {
        final ProgressDialog progressDialog = new ProgressDialog(mContext,
                R.style.MyDialogTheme);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getString(R.string.authentificating));
        progressDialog.show();
        HttpRequest.login(name, token.substring(0, 15), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String token = new JSONObject(new String(responseBody)).getString("token");
                    mSession.createLoginSession(name, token);
                    updateUI(email, name, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                HttpRequest.createUser(mContext, name, email, token.substring(0, 15), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.i("DG", "creating google user");
                        updateUI(email, name, true);
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
                        updateUI("", "", false);
                        progressDialog.dismiss();
                    }
                });
            }
        });
    }

    private void updateUI(String email, String username, boolean isLoggedIn) {
        if (Utils.isNetworkAvailable(mContext) && isLoggedIn) {
            mUsernameLogged.setVisibility(View.VISIBLE);
            mSingOutButton.setVisibility(View.VISIBLE);
            mSigninButton.setVisibility(View.GONE);
            mEmailHolder.setVisibility(View.VISIBLE);
            mUsernameHolder.setVisibility(View.VISIBLE);
            mReadCountHolder.setVisibility(View.VISIBLE);
            mReadCount.setVisibility(View.VISIBLE);
            if (!email.equals("")) {
                mEmail.setText(email);
            } else {
                mEmail.setText("");
            }
            if (!username.equals("")) {
                mUsernameLogged.setText(username);
            } else {
                mEmail.setText(mSession.getUserDetails().get("UserName"));
            }
            mEmail.setVisibility(View.VISIBLE);
            mSigninWithGoogleButton.setVisibility(View.GONE);
            mSigninWithFacebookButton.setVisibility(View.GONE);
            getView().findViewById(R.id.loginForm).setVisibility(View.GONE);
        } else {
            mUsernameLogged.setVisibility(View.GONE);
            mSingOutButton.setVisibility(View.GONE);
            mSigninButton.setVisibility(View.VISIBLE);
            mEmailHolder.setVisibility(View.GONE);
            mUsernameHolder.setVisibility(View.GONE);
            mReadCount.setVisibility(View.GONE);
            mReadCountHolder.setVisibility(View.GONE);
            mEmail.setVisibility(View.GONE);
            mSigninWithGoogleButton.setVisibility(View.VISIBLE);
            mSigninWithFacebookButton.setVisibility(View.VISIBLE);
            getView().findViewById(R.id.loginForm).setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(mContext);
        mSession = new SessionManager(mContext);
        if (account != null) {
            updateUI(account.getEmail(), account.getDisplayName(), true);
        } else if (mSession.isLoggedIn()) {
            updateUI(mSession.getUserDetails().get(SessionManager.KEY_USERNAME), "", true);
        } else {
            updateUI("", "", false);
        }
    }
}
