package com.example.myfoods.Service;


import android.util.Log;

import androidx.annotation.NonNull;

import com.example.myfoods.Common.Common;
import com.example.myfoods.Model.Token;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseService extends FirebaseMessagingService {
    @Override
//    public void onTokenRefresh() {
//        super.onTokenRefresh();
//        String refreshToken = FirebaseInstanceId.getInstance().getToken();
//        updateTokenToFirebase(refreshToken);
//    }
//
//    private void updateTokenToFirebase(String refreshedToken) {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference tokens = database.getReference("Tokens");
//        Token token = new Token(refreshedToken, false);
//        tokens.child(Common.currentUser.getPhone()).setValue(token);
//    }
    public void onNewToken(String token) {
        super.onNewToken(token);
        updateTokenToFirebase(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle incoming messages here if needed
    }

    private void updateTokenToFirebase(String token) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference tokens = database.getReference("Tokens");
        Token tokenObject = new Token(token, true);
        tokens.child(Common.currentUser.getPhone()).setValue(tokenObject);
    }
}


