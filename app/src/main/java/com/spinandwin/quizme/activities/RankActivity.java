package com.spinandwin.quizme.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.os.Bundle;
import android.view.View;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.spinandwin.quizme.adapters.RankAdapter;
import com.spinandwin.quizme.databinding.ActivityRankBinding;
import com.spinandwin.quizme.databinding.RankBottomshitLayoutBinding;
import com.spinandwin.quizme.models.RankModels;
import com.spinandwin.quizme.models.UserModels;
import com.spinandwin.quizme.other.Loading;
import java.util.ArrayList;

public class RankActivity extends AppCompatActivity {
    ActivityRankBinding binding;
    BottomSheetDialog rankDialog;
    RankBottomshitLayoutBinding rankdialogBinding;
    Loading loading;
    private int rank = 0;
    DatabaseReference database;
    FirebaseAuth auth;
    RankAdapter rankAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRankBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loading = new Loading(RankActivity.this);
        loading.create();
        loading.show();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();

        fetchdatabasecoins();
        rankbottomshitdialog();
        setrankRecyclerview();

        binding.rankbackarow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void fetchdatabasecoins(){
        String uid = auth.getCurrentUser().getUid();
        database.child("user").child(uid).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                UserModels obj = dataSnapshot.getValue(UserModels.class);
                binding.ranktextPoint.setText(String.valueOf(obj.getCoins()));
            }
        });
    }

    private void rankbottomshitdialog(){
        rankdialogBinding  = RankBottomshitLayoutBinding.inflate(getLayoutInflater());
        rankDialog = new BottomSheetDialog(RankActivity.this);
        rankDialog.setContentView(rankdialogBinding.getRoot());
        rankDialog.create();
    }

    private void setrankRecyclerview(){
        ArrayList<RankModels> rankarrayList = new ArrayList<>();
        rankAdapter = new RankAdapter(RankActivity.this, rankarrayList, rankDialog, rankdialogBinding);
        binding.rankrecyclerview.setAdapter(rankAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(RankActivity.this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        binding.rankrecyclerview.setLayoutManager(linearLayoutManager);

        database.child("user").orderByChild("coins").limitToFirst(50).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot){
                rank = (int) snapshot.getChildrenCount()+1;
                for (DataSnapshot obj : snapshot.getChildren()){
                    UserModels models = obj.getValue(UserModels.class);
                    rank--;
                    rankarrayList.add(new RankModels(String.valueOf(rank), models.getId(), models.getCoins(),models.getName(),models.getProfile()));
                }
                rankAdapter.notifyDataSetChanged();
                loading.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}