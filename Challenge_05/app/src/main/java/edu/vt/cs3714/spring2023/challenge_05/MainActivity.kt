package edu.vt.cs3714.spring2023.challenge_05

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import edu.vt.cs3714.spring2023.challenge_05.databinding.ActivityMainBinding
import edu.vt.cs3714.spring2023.challenge_05.databinding.FragmentEditBinding
import androidx.work.workDataOf


class MainActivity : AppCompatActivity() {

    companion object {
        const val TAG = "C05 TAG"
        const val USERNAME = "CS3714"
        const val URL = "https://eov4iiwzc9ht4rn.m.pipedream.net"
        const val ROUTE = "/"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun appendEvent(event: String) {
        val eventData = workDataOf(
            "username" to USERNAME,
            "event" to event,
            "tag" to TAG,
            "url" to URL
        )

        val uploadWorkRequest = OneTimeWorkRequestBuilder<UploadWorker>()
            .setInputData(eventData)
            .build()

        WorkManager.getInstance(applicationContext)
            .beginUniqueWork(TAG, ExistingWorkPolicy.KEEP, uploadWorkRequest)
            .enqueue()
    }
}
