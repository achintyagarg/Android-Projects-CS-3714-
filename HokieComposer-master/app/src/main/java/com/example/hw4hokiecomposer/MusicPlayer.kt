package com.example.hw4hokiecomposer

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.content.res.AssetFileDescriptor
import java.io.IOException


class MusicPlayer(val musicService: MusicService): MediaPlayer.OnCompletionListener {

    private val musicPath = arrayOf(R.raw.gotechgo, R.raw.gtg2, R.raw.gtg3, R.raw.clapping, R.raw.cheering, R.raw.letsgohokies)
    private val musicName = arrayOf("Go Tech Go", "Go Tech Go 2", "Go Tech Go 3", "clapping", "cheering", "gohokies")

    var currentPosition = 0
    private var musicIndex = 0

    // Before start: 0
    // Playing: 1
    // Paused: 2
    private var musicStatus = 0
    private lateinit var player: MediaPlayer

    fun setSong(index: Int) {
        musicIndex = index
    }

    fun playMusic() {
        player = MediaPlayer()
        player.setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build())
        val assetFileDescriptor: AssetFileDescriptor = musicService.resources.openRawResourceFd(musicPath[musicIndex])
        try {
            player.setDataSource(assetFileDescriptor)
            player.prepare()
            player.setOnCompletionListener(this)
            player.start()
            musicService.onUpdateMusicName(getMusicName())
        } catch (ex: IOException) {
            ex.printStackTrace()
        }

        musicStatus = 1
    }

    fun pauseMusic() {
        if (player.isPlaying) {
            player.pause()
            currentPosition = player.currentPosition
            musicStatus = 2
        }
    }

    fun resumeMusic() {
        player.seekTo(currentPosition)
        player.start()
        musicStatus = 1
    }

    fun restartMusic() {
        player.release()
        playMusic()
    }

    fun getMusicName(): String {
        return musicName[musicIndex]
    }

    fun getMusicStatus(): Int {
        return musicStatus
    }

    override fun onCompletion(mp: MediaPlayer?) {
        player.reset()
    }
}