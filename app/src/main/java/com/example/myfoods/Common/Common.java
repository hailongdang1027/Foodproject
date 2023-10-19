package com.example.myfoods.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.myfoods.Model.User;
import com.example.myfoods.Remote.APIService;
import com.example.myfoods.Remote.RetrofitClient;

public class Common {

    public static String topicName = "News";
    public static User currentUser;

    public static String PHONE_TEXT = "userPhone";

    public static final String INTENT_FOOD_ID = "FoodID";

    private static final String BASE_URL = "https://fcm.googleapis.com/";
    private static final String GOOGLE_API_URL = "https://maps.googleapis.com/";

    public static APIService getFCMService(){
        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }


    public static String convertCodeToStatus(String status) {
        if (status.equals("0")){
            return "Ordered";
        }else if (status.equals("1")){
            return "On my way";
        }else {
            return "Shipped";
        }
    }

    public static final String DELETE = "Delete";
    public static final String USER_KEY = "User";
    public static final String PASS_KEY = "Password";

    public static boolean isConnectToInternet(Context context){
        //note
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null){
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            if (networkInfos != null){
                for (int i = 0; i < networkInfos.length; i++){
                    if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
}
