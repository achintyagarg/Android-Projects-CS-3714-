package com.example.hw4hokiecomposer

import retrofit2.Call
import com.google.gson.GsonBuilder
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackerRetrofitService {
    @POST(value = PlayFragment.ROUTE)
    fun postLog(@Body json: JSONObject): Call<String>


    companion object {
        fun create(baseUrl: String): TrackerRetrofitService {

            val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .baseUrl(baseUrl)
                .build()

            return retrofit.create(TrackerRetrofitService::class.java)
        }
    }
}