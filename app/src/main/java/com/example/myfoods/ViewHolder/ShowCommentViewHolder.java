package com.example.myfoods.ViewHolder;

import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoods.R;

public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
    public TextView textUserPhone, textComment;
    public RatingBar ratingBar;
    public ShowCommentViewHolder(@NonNull View itemView) {
        super(itemView);
        textComment = (TextView) itemView.findViewById(R.id.text_Comment);
        textUserPhone = (TextView) itemView.findViewById(R.id.text_userPhone);
        ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);

    }
}
