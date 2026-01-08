package edu.vt.cs3714.spring2023.challenge_05

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val tag = inputData.getString("tag") ?: "DEFAULT_TAG"
        val url = inputData.getString("url") ?: ""

        val json = JSONObject()
        json.accumulate("username", inputData.getString("username"))
        json.accumulate("event", inputData.getString("event"))
        Log.d(tag, "params:$json url: $url")
        return uploadLog(json, url, tag)
    }

    fun uploadLog(json: JSONObject, url: String, tag: String): Result {
        Log.d(tag, "uploadLog() $url")
        try {
            var call = TrackerService.create(url).postLog(json)
            var response = call.execute()

            if (response.isSuccessful) {
                Log.d(tag, "Log uploaded successfully: ${response.body()}")
                return Result.success()
            } else {
                Log.e(tag, "Failed to upload log: ${response.errorBody()?.string()}")
                return Result.failure()
            }
        } catch (e: Exception) {
            Log.e(tag, "Error uploading log", e)
            return Result.retry()
        }
    }
}
