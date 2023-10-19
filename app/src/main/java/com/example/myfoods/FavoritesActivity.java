package com.example.myfoods;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.RelativeLayout;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Database.Database;
import com.example.myfoods.Helper.RecyclerItemTouchHelper;
import com.example.myfoods.Interface.RecyclerItemTouchHelperListener;
import com.example.myfoods.Model.Favorites;
import com.example.myfoods.Model.Order;
import com.example.myfoods.ViewHolder.FavoritesAdapter;
import com.example.myfoods.ViewHolder.FavoritesViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoritesActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {
    RecyclerView recyclerFavorites;
    RecyclerView.LayoutManager layoutManager;
    FavoritesAdapter adapter;
    RelativeLayout rootLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        rootLayout = (RelativeLayout) findViewById(R.id.root_layout);
        recyclerFavorites = (RecyclerView) findViewById(R.id.recycler_fav);
        layoutManager = new LinearLayoutManager(this);
        recyclerFavorites.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback itemCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerFavorites);
        
        loadFavorites();
    }

    private void loadFavorites() {
        adapter = new FavoritesAdapter(this, new Database(this).getAllFavorites(Common.currentUser.getPhone()));
        recyclerFavorites.setAdapter(adapter);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direc, int pos) {
        if (viewHolder instanceof FavoritesViewHolder){
            String name = ((FavoritesAdapter) recyclerFavorites.getAdapter()).getItem(pos).getFoodName();
            Favorites deleteItem = ((FavoritesAdapter) recyclerFavorites.getAdapter()).getItem(viewHolder.getAdapterPosition());
            int deleteIndex = viewHolder.getAdapterPosition();
            adapter.removeItem(viewHolder.getAdapterPosition());
            new Database(getBaseContext()).removeFavorites(deleteItem.getFoodID(), Common.currentUser.getPhone());
            Snackbar snackbar = Snackbar.make(rootLayout, name + " removed", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addFavorites(deleteItem);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}