package com.spinandwin.quizme.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.spinandwin.quizme.R;

public class Loading {
    Context context;
    AlertDialog dialog;
    public Loading(Context context){
        this.context = context;
    }

    public void create(){
        View loadingView = LayoutInflater.from(context).inflate(R.layout.loading_dialog_layout, null);
        dialog = new AlertDialog.Builder(context).setView(loadingView)
                .setCancelable(false).create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void show(){
        dialog.show();
    }
    public void dismiss(){
        dialog.dismiss();
    }

}
