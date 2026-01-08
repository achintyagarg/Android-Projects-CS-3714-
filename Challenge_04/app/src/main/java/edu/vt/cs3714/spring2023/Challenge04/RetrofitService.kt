package edu.vt.cs3714.spring2023.Challenge04

import com.google.gson.GsonBuilder
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface  RetrofitService {

    @GET("movie/now_playing?language=en-US")
    fun getNowPlaying(@Query("api_key") api_key: String, @Query("page") page: Int ): Observable<Movies>

    companion object {
        fun create(baseUrl: String): RetrofitService {

            val fit = Retrofit.Builder().addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create()))
                .baseUrl(baseUrl)
                .build()

            return fit.create(RetrofitService::class.java)
        }
    }
}