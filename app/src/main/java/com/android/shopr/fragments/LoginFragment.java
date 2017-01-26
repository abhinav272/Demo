package com.android.shopr.fragments;

import android.annotation.SuppressLint;
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
import com.android.shopr.model.UserProfile;
import com.android.shopr.utils.ExecutorSupplier;
import com.android.shopr.utils.PreferenceUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
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
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Created by abhinav.sharma on 11/26/2016.
 */
public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FacebookCallback<LoginResult> {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int RC_SIGN_IN = 0X2F;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private ProgressDialog progress;
    private LoginButton fbSignIn;
    private CallbackManager callbackManager;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        initFirebaseAuth();
        buildGoogleApiClient();
        setupUI(view);
        return view;
    }

    private void initFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
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
        };
    }

    @SuppressLint("WrongViewCast")
    private void setupUI(View view) {
        Button googleSignIn = (Button) view.findViewById(R.id.btn_google_sign_in);
        googleSignIn.setOnClickListener(this);
        fbSignIn = (LoginButton) view.findViewById(R.id.btn_fb_sign_in);
        callbackManager = CallbackManager.Factory.create();
        setUpFBLoginButton();
    }

    private void setUpFBLoginButton() {
        fbSignIn.setReadPermissions(new String[]{"email", "public_profile", "user_birthday"});
        fbSignIn.setFragment(this);
        fbSignIn.registerCallback(callbackManager, this);
        fbSignIn.setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    private void buildGoogleApiClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_gcp))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(), this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
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

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            firebaseAuthWithGoogle(acct);
        } else {
            Log.e(TAG, "handleSignInResult: sign in failed");
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
                            revokeAccess();
                        }
                    }
                });
    }

    private void showHomeActivity() {
        ((OnBoardActivity) getActivity()).showHomeActivity();
    }

    private void onAccountSelected(FirebaseUser user) {
        try {
            UserProfile userProfile = new UserProfile();
            if (user != null) {
                userProfile.setPersonName(user.getDisplayName());
                userProfile.setAccessToken(user.getUid());
                userProfile.setContact(null);
                if (user.getPhotoUrl() != null) {
                    String imageUrl = user.getPhotoUrl().toString();
                    userProfile.setPicUrl(imageUrl.replace("s96-c", "s200-c"));
                }
                userProfile.setEmailId(user.getEmail());
                userProfile.setProvider(user.getProviders());
            }
            saveProfileData(userProfile);
            Log.e(TAG, "onAccountSelected: " + userProfile.getPersonName() + " " + userProfile.getProvider());
            showHomeActivity();
        } catch (Exception e) {
            e.printStackTrace();
            revokeAccess();
        } finally {
            hideProgress();
        }
    }

    private void saveProfileData(final UserProfile userProfile) {
        ExecutorSupplier.getInstance().getWorkerThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                PreferenceUtils.getInstance(getActivity()).saveUserProfile(userProfile);
            }
        });
    }

    private void revokeAccess() {
        Log.d(TAG, "revokeAccess: ");
        LoginManager.getInstance().logOut();
        mAuth.signOut();
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
        switch (view.getId()) {
            case R.id.btn_google_sign_in:
                signInWithGoogle();
                break;
        }
    }

    @Override
    public void onSuccess(LoginResult loginResult) {
        handleFacebookAccessToken(loginResult.getAccessToken());
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
        showProgress("Connecting...");
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            if (task.getException().getClass().getSimpleName().equals("FirebaseAuthUserCollisionException"))
                                showShortToast("Try signing in with Google");
                            else showShortToast("Authentication failed.");
                            hideProgress();
                            revokeAccess();
                        }
                    }
                });
    }

    @Override
    public void onCancel() {
        Log.e(TAG, "FB Login onCancel: ");
    }

    @Override
    public void onError(FacebookException error) {
        Log.e(TAG, "FB Login onError: ", error.fillInStackTrace());
    }
}
