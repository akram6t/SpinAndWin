package com.spinandwin.quizme.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.databinding.LoadingDialogLayoutBinding;
import com.spinandwin.quizme.models.UserModels;
import com.spinandwin.quizme.other.Loading;
import com.startapp.sdk.adsbase.StartAppAd;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AuthActivity extends AppCompatActivity {
    LoadingDialogLayoutBinding progressBarLayout;
    Loading loading;
    FirebaseAuth auth;
    DatabaseReference database;
    GoogleSignInClient mGoogleSignInClient;
    final int RC_SIGN_IN = 95;
    Button btnSignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        // sign in button findviewById
        btnSignin = findViewById(R.id.btnsignin);

        // disable interstitialAds
        StartAppAd.disableAutoInterstitial();
        // initialise auth and database
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        progressBarLayout = LoadingDialogLayoutBinding.inflate(getLayoutInflater());

        // custom loading class
        loading = new Loading(this);
        loading.create();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.show();
                signIn();
            }
        });

    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {

            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String id = auth.getCurrentUser().getUid();
                            String name = auth.getCurrentUser().getDisplayName();
                            String email = auth.getCurrentUser().getEmail();
                            String profile = auth.getCurrentUser().getPhotoUrl().toString();
                            String createdBy = new Date().toString();
                            String todayDate = new SimpleDateFormat("ddMMyyyy").format(new Date());

                            UserModels adddata = new UserModels(id,name,email,profile,todayDate, createdBy);
                            chackauthisalready(adddata);

                        } else {
                            Toast.makeText(AuthActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                Toast.makeText(AuthActivity.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //  chack firebase database if you are login or signup if child has contain your auth id means your account are login
    // what use of this method - if your account already exists , not reset your coins, spin scratch and etc.
    private void chackauthisalready(UserModels adddata){
       database.addListenerForSingleValueEvent(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.child("user").hasChild(auth.getUid())){
                   Toast.makeText(AuthActivity.this, "Sign In Complete", Toast.LENGTH_LONG).show();
                   startActivity(new Intent(AuthActivity.this, MainActivity.class));
                   finishAffinity();
                   loading.dismiss();
               }else {
                   if (!snapshot.hasChild(auth.getUid())){
                       database.child("user").child(auth.getUid()).setValue(adddata).addOnSuccessListener(new OnSuccessListener<Void>() {
                           @Override
                           public void onSuccess(Void unused) {
                               Toast.makeText(AuthActivity.this, "Signup Complete", Toast.LENGTH_LONG).show();
                               loading.dismiss();
                               startActivity(new Intent(AuthActivity.this, MainActivity.class));
                               finishAffinity();
                           }
                       });
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });

    }

    @Override
    protected void onStart() {
        super.onStart();
        // user already logged in
        // skip auth activity
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(AuthActivity.this, MainActivity.class));
            finishAffinity();
        }
    }
}