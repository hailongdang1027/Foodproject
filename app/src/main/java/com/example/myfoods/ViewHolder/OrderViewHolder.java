package com.example.myfoods.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoods.Interface.ItemClickListener;
import com.example.myfoods.R;

public class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView textOrderId, textOrderStatus, textOrderPhone, textOrderAddress;

    private ItemClickListener itemClickListener;

    public ImageView btnDelete;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
        textOrderAddress = (TextView) itemView.findViewById(R.id.order_address);
        textOrderId = (TextView) itemView.findViewById(R.id.order_id);
        textOrderStatus = (TextView) itemView.findViewById(R.id.order_status);
        textOrderPhone = (TextView) itemView.findViewById(R.id.order_phone);

        btnDelete = (ImageView) itemView.findViewById(R.id.btn_delete_order);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }
}
