package com.example.hw4hokiecomposer

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import org.json.JSONObject

class UploadWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val json = JSONObject()
        json.put("userID", inputData.getString("userID"))
        json.put("event", inputData.getString("event"))

        return uploadLog(json, PlayFragment.URL)
    }

    private fun uploadLog(json: JSONObject, url: String): Result {
        var call = TrackerRetrofitService.create(url).postLog(json)
        var response = call.execute()

        if (response.isSuccessful) {
            return Result.success()
        } else {
            if (response.code() in (500..599)) {
                return Result.retry()
            }
            return Result.failure()
        }
    }
}