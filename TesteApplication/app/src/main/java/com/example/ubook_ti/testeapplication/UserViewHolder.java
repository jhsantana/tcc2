package com.example.ubook_ti.testeapplication;

/**
 * Created by UBOOK-TI on 20/07/2016.
 */

import android.widget.TextView;

import  android.support.v7.widget.RecyclerView;
import android.view.View;

public class UserViewHolder extends RecyclerView.ViewHolder {

    public TextView text1;
    public TextView text2;

    public UserViewHolder(View itemView) {
        super(itemView);

        text1 = (TextView) itemView.findViewById(android.R.id.text1);
        text2 = (TextView) itemView.findViewById(android.R.id.text2);
    }
}