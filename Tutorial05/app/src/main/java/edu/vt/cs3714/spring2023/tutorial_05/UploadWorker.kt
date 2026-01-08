package edu.vt.cs3714.spring2023.tutorial_05

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val event = inputData.getString("event") ?: return Result.failure()
        return try {
            val json = JSONObject().apply {
                put("username", MainActivity.USERNAME)
                put("event", event)
            }
            makeRequest(json)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

    private fun makeRequest(json: JSONObject): Boolean {
        val url = URL(MainActivity.URL + MainActivity.ROUTE)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        val bytes = json.toString().toByteArray()
        connection.outputStream.use {
            it.write(bytes)
        }

        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            return true
        }
        return false
    }
}
