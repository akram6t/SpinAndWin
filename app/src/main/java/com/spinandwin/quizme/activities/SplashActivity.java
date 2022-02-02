package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.models.UserModels;
import com.startapp.sdk.adsbase.StartAppAd;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    DatabaseReference database;
    FirebaseAuth auth;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        imageView = findViewById(R.id.imageView6);

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        StartAppAd.disableSplash();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        animation(imageView);

    }

    private void animation(ImageView view){
        view.animate().rotation(360).setDuration(1000).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (auth.getCurrentUser() != null){
                    everydayspinandscratchreset();
                }else {
                    startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                    finish();
                }
            }
        }).start();
    }

    // every day spin and scratch reset

    // chack firebase field date
    // database date not match today date reset spin and scratch and update date to todaydate
    // user again open app and firebase database date match to todayDate not reset spin and scratch
    private void everydayspinandscratchreset(){
        String uid = auth.getUid();
        database.child("user").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModels models = dataSnapshot.getValue(UserModels.class);
                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");// dd = day, MM = month and yyyy = year;
                String todayDate = dateFormat.format(new Date());
                if (models.getTodayDate().equals(todayDate)){
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Map<String,Object> updateDate = new HashMap<>();
                    updateDate.put("todayDate", todayDate);
                    updateDate.put("spin", 10);
                    updateDate.put("scratch", 10);

                    database.child("user").child(uid).updateChildren(updateDate).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            startActivity(new Intent(SplashActivity.this, AuthActivity.class));
                            finish();
                        }
                    }).addOnCanceledListener(new OnCanceledListener() {
                        @Override
                        public void onCanceled() {
                            finish();
                            Toast.makeText(SplashActivity.this, "Connection Problem", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnCanceledListener(new OnCanceledListener() {
            @Override
            public void onCanceled() {
                finish();
                Toast.makeText(SplashActivity.this, "Connection Problem", Toast.LENGTH_SHORT).show();
            }
        });
    }

}