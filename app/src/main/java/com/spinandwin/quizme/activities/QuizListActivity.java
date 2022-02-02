package com.spinandwin.quizme.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.spinandwin.quizme.adapters.QuizListAdapters;
import com.spinandwin.quizme.databinding.ActivityQuizlistBinding;
import com.spinandwin.quizme.models.QuizListModels;
import com.spinandwin.quizme.other.Loading;

import java.util.ArrayList;
import java.util.Collections;

public class QuizListActivity extends AppCompatActivity {
    ActivityQuizlistBinding binding;
    TextView quizListPoint;
    FirebaseFirestore firestore;
    ArrayList<QuizListModels> quizList;
    QuizListAdapters quizListAdapters;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQuizlistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firestore = FirebaseFirestore.getInstance();

        loading = new Loading(QuizListActivity.this);
        loading.create();
        loading.show();
        quizList = new ArrayList<>();
        quizListAdapters = new QuizListAdapters(QuizListActivity.this, quizList);
        binding.quizlistrecyclerview.setAdapter(quizListAdapters);
        binding.quizlistrecyclerview.setLayoutManager(new GridLayoutManager(QuizListActivity.this, 2));

        setquizcategory();

        binding.quizListbackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void setquizcategory(){
        firestore.collection("Quiz").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (DocumentSnapshot obj : queryDocumentSnapshots.getDocuments()){
                    QuizListModels quizListModels = new QuizListModels(obj.getString("name"), obj.getString("logo"), obj.getId());
                    quizList.add(quizListModels);
                }
                Collections.shuffle(quizList);
                quizListAdapters.notifyDataSetChanged();
                loading.dismiss();
            }
        });

    }

}