package edu.vt.cs3714.spring2023.challenge_05

import android.app.Application
import android.media.MediaPlayer
import android.media.SoundPool
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        const val TAG = "C05 TAG"
        const val USERNAME = "CS3714"
        const val URL = "https://eov4iiwzc9ht4rn.m.pipedream.net"
        const val ROUTE = "/"
    }

    val selectedMusic = MutableLiveData<String?>()

    val selectedSoundEffect1 = MutableLiveData<String?>()
    val delayForSoundEffect1 = MutableLiveData<Int?>()

    val selectedSoundEffect2 = MutableLiveData<String?>()
    val delayForSoundEffect2 = MutableLiveData<Int?>()

    val selectedSoundEffect3 = MutableLiveData<String?>()
    val delayForSoundEffect3 = MutableLiveData<Int?>()

    private var mediaPlayer: MediaPlayer? = null
    private val soundPool = SoundPool.Builder().setMaxStreams(3).build()
    private val soundEffectStreams = mutableListOf<Int>()

    private val client = OkHttpClient()

    fun startPlayback() {
        mediaPlayer?.stop()
        playSelectedMusic()


        soundEffectStreams.clear()


        playSoundEffect(selectedSoundEffect1.value, delayForSoundEffect1.value)
        playSoundEffect(selectedSoundEffect2.value, delayForSoundEffect2.value)
        playSoundEffect(selectedSoundEffect3.value, delayForSoundEffect3.value)
    }

    private fun playSelectedMusic() {
        val musicName = selectedMusic.value ?: return
        val musicResourceId = getResourceIdForName(musicName)

        if (musicResourceId != null) {
            mediaPlayer?.release()

            mediaPlayer = MediaPlayer.create(getApplication<Application>().applicationContext, musicResourceId)

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }

            mediaPlayer?.setOnErrorListener { mp, what, extra ->
                true
            }

            mediaPlayer?.start()
        }
    }

    fun togglePlayPause() {
        if (mediaPlayer?.isPlaying == true) {
            mediaPlayer?.pause()
            soundEffectStreams.forEach { streamId ->
                soundPool.pause(streamId)
            }
            trackEvent("pause")
        } else {
            mediaPlayer?.start()
            soundEffectStreams.forEach { streamId ->
                soundPool.resume(streamId)
            }
            trackEvent("play")
        }
    }

    fun restartPlayback() {
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()

        soundEffectStreams.forEach { streamId ->
            soundPool.stop(streamId)
        }
        soundEffectStreams.clear()

        playSoundEffect(selectedSoundEffect1.value, delayForSoundEffect1.value)
        playSoundEffect(selectedSoundEffect2.value, delayForSoundEffect2.value)
        playSoundEffect(selectedSoundEffect3.value, delayForSoundEffect3.value)

        trackEvent("reset")
    }

    private fun getResourceIdForName(resourceName: String): Int? {
        return try {
            val packageName = "edu.vt.cs3714.spring2023.challenge_05"
            val resourceId = getApplication<Application>().resources.getIdentifier(resourceName, "raw", packageName)
            if (resourceId == 0) null else resourceId
        } catch (e: Exception) {
            null
        }
    }

    fun playSoundEffect(resourceName: String?, delay: Int?) {
        resourceName?.let { name ->
            val resourceId = getResourceIdForName(name)
            resourceId?.let {
                val soundId = soundPool.load(getApplication<Application>().applicationContext, it, 1)
                soundPool.setOnLoadCompleteListener { _, id, _ ->
                    Handler(Looper.getMainLooper()).postDelayed({
                        val streamId = soundPool.play(id, 1.0f, 1.0f, 0, 0, 1.0f)
                        soundEffectStreams.add(streamId)
                    }, delay?.toLong() ?: 0)
                }
            }
        }
    }

    private fun trackEvent(eventType: String) {
        val completeURL = "$URL$ROUTE?event=$eventType&username=$USERNAME"

        val request = Request.Builder()
            .url(completeURL)
            .build()

        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.close()
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer?.release()
        mediaPlayer = null
        soundPool.release()
    }
}
