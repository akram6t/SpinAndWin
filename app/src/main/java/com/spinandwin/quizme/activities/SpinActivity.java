package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinandwin.quizme.SpinWheel.LuckyWheelView;
import com.spinandwin.quizme.SpinWheel.model.LuckyItem;
import com.spinandwin.quizme.databinding.ActivitySpinBinding;
import com.spinandwin.quizme.models.UserModels;
import com.startapp.sdk.adsbase.StartAppAd;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SpinActivity extends AppCompatActivity {
    DatabaseReference database;
    ActivitySpinBinding binding;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySpinBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        binding.spinBtn.setEnabled(false);

        fetchfirebasedata();
        createModelforSpin();

        binding.spinbackarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fetchfirebasedata(){
        database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                binding.spinBtn.setEnabled(true);
                UserModels models = dataSnapshot.getValue(UserModels.class);
                binding.spinPoint.setText(String.valueOf(models.getCoins()));
                binding.spinleft.setText(String.valueOf(models.getSpin()));
            }
        });
    }

    private void createModelforSpin(){

        List<LuckyItem> data = new ArrayList<>();

        LuckyItem luckyItem1 = new LuckyItem();
        luckyItem1.topText = "0";
        luckyItem1.secondaryText = "COINS";
        luckyItem1.textColor = Color.parseColor("#212121");
        luckyItem1.color = Color.parseColor("#eceff1");
        data.add(luckyItem1);

        LuckyItem luckyItem2 = new LuckyItem();
        luckyItem2.topText = "5";
        luckyItem2.secondaryText = "COINS";
        luckyItem2.color = Color.parseColor("#00cf00");
        luckyItem2.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem2);

        LuckyItem luckyItem3 = new LuckyItem();
        luckyItem3.topText = "15";
        luckyItem3.secondaryText = "COINS";
        luckyItem3.textColor = Color.parseColor("#212121");
        luckyItem3.color = Color.parseColor("#eceff1");
        data.add(luckyItem3);

        LuckyItem luckyItem4 = new LuckyItem();
        luckyItem4.topText = "20";
        luckyItem4.secondaryText = "COINS";
        luckyItem4.color = Color.parseColor("#7f00d9");
        luckyItem4.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem4);

        LuckyItem luckyItem5 = new LuckyItem();
        luckyItem5.topText = "25";
        luckyItem5.secondaryText = "COINS";
        luckyItem5.textColor = Color.parseColor("#212121");
        luckyItem5.color = Color.parseColor("#eceff1");
        data.add(luckyItem5);

        LuckyItem luckyItem6 = new LuckyItem();
        luckyItem6.topText = "35";
        luckyItem6.secondaryText = "COINS";
        luckyItem6.color = Color.parseColor("#dc0000");
        luckyItem6.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem6);

        LuckyItem luckyItem7 = new LuckyItem();
        luckyItem7.topText = "45";
        luckyItem7.secondaryText = "COINS";
        luckyItem7.textColor = Color.parseColor("#212121");
        luckyItem7.color = Color.parseColor("#eceff1");
        data.add(luckyItem7);

        LuckyItem luckyItem8 = new LuckyItem();
        luckyItem8.topText = "50";
        luckyItem8.secondaryText = "COINS";
        luckyItem8.color = Color.parseColor("#008bff");
        luckyItem8.textColor = Color.parseColor("#ffffff");
        data.add(luckyItem8);

        binding.wheelview.setData(data);
        binding.wheelview.setRound(5);

                binding.spinBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int leftspin = Integer.parseInt(binding.spinleft.getText().toString());

                        if (leftspin > 0){
                            binding.spinBtn.setEnabled(false);
                            Random r = new Random();
                            int randomNumber = r.nextInt(8);

                            binding.wheelview.startLuckyWheelWithTargetIndex(randomNumber);
                            int currentSpin = Integer.parseInt(binding.spinleft.getText().toString());
                            Map<String, Object> updateSpin = new HashMap();
                            updateSpin.put("spin", currentSpin-1);
                            database.child("user").child(auth.getUid()).updateChildren(updateSpin).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    binding.spinleft.setText(String.valueOf(leftspin-1));
                                }
                            });

                        }else {
                            Toast.makeText(SpinActivity.this, "You Have Not Spin", Toast.LENGTH_SHORT).show();

                        }

                    }
                });

                binding.wheelview.setLuckyRoundItemSelectedListener(new LuckyWheelView.LuckyRoundItemSelectedListener() {
                    @Override
                    public void LuckyRoundItemSelected(int index) {
                        updateCash(index);
                    }
                });
            }


            void updateCash(int index) {
                long cash = 0;
                switch (index) {
                    case 0:
                        cash = 0;
                        break;
                    case 1:
                        cash = 5;
                        break;
                    case 2:
                        cash = 15;
                        break;
                    case 3:
                        cash = 20;
                        break;
                    case 4:
                        cash = 25;
                        break;
                    case 5:
                        cash = 35;
                        break;
                    case 6:
                        cash = 40;
                        break;
                    case 7:
                        cash = 50;
                        break;
                }

                String coins = String.valueOf(cash);

                long currentCoins = Long.valueOf(binding.spinPoint.getText().toString());
                Map<String, Object> updateCoins = new HashMap();
                updateCoins.put("coins", currentCoins+cash);

                StartAppAd.showAd(SpinActivity.this);

                database.child("user").child(auth.getUid()).updateChildren(updateCoins).addOnSuccessListener(new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        binding.spinBtn.setEnabled(true);
                        Toast.makeText(SpinActivity.this, coins + " coins added in wallet", Toast.LENGTH_SHORT).show();
                        int spinpoint = Integer.parseInt(binding.spinPoint.getText().toString());
                        int total = spinpoint + Integer.parseInt(coins);
                        binding.spinPoint.setText(String.valueOf(total));
                    }
                });

     }

}