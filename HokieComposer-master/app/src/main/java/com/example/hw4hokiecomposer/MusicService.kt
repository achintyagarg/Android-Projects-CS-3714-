package com.example.hw4hokiecomposer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder

class MusicService: Service() {

    companion object {
        const val COMPLETE_INTENT = "complete intent"
        const val MUSIC_NAME = "music name"
    }

    var musicPlayer: MusicPlayer? = null
    var effect1Player: MusicPlayer? = null
    var effect2Player: MusicPlayer? = null
    var effect3Player: MusicPlayer? = null
    private val iBinder = MyBinder()

    override fun onBind(intent: Intent?): IBinder? {
        return iBinder
    }

    inner class MyBinder: Binder() {
        fun getService(): MusicService {
            return this@MusicService
        }
    }

    fun onUpdateMusicName(musicName: String) {
        val intent = Intent(COMPLETE_INTENT)
        intent.putExtra(MUSIC_NAME, musicName)
        sendBroadcast(intent)
    }

    override fun onCreate() {
        super.onCreate()
        musicPlayer = MusicPlayer(this)
        effect1Player = MusicPlayer(this)
        effect2Player = MusicPlayer(this)
        effect3Player = MusicPlayer(this)
    }

    fun setSongs(song: Int, e1: Int, e2: Int, e3: Int) {
        musicPlayer?.setSong(song)
        effect1Player?.setSong(e1)
        effect2Player?.setSong(e2)
        effect3Player?.setSong(e3)
    }

    fun getPlayingStatus(): Int {
        return musicPlayer?.getMusicStatus() ?: -1
    }

    fun startMusic() {
        musicPlayer?.playMusic()
    }

    fun startEffect1() {
        effect1Player?.playMusic()
    }

    fun startEffect2() {
        effect2Player?.playMusic()
    }

    fun startEffect3() {
        effect3Player?.playMusic()
    }

    fun pauseMusic() {
        musicPlayer?.pauseMusic()
        if (effect1Player?.getMusicStatus() == 1) {
            effect1Player?.pauseMusic()
        }
        if (effect2Player?.getMusicStatus() == 1) {
            effect2Player?.pauseMusic()
        }
        if (effect3Player?.getMusicStatus() == 1) {
            effect3Player?.pauseMusic()
        }
    }

    fun resumeMusic() {
        musicPlayer?.resumeMusic()
        if (effect1Player?.getMusicStatus() == 2) {
            effect1Player?.resumeMusic()
        }
        if (effect2Player?.getMusicStatus() == 2) {
            effect2Player?.resumeMusic()
        }
        if (effect3Player?.getMusicStatus() == 2) {
            effect3Player?.resumeMusic()
        }
    }

    fun restartMusic() {
        musicPlayer?.restartMusic()
    }
}