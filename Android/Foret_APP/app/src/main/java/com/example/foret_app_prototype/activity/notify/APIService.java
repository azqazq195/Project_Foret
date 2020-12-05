package com.example.foret_app_prototype.activity.notify;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAApZYwcIo:APA91bHRTpr7vt3RRq198o_j2cqCKrZs-ff6omgEGT7OjPDhTbv9-hWeXzUi0S5OkzjW86cCXoujTZ69Uk4lJEn94BDTY5Llq4DY0yhE7K7c9WIpTc8OsMqc_j0LJV7MIC1y-b6V2IRB"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);

}
