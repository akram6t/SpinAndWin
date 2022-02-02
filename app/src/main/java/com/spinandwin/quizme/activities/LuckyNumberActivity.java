package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.databinding.ActivityLuckyNumberBinding;
import com.spinandwin.quizme.databinding.RewardLayoutBinding;
import com.spinandwin.quizme.models.UserModels;
import com.startapp.sdk.adsbase.StartAppAd;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LuckyNumberActivity extends AppCompatActivity {
    private ActivityLuckyNumberBinding binding;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private int chhosesNumber;
    private AlertDialog winDialog;
    private RewardLayoutBinding rewardLayoutBinding;
    private int randomValue, randomCoins;
    private long currentCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLuckyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(getResources().getColor(R.color.l_blue));
        getWindow().setStatusBarColor(getResources().getColor(R.color.l_blue));
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        rewardLayoutBinding = RewardLayoutBinding.inflate(getLayoutInflater());
        winDialog = new AlertDialog.Builder(this)
                .setView(rewardLayoutBinding.getRoot()).setCancelable(false)
                .create();
        winDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        binding.luckybackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fetchfirebasedata();
        chackluckynumber();

        binding.btnLuckyNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentCoins = Long.parseLong(binding.lCoins.getText().toString());
                if (currentCoins >= 10) {
                    if (chhosesNumber != 0) {
                        binding.btnLuckyNumber.setEnabled(false);
                        Random random = new Random();
                        randomValue = random.nextInt(15);
                        winDialog.show();
                        if (chhosesNumber == randomValue) {
                            randomCoins = random.nextInt(200);
                            rewardLayoutBinding.ldearntoptext.setText("You Earn");
                            rewardLayoutBinding.ldCoins.setText(randomCoins + "\n" + "Coins");
                            Map<String, Object> upcoins = new HashMap<>();
                            upcoins.put("coins", currentCoins + randomCoins);
                            database.child("user").child(auth.getUid()).updateChildren(upcoins).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    StartAppAd.showAd(LuckyNumberActivity.this);
                                    binding.lCoins.setText(String.valueOf(currentCoins + randomCoins));
                                    binding.btnLuckyNumber.setEnabled(true);
                                    Toast.makeText(LuckyNumberActivity.this, "You Earn "+String.valueOf(randomCoins), Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            rewardLayoutBinding.ldearntoptext.setText("You Lose");
                            rewardLayoutBinding.ldCoins.setText("-10 Coins");
                            Map<String,Object> upcoins = new HashMap();
                            upcoins.put("coins", currentCoins - 10);
                            database.child("user").child(auth.getUid()).updateChildren(upcoins).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    StartAppAd.showAd(LuckyNumberActivity.this);
                                    binding.lCoins.setText(String.valueOf(currentCoins - 10));
                                    binding.btnLuckyNumber.setEnabled(true);
                                    Toast.makeText(LuckyNumberActivity.this, String.valueOf("You Lose"), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        Toast.makeText(LuckyNumberActivity.this, "Please Chhose A Number", Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(LuckyNumberActivity.this, "You Have Not Coins", Toast.LENGTH_SHORT).show();
            }
        });

        rewardLayoutBinding.btnrewardOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                winDialog.dismiss();
            }
        });

    }

    private void chackluckynumber(){
        binding.one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 1;
                binding.one.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.one.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 2;
                binding.two.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.two.setTextColor(getResources().getColor(R.color.l_blue));

                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 3;
                binding.three.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.three.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 4;
                binding.four.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.four.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 5;
                binding.five.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.five.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 6;
                binding.six.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.six.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 7;
                binding.seven.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.seven.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 8;
                binding.eight.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.eight.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.nine.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.nine.setTextColor(getResources().getColor(R.color.white));
            }
        });
        binding.nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chhosesNumber = 9;
                binding.nine.setBackgroundResource(R.drawable.lucky_number_hold_backround);
                binding.nine.setTextColor(getResources().getColor(R.color.l_blue));

                binding.two.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.three.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.four.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.five.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.six.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.seven.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.eight.setBackgroundResource(R.drawable.lucky_number_default_backround);
                binding.one.setBackgroundResource(R.drawable.lucky_number_default_backround);

                binding.one.setTextColor(getResources().getColor(R.color.white));
                binding.two.setTextColor(getResources().getColor(R.color.white));
                binding.three.setTextColor(getResources().getColor(R.color.white));
                binding.four.setTextColor(getResources().getColor(R.color.white));
                binding.five.setTextColor(getResources().getColor(R.color.white));
                binding.six.setTextColor(getResources().getColor(R.color.white));
                binding.seven.setTextColor(getResources().getColor(R.color.white));
                binding.eight.setTextColor(getResources().getColor(R.color.white));
            }
        });

    }

    private void fetchfirebasedata(){
        database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModels models = dataSnapshot.getValue(UserModels.class);
                binding.lCoins.setText(String.valueOf(models.getCoins()));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StartAppAd.showAd(this);
    }
}