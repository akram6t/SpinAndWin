package com.spinandwin.quizme.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.databinding.RankBinding;
import com.spinandwin.quizme.databinding.RankBottomshitLayoutBinding;
import com.spinandwin.quizme.models.RankModels;
import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.RankHolder>{
    Context context;
    ArrayList<RankModels> rankList;
    BottomSheetDialog bottomSheetDialog;
    RankBottomshitLayoutBinding rankBottomshitLayoutBinding;

    public RankAdapter(Context context, ArrayList<RankModels> rankList, BottomSheetDialog bottomSheetDialog, RankBottomshitLayoutBinding rankBottomshitLayoutBinding) {
        this.context = context;
        this.rankList = rankList;
        this.bottomSheetDialog = bottomSheetDialog;
        this.rankBottomshitLayoutBinding = rankBottomshitLayoutBinding;
    }

    @NonNull
    @Override
    public RankHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rankView = LayoutInflater.from(context).inflate(R.layout.rank, parent, false);
        return new RankHolder(rankView);
    }

    @Override
    public void onBindViewHolder(@NonNull RankHolder holder, int position) {
        RankModels rankModels = rankList.get(position);
        holder.rankBinding.rankPoint.setText(String.valueOf(rankModels.getPoint()));
        holder.rankBinding.rankName.setText(rankModels.getName());
        Glide.with(context).load(rankModels.getProfile())
                .placeholder(R.drawable.profile)
                .into(holder.rankBinding.rankProfile);
        if (rankModels.getPos().length()==1){
            holder.rankBinding.rankPosition.setText("#0"+rankModels.getPos());
        }else {
            holder.rankBinding.rankPosition.setText("#"+rankModels.getPos());
        }

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(rankModels.getUid())){
            holder.rankBinding.clickprofile.setBackgroundColor(ContextCompat.getColor(context, R.color.light_orange));
        }

        holder.rankBinding.clickprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rankBottomshitLayoutBinding.textrankbottomshit.setText(rankModels.getName());
                Glide.with(context).load(rankModels.getProfile()).placeholder(R.drawable.profile)
                        .into(rankBottomshitLayoutBinding.rankshitprofile);
                bottomSheetDialog.show();
            }
        });
        rankBottomshitLayoutBinding.btnrankshitclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

    }

    @Override
    public int getItemCount() {
        return rankList.size();
    }

    public class RankHolder extends RecyclerView.ViewHolder {
        RankBinding rankBinding;
        public RankHolder(@NonNull View itemView) {
            super(itemView);
            rankBinding = RankBinding.bind(itemView);
        }
    }
}
