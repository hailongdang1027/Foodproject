package com.example.myfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Database.Database;
import com.example.myfoods.Interface.ItemClickListener;
import com.example.myfoods.Model.Category;
import com.example.myfoods.Model.Favorites;
import com.example.myfoods.Model.Food;
import com.example.myfoods.Model.Order;
import com.example.myfoods.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FoodList extends AppCompatActivity {
    RecyclerView recyclerFood;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference foodList;
    String categoryId = "";
    FirebaseRecyclerAdapter<Food, FoodViewHolder>adapter;

    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    Database localDB;


    SwipeRefreshLayout swipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.holo_green_dark, R.color.holo_blue_dark, R.color.holo_orange_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (getIntent() != null){
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null){

                    if (Common.isConnectToInternet(getBaseContext())){
                        loadListFood(categoryId);
                    }else {
                        Toast.makeText(FoodList.this, "Check the connection!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (getIntent() != null){
                    categoryId = getIntent().getStringExtra("CategoryId");
                }
                if (!categoryId.isEmpty() && categoryId != null){

                    if (Common.isConnectToInternet(getBaseContext())){
                        loadListFood(categoryId);
                    }else {
                        Toast.makeText(FoodList.this, "Check the connection!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                materialSearchBar = (MaterialSearchBar) findViewById(R.id.search_bar);
                materialSearchBar.setHint("Enter your food");
                loadSuggest();

                materialSearchBar.setCardViewElevation(10);
                materialSearchBar.addTextChangeListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        List<String> suggest = new ArrayList<String>();
                        for (String search : suggestList){
                            if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())){
                                suggest.add(search);
                            }
                        }
                        materialSearchBar.setLastSuggestions(suggest);
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                    @Override
                    public void onSearchStateChanged(boolean enabled) {
                        //When SearchBar is close
                        //Restore original adapter
                        if (!enabled){
                            recyclerFood.setAdapter(adapter);
                        }
                    }

                    @Override
                    public void onSearchConfirmed(CharSequence text) {
                        //When search finish
                        //Show result of search adapter
                        startSearch(text);
                    }

                    @Override
                    public void onButtonClicked(int buttonCode) {

                    }
                });

            }
        });

        database = FirebaseDatabase.getInstance();
        foodList = database.getReference("Foods");

        localDB = new Database(this);
        recyclerFood = (RecyclerView) findViewById(R.id.recycler_food);
        recyclerFood.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerFood.setLayoutManager(layoutManager);
        if (getIntent() != null){
            categoryId = getIntent().getStringExtra("CategoryId");
        }
        if (!categoryId.isEmpty() && categoryId != null){

            if (Common.isConnectToInternet(getBaseContext())){
                loadListFood(categoryId);
            }else {
                Toast.makeText(FoodList.this, "Check the connection!", Toast.LENGTH_SHORT).show();
                return;
            }
        }


    }

    private void startSearch(CharSequence text) {
        Query searchByName = foodList.orderByChild("name").equalTo(text.toString());
        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();
        searchAdapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, int position, @NonNull Food food) {
                foodViewHolder.txtFoodName.setText(food.getName());
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.imageFood);
                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent detailFood = new Intent(FoodList.this, DetailFood.class);
                        detailFood.putExtra("FoodID", searchAdapter.getRef(position).getKey());
                        startActivity(detailFood);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }
        };
        searchAdapter.startListening();
        recyclerFood.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot : snapshot.getChildren()){
                    Food itemFood = postSnapshot.getValue(Food.class);
                    suggestList.add(itemFood.getName());
                }
                materialSearchBar.setLastSuggestions(suggestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListFood(String categoryId) {
        Query searchByName = foodList.orderByChild("menuId").equalTo(categoryId);
        FirebaseRecyclerOptions<Food> foodOptions = new FirebaseRecyclerOptions.Builder<Food>()
                .setQuery(searchByName, Food.class)
                .build();


        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(foodOptions) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder foodViewHolder, @SuppressLint("RecyclerView") int position, @NonNull Food food) {
                foodViewHolder.txtFoodName.setText(food.getName());
                foodViewHolder.txtFoodPrice.setText(String.format("$ %s", food.getPrice().toString()));
                Picasso.with(getBaseContext()).load(food.getImage())
                        .into(foodViewHolder.imageFood);


                boolean isExists = new Database(getBaseContext()).checkFoodExists(adapter.getRef(position).getKey(), Common.currentUser.getPhone());
                foodViewHolder.quickCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!isExists){
                            new Database(getBaseContext()).addToCart(new Order(
                                    Common.currentUser.getPhone(),
                                    adapter.getRef(position).getKey(),
                                    food.getName(),
                                    "1",
                                    food.getPrice(),
                                    food.getDiscount(),
                                    food.getImage()
                            ));
                        }else {
                            new Database(getBaseContext()).increaseCart(Common.currentUser.getPhone(), adapter.getRef(position).getKey());
                        }
                        Toast.makeText(FoodList.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    }
                });



                if (localDB.isFavorites(adapter.getRef(position).getKey(), Common.currentUser.getPhone())){
                    foodViewHolder.imageFavorites.setImageResource(R.drawable.ic_baseline_favorite_24);
                }

                //Click Share
                foodViewHolder.shareImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                });



                foodViewHolder.imageFavorites.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Favorites favorites = new Favorites();

                        favorites.setFoodID(adapter.getRef(position).getKey());
                        favorites.setUserPhone(Common.currentUser.getPhone());
                        favorites.setFoodName(food.getName());
                        favorites.setFoodDescription(food.getDescription());
                        favorites.setFoodDiscount(food.getDiscount());
                        favorites.setFoodImage(food.getImage());
                        favorites.setFoodMenuId(food.getMenuId());


                        if (!localDB.isFavorites(adapter.getRef(position).getKey(), Common.currentUser.getPhone())){
                            localDB.addFavorites(favorites);
                            foodViewHolder.imageFavorites.setImageResource(R.drawable.ic_baseline_favorite_24);
                            Toast.makeText(FoodList.this, "" + food.getName() + " was add to Favorites", Toast.LENGTH_SHORT).show();
                        }else {
                            localDB.removeFavorites(adapter.getRef(position).getKey(), Common.currentUser.getPhone());
                            foodViewHolder.imageFavorites.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            Toast.makeText(FoodList.this, "" + food.getName() + " was removed to Favorites", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final Food local = food;
                foodViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent detailFood = new Intent(FoodList.this, DetailFood.class);
                        detailFood.putExtra("FoodID", adapter.getRef(position).getKey());
                        startActivity(detailFood);
                    }
                });
            }

            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(itemView);
            }
        };

        adapter.startListening();

        recyclerFood.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null){
            adapter.startListening();
        }
        loadListFood(categoryId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null && searchAdapter != null){
            adapter.stopListening();
            searchAdapter.stopListening();
        }

    }

}