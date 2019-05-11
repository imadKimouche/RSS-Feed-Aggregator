package com.ignite.rssfa;

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
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MyAccountFragment extends Fragment {

    private static final int RC_SIGN_IN = 1;
    final private String mUsername = "";
    final private String mPassword = "";
    private GoogleSignInClient mGoogleSignInClient;
    private Button mSigninButton;
    private Button mSigninWithGoogleButton;
    private Button mSigninWithFacebookButton;
    private Button mSingOutButton;
    private TextView mEmail;
    private TextView mEmailHolder;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        View usernameEditText = view.findViewById(R.id.username);
        View passwordEditText = view.findViewById(R.id.password);

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

            }
        });

        mSigninWithGoogleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });

        mSigninWithFacebookButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

            }
        });

        mSingOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
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

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();

            // Send token to server

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign in", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            mSingOutButton.setVisibility(View.VISIBLE);
            mEmailHolder.setVisibility(View.VISIBLE);
            mEmail.setText(account.getEmail());
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
        if (account != null) {
            updateUI(account);
        } else {

            updateUI(null);
        }
    }
}
