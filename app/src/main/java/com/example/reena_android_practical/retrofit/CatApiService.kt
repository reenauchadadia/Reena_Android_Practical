package com.example.reena_android_practical.retrofit

import com.example.reena_android_practical.model.CatImageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CatApiService {

    @GET("images/search")
    fun getCatImages(@Header("api_key") apiKey: String,@Query("limit") limit: Int): Call<ArrayList<CatImageModel>>
}