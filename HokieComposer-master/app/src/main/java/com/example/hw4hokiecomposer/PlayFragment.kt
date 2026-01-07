package com.example.hw4hokiecomposer


import android.content.*
import android.os.Bundle
import android.os.IBinder
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.android.synthetic.main.fragment_edit.*
import kotlinx.android.synthetic.main.fragment_play.*
import java.util.*
import kotlin.concurrent.timerTask


/**
 * A simple [Fragment] subclass.
 *
 */
class PlayFragment : Fragment(), View.OnClickListener {

    companion object {
        const val TAG = "HW4_TAG"
        const val USER_ID = "derekj11"
        const val URL = "https://posthere.io/"
        const val ROUTE = "7af8-4ea7-915c"
        const val INITIALIZE_STATUS = "initialization status"
        const val MUSIC_PLAYING = "music playing"
    }

    override fun onClick(v: View?) {
        if (isBound) {
            if (v == playButton) {
                when (musicService?.getPlayingStatus()) {
                    0 -> {
                        musicService?.startMusic()
                        playButton?.text = "Pause"
                        appendEvent("pressed play")
                        timer?.scheduleAtFixedRate(object : TimerTask() {
                            override fun run() {
                                counter++
                                playEffect(counter)
                            }
                        }, 1000L, 1000L)
                    }
                    1 -> {
                        musicService?.pauseMusic()
                        playButton?.text = "Resume"
                        appendEvent("pressed pause")
                        timer?.cancel()
                        timer = Timer()
                    }
                    2 -> {
                        musicService?.resumeMusic()
                        playButton?.text = "Pause"
                        appendEvent("pressed resume")
                        timer?.scheduleAtFixedRate(object : TimerTask() {
                            override fun run() {
                                counter++
                            }
                        }, 1000L, 1000L)
                    }
                }
            } else if (v == restartButton) {
                musicService?.restartMusic()
                appendEvent("pressed restart")
                counter = 0
                timer?.cancel()
                timer = Timer()
                timer?.scheduleAtFixedRate(object : TimerTask() {
                    override fun run() {
                        counter++
                    }
                }, 1000L, 1000L)
            }
        }
    }

    private val images = arrayOf(
        R.drawable.gotechgo,
        R.drawable.gtg2,
        R.drawable.gtg3,
        R.drawable.clapping,
        R.drawable.cheering,
        R.drawable.letsgohokies)

    var music: TextView? = null
    var playButton: Button? = null
    var restartButton: Button? = null
    var image: ImageView? = null
    var timer: Timer? = null
    var counter: Long = 0

    var song: String? = null
    var effect1: String? = null
    var position1: Int? = 0
    var effect2: String? = null
    var position2: Int? = 0
    var effect3: String? = null
    var position3: Int? = 0

    var songNum = 0
    var e1Num = 0
    var e2Num = 0
    var e3Num = 0

    var isBound = false
    var musicService: MusicService? = null
    var musicCompletionReceiver: MusicCompletionReceiver? = null
    var startMusicServiceIntent: Intent? = null
    var isInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play, container, false)
    }

    private val musicServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {

            val binder = iBinder as MusicService.MyBinder
            musicService = binder.getService()
            isBound = true
            when (musicService?.getPlayingStatus()) {
                0 -> playButton?.text = "Start"
                1 -> playButton?.text = "Pause"
                2 -> playButton?.text = "Resume"
            }
            musicService?.setSongs(songNum, e1Num, e2Num, e3Num)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {
            musicService = null
            isBound = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (isBound) {
            context?.unbindService(musicServiceConnection)
            isBound = false
        }
        context?.unregisterReceiver(musicCompletionReceiver)

        appendEvent("onPause")
    }

    override fun onResume() {
        super.onResume()
        if (isInitialized && !isBound) {
            context?.bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE)
        }
        context?.registerReceiver(musicCompletionReceiver, IntentFilter(MusicService.COMPLETE_INTENT))
        appendEvent("onResume")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        timer = Timer()
        song = this.arguments?.getString("song")
        effect1 = this.arguments?.getString("effect1")
        position1 = this.arguments?.getInt("position1")
        effect2 = this.arguments?.getString("effect2")
        position2 = this.arguments?.getInt("position2")
        effect3 = this.arguments?.getString("effect3")
        position3 = this.arguments?.getInt("position3")

        songName.text = song

        when (song) {
            "Go Tech Go!" -> {
                songNum = 0
            }
            "Go Tech Go!2" -> {
                songNum = 1
            }
            "Go Tech Go!3" -> {
                songNum = 2
            }
        }

        when (effect1) {
            "Clapping" -> {
                e1Num = 3
            }
            "Cheering" -> {
                e1Num = 4
            }
            "Go Hokies!" -> {
                e1Num = 5
            }
        }

        when (effect2) {
            "Clapping" -> {
                e2Num = 3
            }
            "Cheering" -> {
                e2Num = 4
            }
            "Go Hokies!" -> {
                e2Num = 5
            }
        }

        when (effect3) {
            "Clapping" -> {
                e3Num = 3
            }
            "Cheering" -> {
                e3Num = 4
            }
            "Go Hokies!" -> {
                e3Num = 5
            }
        }

        playButton = view.findViewById(R.id.playPauseButton)
        restartButton = view.findViewById(R.id.restartButton)
        music = view.findViewById(R.id.songName)
        playButton?.setOnClickListener(this)
        restartButton?.setOnClickListener(this)
        image = view.findViewById(R.id.songImage)

        if (savedInstanceState != null) {
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS)
            music?.text = savedInstanceState.getString(MUSIC_PLAYING)
        }
        startMusicServiceIntent = Intent(context, MusicService::class.java)
        if (!isInitialized) {
            context?.startService(startMusicServiceIntent)
            isInitialized = true
        }
        musicCompletionReceiver = MusicCompletionReceiver(this)
        musicService?.setSongs(songNum, e1Num, e2Num, e3Num)
        updatePic(song)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized)
        outState.putString(MUSIC_PLAYING, music?.text.toString())
        super.onSaveInstanceState(outState)
    }

    private fun appendEvent(event: String) {
        WorkManager.getInstance().beginUniqueWork(TAG, ExistingWorkPolicy.KEEP, OneTimeWorkRequestBuilder<UploadWorker>().setInputData(
            workDataOf("userID" to USER_ID, "event" to event)
        ).build()).enqueue()
    }

    fun updatePic(musicName: String?) {
        when (musicName) {
            "Go Tech Go" -> {
                image?.setImageResource(images[0])
            }
            "Go Tech Go 2" -> {
                image?.setImageResource(images[1])
            }
            "Go Tech Go 3" -> {
                image?.setImageResource(images[2])
            }
            "clapping" -> {
                image?.setImageResource(images[3])
            }
            "cheering" -> {
                image?.setImageResource(images[4])
            }
            "gohokies" -> {
                image?.setImageResource(images[5])
            }
        }
    }

    fun playEffect(counter: Long) {
        when (counter) {
            position1?.toLong() -> {
                musicService?.startEffect1()
                updatePic(effect1)
            }
            position2?.toLong() -> {
                musicService?.startEffect2()
                updatePic(effect2)
            }
            position3?.toLong() -> {
                musicService?.startEffect3()
                updatePic(effect3)
            }
        }
    }

}
