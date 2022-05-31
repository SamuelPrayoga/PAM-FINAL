package com.project.delcanteen.app

import com.project.delcanteen.model.ResponModel
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name :String,
        @Field("email") email :String,
        @Field("phone") phone :String,
        @Field("noktp") noktp :String,
        @Field("password") password :String
    ):Call<ResponModel>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email :String,
        @Field("password") password :String
    ):Call<ResponModel>

    @GET("produk")
    fun getProduk(): Call<ResponModel>

    @GET("provinsi")
    fun getProvinsi(): Call<ResponModel>

    @GET("kota")
    fun getKota(
        @Query("id_provinsi") id: Int
    ): Call<ResponModel>

    @GET("kecamatan")
    fun getKecamatan(
        @Query("id_kota") id: Int
    ): Call<ResponModel>

}