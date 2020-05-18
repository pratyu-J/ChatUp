package com.example.chatup.Fragments;

import com.example.chatup.Notification.MyResponse;
import com.example.chatup.Notification.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAixyfRt0:APA91bEAn0bs1bYq_TRVx_Au1Vjo85KZXzOnSAABlH72VoQtBNnW89Qj4QJzLDDzK8ET93d3jr0c4Omm9J3kI5sYCE0Iz2IdkeWSHQFMN1zF6K32A_RswtqmFEIui-iIoehaJ68j9mTp"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
