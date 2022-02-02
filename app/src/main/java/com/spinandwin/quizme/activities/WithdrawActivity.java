package com.spinandwin.quizme.activities;

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
import com.spinandwin.quizme.databinding.ActivityWithdrawBinding;
import com.spinandwin.quizme.models.UserModels;
import com.spinandwin.quizme.other.Loading;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends AppCompatActivity {
    private ActivityWithdrawBinding binding;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private long coins;
    private Loading loading;
    private int selectedMoney = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loading = new Loading(WithdrawActivity.this);
        loading.create();
        loading.show();

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        fetchfirebasedata();
        selectmoney();

        binding.btnRedeem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedMoney==0){
                    Toast.makeText(WithdrawActivity.this, "Please Select Amount", Toast.LENGTH_SHORT).show();
                }else {
                    senddatatodatabase();
                }
            }
        });

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
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
                loading.dismiss();
                UserModels models = dataSnapshot.getValue(UserModels.class);
                coins = models.getCoins();
                binding.redeemCoins.setText(String.valueOf(coins));
            }
        });
    }

    private void selectmoney(){
        binding.select25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins<25000){
                    Toast.makeText(WithdrawActivity.this, "Your coins is less than 25000", Toast.LENGTH_SHORT).show();
                }else {
                    selectedMoney = 25;
                    binding.select25.setBackgroundResource(R.drawable.shape_holdwithdraw);

                    binding.select50.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select100.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select500.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                }
            }
        });
        binding.select50.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins<50000){
                    Toast.makeText(WithdrawActivity.this, "Your coins is less than 25000", Toast.LENGTH_SHORT).show();
                }else {
                    selectedMoney = 50;
                    binding.select50.setBackgroundResource(R.drawable.shape_holdwithdraw);

                    binding.select25.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select100.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select500.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                }
            }
        });
        binding.select100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins<100000){
                    Toast.makeText(WithdrawActivity.this, "Your coins is less than 100000", Toast.LENGTH_SHORT).show();
                }else {
                    selectedMoney = 100;
                    binding.select100.setBackgroundResource(R.drawable.shape_holdwithdraw);

                    binding.select50.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select25.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select500.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                }
            }
        });
        binding.select500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (coins<500000){
                    Toast.makeText(WithdrawActivity.this, "Your coins is less than 500000", Toast.LENGTH_SHORT).show();
                }else {
                    selectedMoney = 500;
                    binding.select500.setBackgroundResource(R.drawable.shape_holdwithdraw);

                    binding.select50.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select100.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                    binding.select25.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                }
            }
        });


    }

    private void senddatatodatabase(){
        if(binding.edit91.getText().toString().trim().isEmpty()){
            Toast.makeText(WithdrawActivity.this, "Enter Right Country Code", Toast.LENGTH_SHORT).show();
        }else if (binding.editNumber.getText().toString().trim().length()!=10){
            Toast.makeText(WithdrawActivity.this, "Enter 10 Degit Number", Toast.LENGTH_SHORT).show();
        }else {
            loading.show();
            String countryCode = binding.edit91.getText().toString();
            String phoneNumber = countryCode+binding.editNumber.getText().toString();
            String date = new Date().toString();
            Map<String, Object> removeCoins = new HashMap();
            removeCoins.put("coins", coins-selectedMoney*1000);
            database.child("user").child(auth.getUid()).updateChildren(removeCoins).addOnSuccessListener(new OnSuccessListener() {
                @Override
                public void onSuccess(Object o) {
                    coins = coins-selectedMoney*1000;
                    binding.redeemCoins.setText(String.valueOf(coins));
                    Map<String, Object> map = new HashMap();
                    map.put("phone", phoneNumber);
                    map.put("money", selectedMoney);
                    map.put("date", date);
                    database.child("redeem").child(auth.getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loading.dismiss();
                            Toast.makeText(WithdrawActivity.this, "Your Payment Receive in 24 hours", Toast.LENGTH_SHORT).show();
                            selectedMoney = 0;
                            binding.select25.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                            binding.select50.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                            binding.select100.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                            binding.select500.setBackgroundResource(R.drawable.shape_defaultwithdraw);
                        }
                    });
                }
            });

        }
    }

}