package com.spinandwin.quizme.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.databinding.ActivityScratchBinding;
import com.spinandwin.quizme.models.UserModels;
import com.spinandwin.quizme.other.Loading;
import com.startapp.sdk.adsbase.StartAppAd;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import dev.skymansandy.scratchcardlayout.listener.ScratchListener;
import dev.skymansandy.scratchcardlayout.ui.ScratchCardLayout;

public class ScratchActivity extends AppCompatActivity {
    private ActivityScratchBinding binding;
    private FirebaseAuth auth;
    private DatabaseReference database;
    private int rand;
    Loading loading;
    private UserModels models;
    private boolean isreset;
    private long currentCoins;
    private int currentScratch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScratchBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new Loading(this);
        loading.create();
        loading.show();
        isreset = true;

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getWindow().setStatusBarColor(getResources().getColor(R.color.green));
        getWindow().setNavigationBarColor(getResources().getColor(R.color.appbackround));

        firebasedatafetch();
        scratchbackbtn();

    }

    private void scratchcard(){
        Random random = new Random();
        binding.scratchCard.setScratchListener(new ScratchListener() {
            @Override
            public void onScratchStarted() {
                currentScratch = Integer.parseInt(binding.scratchleft.getText().toString());
                currentCoins = Long.parseLong(binding.scoins.getText().toString());

                binding.btnscratchreset.setEnabled(false);
                if (currentScratch>0){
                    isreset = true;
                    rand = random.nextInt(50);
                    binding.scratchpoin.setText(String.valueOf(rand));
                }else {
                    binding.scratchpoin.setText("Daily Limit Over");
                }
            }
            @Override
            public void onScratchProgress(@NonNull ScratchCardLayout scratchCardLayout, int i) {
                if (currentScratch>0) {
                    binding.scratchCard.setRevealFullAtPercent(30);
                }
            }
            @Override
            public void onScratchComplete() {
                StartAppAd.showAd(ScratchActivity.this);
                binding.btnscratchreset.setEnabled(true);
                if (isreset) {
                    Map<String, Object> updatescratch = new HashMap<>();
                    updatescratch.put("scratch", currentScratch- 1);
                    database.child("user").child(auth.getUid()).updateChildren(updatescratch, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            binding.scratchleft.setText(String.valueOf(currentScratch-1));
                        }
                    });
                    Map<String, Object> updateCoins = new HashMap<>();
                    long prevCoins = Long.parseLong(binding.scoins.getText().toString());
                    updateCoins.put("coins", prevCoins + rand);
                    isreset = false;
                    database.child("user").child(auth.getUid()).updateChildren(updateCoins, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            Toast.makeText(ScratchActivity.this, rand + " Coins Added", Toast.LENGTH_SHORT).show();
                            binding.scoins.setText(String.valueOf(currentCoins+rand));
                        }
                    });
                }
                binding.btnscratchreset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.scratchCard.resetScratch();
                    }
                });
            }
        });

    }

    private void firebasedatafetch(){
        database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                loading.dismiss();
                models = dataSnapshot.getValue(UserModels.class);
                binding.scoins.setText(String.valueOf(models.getCoins()));
                binding.scratchleft.setText(String.valueOf(models.getScratch()));
                scratchcard();
            }
        });

    }

    private void scratchbackbtn(){
        binding.scratchbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }

}