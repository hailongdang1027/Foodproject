package com.example.myfoods.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.myfoods.Common.Common;
import com.example.myfoods.Interface.ItemClickListener;
import com.example.myfoods.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        , View.OnCreateContextMenuListener {
    public TextView textCartName, textPrice;
    public ElegantNumberButton btnQuantity;
    public ImageView cartImage;

    public RelativeLayout viewBackground;
    public LinearLayout viewForeground;

    private ItemClickListener itemClickListener;

    public void setTextCartName(TextView textCartName){
        this.textCartName = textCartName;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        textCartName = (TextView) itemView.findViewById(R.id.cart_itemName);
        textPrice = (TextView) itemView.findViewById(R.id.cart_itemPrice);
        btnQuantity = (ElegantNumberButton) itemView.findViewById(R.id.btn_quantity);
        cartImage = (ImageView) itemView.findViewById(R.id.cart_image);
        viewBackground = (RelativeLayout) itemView.findViewById(R.id.view_background_del);
        viewForeground = (LinearLayout) itemView.findViewById(R.id.view_foreground);
        itemView.setOnCreateContextMenuListener(this);
    }


    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
        contextMenu.setHeaderTitle("Select action");
        contextMenu.add(0, 0, getAdapterPosition(), Common.DELETE);
    }
}