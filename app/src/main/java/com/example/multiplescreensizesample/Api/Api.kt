package com.example.multiplescreensizesample.Api

import LoginResponse
import com.squareup.okhttp.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.Map;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;


interface Api {
    @FormUrlEncoded
    @POST("login")
    fun userLogin(
        @Field("email") email:String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("images/upload_image.php")
    fun upload(
        @Header("Authorization") authorization: String?,
        @PartMap map: Map<String?, RequestBody?>?
    ): Call<ServerResponse?>?
}