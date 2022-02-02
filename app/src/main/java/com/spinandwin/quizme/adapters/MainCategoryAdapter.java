package com.spinandwin.quizme.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.activities.LuckyNumberActivity;
import com.spinandwin.quizme.activities.ProfileActivity;
import com.spinandwin.quizme.activities.QuizListActivity;
import com.spinandwin.quizme.activities.RankActivity;
import com.spinandwin.quizme.activities.ScratchActivity;
import com.spinandwin.quizme.activities.SpinActivity;
import com.spinandwin.quizme.activities.WithdrawActivity;
import com.spinandwin.quizme.databinding.MainCategoryBinding;
import com.spinandwin.quizme.models.MainCategoryModels;
import java.util.ArrayList;

public class MainCategoryAdapter extends RecyclerView.Adapter<MainCategoryAdapter.MainCategoryHolder>{
    Context context;
    ArrayList<MainCategoryModels> mclist;

    public MainCategoryAdapter(Context context, ArrayList<MainCategoryModels> mclist) {
        this.context = context;
        this.mclist = mclist;
    }

    @NonNull
    @Override
    public MainCategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mcview = LayoutInflater.from(context).inflate(R.layout.main_category, null);
        return new MainCategoryHolder(mcview);
    }

    @Override
    public void onBindViewHolder(@NonNull MainCategoryHolder holder, int position) {
        MainCategoryModels mcmodls = mclist.get(position);
        holder.mcBinding.mcicon.setImageResource(mcmodls.getIcon());
        holder.mcBinding.mcname.setText(mcmodls.getName());
        holder.mcBinding.clicklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mcmodls.getName().equalsIgnoreCase("Spin")){
                    context.startActivity(new Intent(context, SpinActivity.class));

                }else if (mcmodls.getName().equalsIgnoreCase("Scratch")){
                    context.startActivity(new Intent(context, ScratchActivity.class));

                }else if (mcmodls.getName().equalsIgnoreCase("Lucky")){
                    context.startActivity(new Intent(context, LuckyNumberActivity.class));

                }else if (mcmodls.getName().equalsIgnoreCase("Quiz")){
                    context.startActivity(new Intent(context, QuizListActivity.class));

                }else if (mcmodls.getName().equalsIgnoreCase("Rank")){
                    context.startActivity(new Intent(context, RankActivity.class));

                }else if (mcmodls.getName().equalsIgnoreCase("Withdraw")){
                    context.startActivity(new Intent(context, WithdrawActivity.class));

                }else if (mcmodls.getName().equalsIgnoreCase("Profile")){
                    context.startActivity(new Intent(context, ProfileActivity.class));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mclist.size();
    }

    public class MainCategoryHolder extends RecyclerView.ViewHolder {
        MainCategoryBinding mcBinding;
        public MainCategoryHolder(@NonNull View itemView) {
            super(itemView);
            mcBinding = MainCategoryBinding.bind(itemView);
        }
    }
}
