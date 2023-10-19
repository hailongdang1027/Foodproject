package com.example.myfoods.Remote;

import com.example.myfoods.Model.DataMessage;
import com.example.myfoods.Model.MyResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAyMkDOEY:APA91bHXeX9v2IaIyrmEIz2lwCKhnOpHntK8aaaNjMUNKIBveKOO9c75xflppfDbxjTzDOKIVy0QB9_boNY6qM1Fjx8pij0qwJhyqo1Aq87XJhJvp2kgfUiKpWFgZOP-7r7sJCgkaVvK"
            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body DataMessage body);
}
