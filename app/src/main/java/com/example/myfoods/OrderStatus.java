package com.example.myfoods;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Interface.ItemClickListener;
import com.example.myfoods.Model.RequestAdd;
import com.example.myfoods.ViewHolder.FoodViewHolder;
import com.example.myfoods.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OrderStatus extends AppCompatActivity {

    public RecyclerView recyclerOrder;
    public RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<RequestAdd, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database = FirebaseDatabase.getInstance();
        requests = database.getReference("Requests");

        recyclerOrder = (RecyclerView) findViewById(R.id.list_orders);
        recyclerOrder.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerOrder.setLayoutManager(layoutManager);

        if (getIntent() != null){
            loadOrders(Common.currentUser.getPhone());
        }else {
            loadOrders(getIntent().getStringExtra("userPhone"));
        }



    }

    private void loadOrders(String phone) {
        Query getOrderByUser = requests.orderByChild("phone").equalTo(phone);

        FirebaseRecyclerOptions<RequestAdd> orderOptions = new FirebaseRecyclerOptions.Builder<RequestAdd>()
                .setQuery(getOrderByUser, RequestAdd.class)
                .build();
        adapter = new FirebaseRecyclerAdapter<RequestAdd, OrderViewHolder>(orderOptions) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, @SuppressLint("RecyclerView") int pos, @NonNull RequestAdd requestAdd) {
                orderViewHolder.textOrderId.setText(adapter.getRef(pos).getKey());
                orderViewHolder.textOrderStatus.setText(Common.convertCodeToStatus(requestAdd.getStatus()));
                orderViewHolder.textOrderAddress.setText(requestAdd.getAddress());
                orderViewHolder.textOrderPhone.setText(requestAdd.getPhone());
                orderViewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (adapter.getItem(pos).getStatus().equals("0")){
                            deleteOrder(adapter.getRef(pos).getKey());
                        }else {
                            Toast.makeText(OrderStatus.this, "Cannot delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                orderViewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                    }
                });
            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolder(itemView);
            }
        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerOrder.setAdapter(adapter);
    }

    private void deleteOrder(String key) {
        requests.child(key)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(OrderStatus.this, new StringBuilder("Order").append(key).append(" be deleted").toString(), Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OrderStatus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
        }

    }
}