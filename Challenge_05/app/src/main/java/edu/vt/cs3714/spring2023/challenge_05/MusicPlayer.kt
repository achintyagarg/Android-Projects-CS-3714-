package edu.vt.cs3714.spring2023.challenge_05

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

class MusicPlayer(val musicService: MusicService): MediaPlayer.OnCompletionListener {

    val MUSICPATH = arrayOf("https://courses.cs.vt.edu/cs3714/2023Sp_Android/media/mario.mp3",
                            "https://courses.cs.vt.edu/cs3714/2023Sp_Android/media/tetris.mp3")

    val MUSICNAME = arrayOf("Super Mario", "Tetris")


    var player: MediaPlayer? = null
    var currentPosition = 0
    var musicIndex = 0
    private var musicStatus = 0//0: before starts 1: playing 2: paused

    fun getMusicStatus(): Int {
        return musicStatus
    }

    fun getMusicName(): String {
        return MUSICNAME[musicIndex]
    }


    fun playMusic() {
        Log.i("music index to play", musicIndex.toString())
        player = MediaPlayer()
        player!!.setLooping(false)
        player!!.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
        try {
            Log.i("music file to play", MUSICPATH[musicIndex])
            player!!.setDataSource(MUSICPATH[musicIndex])
            player!!.setLooping(false)
            player!!.prepare()
            player!!.setOnCompletionListener(this)
            player!!.start()

            musicService.onUpdateMusicName(getMusicName())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        musicStatus = 1
    }

    fun pauseMusic() {
        if (player != null && player!!.isPlaying()) {
            player!!.pause()
            currentPosition = player!!.getCurrentPosition()
            musicStatus = 2
        }
    }

    fun resumeMusic() {
        if (player != null) {
            player!!.seekTo(currentPosition)
            player!!.start()
            musicStatus = 1
        }
    }

    override fun onCompletion(player: MediaPlayer?) {
        Log.i("class musicPlayer:" , player.toString())
        musicIndex = (musicIndex + 1) % 2
        Log.i("musicPlayer complete now playing", musicIndex.toString())
        player!!.release()

        playMusic()
    }


}