package com.example.myfoods.ViewHolder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Database.Database;
import com.example.myfoods.DetailFood;
import com.example.myfoods.FoodList;
import com.example.myfoods.Interface.ItemClickListener;
import com.example.myfoods.Model.Favorites;
import com.example.myfoods.Model.Food;
import com.example.myfoods.Model.Order;
import com.example.myfoods.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder> {

    private Context context;
    private List<Favorites> favoritesList;

    public FavoritesAdapter(Context context, List<Favorites> favoritesList) {
        this.context = context;
        this.favoritesList = favoritesList;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.favorites_item, parent, false);
        return new FavoritesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder holder, int position) {
        holder.txtFoodName.setText(favoritesList.get(position).getFoodName());
        holder.txtFoodPrice.setText(String.format("$ %s", favoritesList.get(position).getFoodPrice().toString()));
        Picasso.with(context).load(favoritesList.get(position).getFoodImage())
                .into(holder.imageFood);


        boolean isExists = new Database(context).checkFoodExists(favoritesList.get(position).getFoodID(), Common.currentUser.getPhone());
        holder.quickCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isExists){
                    new Database(context).addToCart(new Order(
                            Common.currentUser.getPhone(),
                            favoritesList.get(position).getFoodID(),
                            favoritesList.get(position).getFoodName(),
                            "1",
                            favoritesList.get(position).getFoodPrice(),
                            favoritesList.get(position).getFoodDiscount(),
                            favoritesList.get(position).getFoodImage()
                    ));
                }else {
                    new Database(context).increaseCart(Common.currentUser.getPhone(), favoritesList.get(position).getFoodID());
                }
                Toast.makeText(context, "Added to Favorites", Toast.LENGTH_SHORT).show();
            }
        });


        final Favorites local = favoritesList.get(position);
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {

                Intent detailFood = new Intent(context, DetailFood.class);
                detailFood.putExtra("FoodID", favoritesList.get(position).getFoodID());
                context.startActivity(detailFood);
            }
        });
    }

    @Override
    public int getItemCount() {
        return favoritesList.size();
    }

    public void removeItem(int pos){
        favoritesList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void restoreItem(Favorites item, int pos){
        favoritesList.add(pos, item);
        notifyItemInserted(pos);
    }

    public Favorites getItem(int pos){
        return favoritesList.get(pos);
    }

}
