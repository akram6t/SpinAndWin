package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.adapters.MainCategoryAdapter;
import com.spinandwin.quizme.databinding.ActivityMainBinding;
import com.spinandwin.quizme.models.MainCategoryModels;
import com.spinandwin.quizme.models.UserModels;
import com.spinandwin.quizme.other.Loading;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    DatabaseReference database;
    FirebaseAuth auth;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        loading = new Loading(MainActivity.this);
        loading.create();
        loading.show();
        getWindow().setNavigationBarColor(getResources().getColor(R.color.orange));
        setmaincategory();
        getDatabaseData();

        binding.textPoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WithdrawActivity.class));
            }
        });

    }

    private void setmaincategory(){
        ArrayList<MainCategoryModels> modelsArrayList = new ArrayList<>();
        modelsArrayList.add(new MainCategoryModels(R.drawable.spin, "Spin"));
        modelsArrayList.add(new MainCategoryModels(R.drawable.scratch, "Scratch"));
        modelsArrayList.add(new MainCategoryModels(R.drawable.lucky, "Lucky"));
        modelsArrayList.add(new MainCategoryModels(R.drawable.quiz, "Quiz"));
        modelsArrayList.add(new MainCategoryModels(R.drawable.ranking, "Rank"));
        modelsArrayList.add(new MainCategoryModels(R.drawable.withdraw, "Withdraw"));
        modelsArrayList.add(new MainCategoryModels(R.drawable.profile, "profile"));

        MainCategoryAdapter mcAdapter = new MainCategoryAdapter(MainActivity.this, modelsArrayList);
        binding.categoryrecyclerview.setAdapter(mcAdapter);
        binding.categoryrecyclerview.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));

    }

    private void getDatabaseData(){
        database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModels userModels = dataSnapshot.getValue(UserModels.class);
                binding.textPoint.setText(String.valueOf(userModels.getCoins()));
                loading.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        binding.textPoint.setText("...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModels userModels = dataSnapshot.getValue(UserModels.class);
                binding.textPoint.setText(String.valueOf(userModels.getCoins()));
            }
        });

    }
}