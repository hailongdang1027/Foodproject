package com.example.myfoods.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoods.Interface.ItemClickListener;
import com.example.myfoods.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtFoodName, txtFoodPrice;
    public ImageView imageFood, imageFavorites, shareImage, quickCart;
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);
        txtFoodName = (TextView) itemView.findViewById(R.id.food_name);
        txtFoodPrice = (TextView)itemView.findViewById(R.id.food_price);
        imageFood = (ImageView) itemView.findViewById(R.id.food_image);
        imageFavorites = (ImageView) itemView.findViewById(R.id.fav);
        shareImage = (ImageView) itemView.findViewById(R.id.share_image);
        quickCart = (ImageView) itemView.findViewById(R.id.btn_quick_cart);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
