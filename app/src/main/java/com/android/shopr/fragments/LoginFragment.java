package com.android.shopr.fragments;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.android.shopr.OnBoardActivity;
import com.android.shopr.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */
public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, FirebaseAuth.AuthStateListener, View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 0X2F;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        buildGoogleApiClient();
        setupUI(view);
        return view;
    }

    private void setupUI(View view) {
        Button googleSignIn = (Button) view.findViewById(R.id.btn_google_sign_in);
        googleSignIn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    private void buildGoogleApiClient() {
        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_gcp))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed: " + connectionResult.getErrorMessage());
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
                handleSignInResult(result);
            else Toast.makeText(getActivity(), "Google Sign-in failed", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();
            Log.d(TAG, "handleSignInResult: " + acct.getDisplayName());
            Log.d(TAG, "handleSignInResult: " + acct.getEmail());
            Log.d(TAG, "handleSignInResult: " + acct.getPhotoUrl());
            Log.d(TAG, "handleSignInResult: " + acct.getGrantedScopes());
//            updateUI(true);
            firebaseAuthWithGoogle(acct);
        } else {
            // TODO -- show screen for re-authentication and educate user
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        showProgress("Connecting...");
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            showShortToast("User Authentication failed");
                            hideProgress();
                        } else {
                            showHomeActivity();
                        }
                    }
                });
    }

    private void showHomeActivity() {
        ((OnBoardActivity) getActivity()).showHomeActivity();
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            // User is signed in
            Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            onAccountSelected(user);

        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }

    private void onAccountSelected(FirebaseUser user) {
        try {
            String personName = user.getDisplayName();
            String personPhotoUrl = user.getPhotoUrl().toString();
            String email = user.getEmail();
            Log.e(TAG, "onAccountSelected: " + personName + " " + personPhotoUrl + " " + email);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            hideProgress();
        }
    }

    private void hideProgress() {
        if (progress != null) {
            progress.hide();
            progress.dismiss();
        }
    }

    public void showProgress(String message) {
        if (progress == null) {
            progress = ProgressDialog.show(getActivity(), "", message, true);
        } else if (progress.isShowing()) {
            progress.setMessage(message);
        } else {
            progress.setMessage(message);
            progress.show();
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_google_sign_in:
                signInWithGoogle();
                break;
        }
    }
}
