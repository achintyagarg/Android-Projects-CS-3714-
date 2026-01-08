package edu.vt.cs3714.spring2023.challenge_05

import com.google.gson.GsonBuilder
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface TrackerService{
    @POST (value = MainActivity.ROUTE)
    fun postLog (@Body json: JSONObject): Call<String>

    companion object {
        fun create (baseUrl: String) : TrackerService{

            val retrofit = Retrofit.Builder().
            addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().
                    setLenient().
                    create ()
                )
            )
                .baseUrl (baseUrl)
                .build()

            return retrofit.create (TrackerService::class.java)
        }
    }
}