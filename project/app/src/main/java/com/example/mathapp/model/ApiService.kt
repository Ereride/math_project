package com.example.mathapp.model

import retrofit2.Call
import retrofit2.http.GET


interface ApiService {
    @GET("random/trivia?json")
    fun getRandomTrivia(): Call<NumberFact>

    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .baseUrl("http://numbersapi.com/")
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}