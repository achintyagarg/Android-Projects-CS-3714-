package edu.vt.cs3714.spring2023.challenge_05

import android.app.*
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log

class MusicService : Service() {

    override fun onBind(intent: Intent?): IBinder {
        return iBinder
    }

    private val CHANNEL_ID = "Music Service Channel ID"

    companion object {
        const val COMPLETE_INTENT = "complete intent"
        const val MUSICNAME = "music name"
    }

    private var musicPlayer: MusicPlayer? = null
    private val iBinder = MyBinder()

    inner class MyBinder : Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    override fun onCreate() {
        super.onCreate()
        musicPlayer = MusicPlayer(this)
    }

    fun startMusic() {
        musicPlayer?.playMusic()
    }

    fun pauseMusic() {
        musicPlayer?.pauseMusic()
    }

    fun resumeMusic() {
        musicPlayer?.resumeMusic()
    }

    fun getPlayingStatus(): Int {
        return musicPlayer?.getMusicStatus() ?: -1
    }

    fun onUpdateMusicName(musicName: String) {
        Log.i("updating music name", musicName)
        val intent = Intent(COMPLETE_INTENT)
        intent.putExtra(MUSICNAME, musicName)
        sendBroadcast(intent)
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        createNotificationChannel()

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)
        }

        val notification: Notification = Notification.Builder(this, CHANNEL_ID)
            .setContentTitle("Music")
            .setContentText("The music service has started")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(123, notification)
        return Service.START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Music Service",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }
}
