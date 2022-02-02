package com.spinandwin.quizme.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.spinandwin.quizme.R;
import com.spinandwin.quizme.activities.QuizActivity;
import com.spinandwin.quizme.databinding.QuzCategoryLayoutBinding;
import com.spinandwin.quizme.models.QuizListModels;
import java.util.ArrayList;

public class QuizListAdapters extends RecyclerView.Adapter<QuizListAdapters.QuizHolder>{
    Context context;
    ArrayList<QuizListModels> quizModelsArrayList;

    public QuizListAdapters(Context context, ArrayList<QuizListModels> quizModelsArrayList) {
        this.context = context;
        this.quizModelsArrayList = quizModelsArrayList;
    }

    @NonNull
    @Override
    public QuizHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View quizView = LayoutInflater.from(context).inflate(R.layout.quz_category_layout, null);
        return new QuizHolder(quizView);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizHolder holder, int position) {
        QuizListModels quizListModels = quizModelsArrayList.get(position);
        Glide.with(context).load(quizListModels.getLogo()).placeholder(R.drawable.logo_holder).into(holder.quizBinding.quizicon);
        holder.quizBinding.quizname.setText(quizListModels.getName());

        holder.quizBinding.qclicklayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                context.startActivity(new Intent(context, QuizActivity.class)
                        .putExtra(context.getString(R.string.quizcategory),
                                quizListModels.getCat_id()).putExtra(context.getString(R.string.category_name), quizListModels.getName()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return quizModelsArrayList.size();
    }

    public class QuizHolder extends RecyclerView.ViewHolder {
        QuzCategoryLayoutBinding quizBinding;
        public QuizHolder(@NonNull View itemView) {
            super(itemView);
            quizBinding = QuzCategoryLayoutBinding.bind(itemView);
        }
    }
}
