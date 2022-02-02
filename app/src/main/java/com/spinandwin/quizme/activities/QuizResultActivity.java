package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.spinandwin.quizme.databinding.ActivityQuizResultBinding;
import com.startapp.sdk.adsbase.StartAppAd;

public class QuizResultActivity extends AppCompatActivity {
    ActivityQuizResultBinding binding;
    int totalQuestion;
    int earnedCoins;
    int rightAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        totalQuestion = getIntent().getIntExtra("total", 0);
        earnedCoins = getIntent().getIntExtra("coins", 0);
        rightAnswer = getIntent().getIntExtra("right", 0);

        binding.score.setText(String.format("%d/%d", rightAnswer, totalQuestion));
        binding.earnedCoins.setText(String.valueOf(earnedCoins));

        binding.btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartAppAd.showAd(QuizResultActivity.this);
                finish();
            }
        });

    }
}