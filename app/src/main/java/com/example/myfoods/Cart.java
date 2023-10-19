package com.example.myfoods;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Database.Database;
import com.example.myfoods.Helper.RecyclerItemTouchHelper;
import com.example.myfoods.Interface.RecyclerItemTouchHelperListener;
import com.example.myfoods.Model.DataMessage;
import com.example.myfoods.Model.MyResponse;
import com.example.myfoods.Model.Order;
import com.example.myfoods.Model.RequestAdd;
import com.example.myfoods.Model.Token;
import com.example.myfoods.Remote.APIService;
import com.example.myfoods.ViewHolder.CartAdapter;
import com.example.myfoods.ViewHolder.CartViewHolder;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {


    RecyclerView recyclerCart;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    public TextView textTotalPrice;
    Button btnPlace;

    List<Order> cart = new ArrayList<>();

    CartAdapter adapter;
    APIService mService;


    EditText editAddress;
    MaterialEditText editComment;


    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        mService = Common.getFCMService();

        rootLayout = (RelativeLayout) findViewById(R.id.rootLayout);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");
        recyclerCart = (RecyclerView) findViewById(R.id.list_cart);
        recyclerCart.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerCart.setLayoutManager(layoutManager);

        ItemTouchHelper.SimpleCallback itemCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemCallback).attachToRecyclerView(recyclerCart);

        textTotalPrice = (TextView) findViewById(R.id.total_money);
        btnPlace = (Button) findViewById(R.id.btn_placeOrder);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cart.size() > 0) {
                    showAlertDialog();
                } else {
                    Toast.makeText(Cart.this, "Nothing in the cart!!", Toast.LENGTH_SHORT).show();
                }

            }
        });

        loadListFood();
    }


    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("More step");
        alertDialog.setMessage("Add address");

        LayoutInflater inflater = this.getLayoutInflater();
        View orderAddressComment = inflater.inflate(R.layout.order_address_comment, null);

        editAddress = (EditText) orderAddressComment.findViewById(R.id.edit_address);
        editComment = (MaterialEditText) orderAddressComment.findViewById(R.id.edit_comment);


        alertDialog.setView(orderAddressComment);
        alertDialog.setIcon(R.drawable.ic_baseline_shopping_cart_24);




        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                RequestAdd request = new RequestAdd(
                        Common.currentUser.getPhone(),
                        Common.currentUser.getName(),
                        editAddress.getText().toString(),
                        textTotalPrice.getText().toString(),
                        "0",
                        editComment.getText().toString(),
                        cart
                );
                String orderNumber = String.valueOf(System.currentTimeMillis());

                requests.child(orderNumber).setValue(request);
                new Database(getBaseContext()).cleanCart(Common.currentUser.getPhone());


                sendNotificationOrder(orderNumber);
//                Toast.makeText(Cart.this, "Thanks, Success", Toast.LENGTH_SHORT).show();
//                finish();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        alertDialog.show();
    }



    private void sendNotificationOrder(String orderNumber) {
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query dataQuery = tokens.orderByChild("serverToken").equalTo(true);
        dataQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Token serverToken = postSnapshot.getValue(Token.class);
//                    Notification notification = new Notification("MIREA", "You have new order " + orderNumber);
//                    Sender content = new Sender(serverToken.getToken(), notification);

                    Map<String, String> dataSend = new HashMap<>();
                    dataSend.put("title", "MIREA");
                    dataSend.put("message", "You have new order" +  orderNumber);
                    DataMessage dataMessage = new DataMessage(serverToken.getToken(), dataSend);

                    mService.sendNotification(dataMessage).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body() != null && response.body().success == 1) {
                                    Toast.makeText(Cart.this, "Thanks, Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(Cart.this, "Failed !", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e("ERROR", t.getMessage());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadListFood() {
        cart = new Database(this).getCarts(Common.currentUser.getPhone());
        adapter = new CartAdapter(cart, this);
        adapter.notifyDataSetChanged();
        recyclerCart.setAdapter(adapter);

        int total = 0;
        for (Order order : cart) {
            total += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en", "US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            textTotalPrice.setText(fmt.format(total));
        }
    }


    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE)) {
            deleteCart(item.getOrder());
        }
        return true;
    }

    private void deleteCart(int position) {
        cart.remove(position);
        new Database(this).cleanCart(Common.currentUser.getPhone());
        for (Order item : cart) {
            new Database(this).addToCart(item);
        }
        loadListFood();
    }


    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direc, int pos) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerCart.getAdapter()).getItem(viewHolder.getAdapterPosition()).getNameProduct();

            Order deleteItem = ((CartAdapter) recyclerCart.getAdapter()).getItem(viewHolder.getAdapterPosition());
            int deleteIndex = viewHolder.getAdapterPosition();

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).removeFromCart(deleteItem.getIdProduct(), Common.currentUser.getPhone());


                int total = 0;
                List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
                for (Order item : orders) {
                    total += (Integer.parseInt(deleteItem.getPrice())) * (Integer.parseInt(item.getQuantity()));
                }
                Locale locale = new Locale("en", "US");
                NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                textTotalPrice.setText(fmt.format(total));

                Snackbar snackbar = Snackbar.make(rootLayout, name + " removed", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.restoreItem(deleteItem, deleteIndex);
                        new Database(getBaseContext()).addToCart(deleteItem);

                        int total = 0;
                        List<Order> orders = new Database(getBaseContext()).getCarts(Common.currentUser.getPhone());
                        for (Order item : orders) {
                            total += (Integer.parseInt(deleteItem.getPrice())) * (Integer.parseInt(item.getQuantity()));
                        }
                        Locale locale = new Locale("en", "US");
                        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                        textTotalPrice.setText(fmt.format(total));

                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
        }
    }
}
