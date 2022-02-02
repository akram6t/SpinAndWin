package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.databinding.ActivityQuizBinding;
import com.spinandwin.quizme.models.QuizModels;
import com.spinandwin.quizme.models.UserModels;
import com.spinandwin.quizme.other.Loading;
import com.startapp.sdk.adsbase.StartAppAd;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class QuizActivity extends AppCompatActivity {
    private ActivityQuizBinding binding;
    private DatabaseReference database;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String cat_id, cat_name;
    private ArrayList<QuizModels> quizList;
    private Loading loading;
    private QuizModels quizModels;
    private int questionIndex = 0;
    private int coins = 0;
    private int numofRightAnswer = 0;
    private int selectedAnswer = 0;
    private CountDownTimer countimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        loading = new Loading(this);
        loading.create();
        loading.show();

        cat_id = getIntent().getStringExtra(getString(R.string.quizcategory));
        cat_name = getIntent().getStringExtra(getString(R.string.category_name));
        binding.categoryName.setText(cat_name);

        quizList = new ArrayList<>();

        getQuizFromFirebaseFirestore();

        alloptionclicklistener();

        btnNextonclicked();

        binding.btnQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getQuizFromFirebaseFirestore(){
        firestore.collection("Quiz").document(cat_id).collection("Q").limit(10).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()){
                    quizModels = snapshot.toObject(QuizModels.class);
                    quizList.add(quizModels);
                }
                Collections.shuffle(quizList);
                binding.textleftquiz.setText(String.format("%d/%d", questionIndex+1, quizList.size()));
                loading.dismiss();
                quizaddintextview();
                secondCount();

                if (quizList.size() == 0){
                    finish();
                    Toast.makeText(QuizActivity.this, "Question Not Available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void quizaddintextview(){
        if (quizList.size() != 0) {
            binding.quistion.setText(quizList.get(questionIndex).getQuestion());

            binding.option1.setText(quizList.get(questionIndex).getOption1());
            binding.option2.setText(quizList.get(questionIndex).getOption2());
            binding.option3.setText(quizList.get(questionIndex).getOption3());
            binding.option4.setText(quizList.get(questionIndex).getOption4());
        }

    }

    private void alloptionclicklistener(){
        binding.option1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alloptionEnable(false);
                    selectedAnswer = 1;
                    whenclickonoption();
            }
        });
        binding.option2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alloptionEnable(false);
                    selectedAnswer = 2;
                    whenclickonoption();
                }
        });
        binding.option3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alloptionEnable(false);
                    selectedAnswer = 3;
                    whenclickonoption();

            }
        });
        binding.option4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alloptionEnable(false);
                    selectedAnswer = 4;
                    whenclickonoption();
            }
        });
    }

    private void btnNextonclicked(){
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionIndex < quizList.size()-1){
                    if (countimer != null){
                        countimer.cancel();
                    }
                    StartAppAd.showAd(QuizActivity.this);
                    assert countimer != null;
                    countimer.start();
                    removeoptionbackround();
                    alloptionEnable(true);
                    questionIndex += 1;
                    quizaddintextview();
                    binding.textleftquiz.setText(String.format("%d/%d", questionIndex+1, quizList.size()));
                }else {
                    countimer.cancel();
                    loading.show();
                    database.child("user").child(auth.getUid()).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Random random = new Random();
                            int randomcoins = random.nextInt(coins+1);
                            UserModels models = dataSnapshot.getValue(UserModels.class);
                            long previusCoins = models.getCoins();
                            Map<String, Object> upcoins = new HashMap();
                            upcoins.put("coins", previusCoins+randomcoins);
                            database.child("user").child(auth.getUid()).updateChildren(upcoins).addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    loading.dismiss();
                                    Intent intent = new Intent(QuizActivity.this, QuizResultActivity.class);
                                    intent.putExtra("right", numofRightAnswer);
                                    intent.putExtra("coins", randomcoins);
                                    intent.putExtra("total", quizList.size());
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });

                }
            }
        });
    }

    private void removeoptionbackround(){
        binding.option1.setBackgroundResource(R.drawable.shape_default_option);
        binding.option2.setBackgroundResource(R.drawable.shape_default_option);
        binding.option3.setBackgroundResource(R.drawable.shape_default_option);
        binding.option4.setBackgroundResource(R.drawable.shape_default_option);
    }

    private void whenclickonoption(){
                if (!quizList.get(questionIndex).getAnswer().equals(binding.option1.getText().toString()) && selectedAnswer==1){
                    binding.option1.setBackgroundResource(R.drawable.shape_wrong_answer);

                }else if (!quizList.get(questionIndex).getAnswer().equals(binding.option2.getText().toString()) && selectedAnswer==2){
                    binding.option2.setBackgroundResource(R.drawable.shape_wrong_answer);

                }else if (!quizList.get(questionIndex).getAnswer().equals(binding.option3.getText().toString()) && selectedAnswer==3){
                    binding.option3.setBackgroundResource(R.drawable.shape_wrong_answer);

                } else if (!quizList.get(questionIndex).getAnswer().equals(binding.option4.getText().toString()) && selectedAnswer==4) {
                    binding.option4.setBackgroundResource(R.drawable.shape_wrong_answer);
                }


                if (quizList.get(questionIndex).getAnswer().equals(binding.option1.getText().toString()) && selectedAnswer==1){
                    binding.option1.setBackgroundResource(R.drawable.shape_right_answer);
                    coins += 10;
                    numofRightAnswer++;

                }else if (quizList.get(questionIndex).getAnswer().equals(binding.option2.getText().toString()) && selectedAnswer==2){
                    binding.option2.setBackgroundResource(R.drawable.shape_right_answer);
                    coins += 10;
                    numofRightAnswer++;

                }else if (quizList.get(questionIndex).getAnswer().equals(binding.option3.getText().toString()) && selectedAnswer==3){
                    binding.option3.setBackgroundResource(R.drawable.shape_right_answer);
                    coins += 10;
                    numofRightAnswer++;

                } else if (quizList.get(questionIndex).getAnswer().equals(binding.option4.getText().toString()) && selectedAnswer==4) {
                    binding.option4.setBackgroundResource(R.drawable.shape_right_answer);
                    coins += 10;
                    numofRightAnswer++;
                }

        if (quizList.get(questionIndex).getAnswer().equals(binding.option1.getText().toString()) && selectedAnswer!=1){
            binding.option1.setBackgroundResource(R.drawable.shape_right_answer);

        }else if (quizList.get(questionIndex).getAnswer().equals(binding.option2.getText().toString()) && selectedAnswer!=2){
            binding.option2.setBackgroundResource(R.drawable.shape_right_answer);

        }else if (quizList.get(questionIndex).getAnswer().equals(binding.option3.getText().toString()) && selectedAnswer!=3){
            binding.option3.setBackgroundResource(R.drawable.shape_right_answer);

        } else if (quizList.get(questionIndex).getAnswer().equals(binding.option4.getText().toString()) && selectedAnswer!=4) {
            binding.option4.setBackgroundResource(R.drawable.shape_right_answer);
        }

    }

    private void secondCount(){
        countimer = new CountDownTimer(30000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        binding.textstopWatchCount.setText(String.valueOf(millisUntilFinished/1000));
                    }

                    @Override
                    public void onFinish() {
                        binding.btnNext.callOnClick();
                    }
                };
        countimer.start();
    }

    private void alloptionEnable(Boolean enable){
        binding.option1.setEnabled(enable);
        binding.option2.setEnabled(enable);
        binding.option3.setEnabled(enable);
        binding.option4.setEnabled(enable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countimer != null) {
            countimer.cancel();
        }
    }
}