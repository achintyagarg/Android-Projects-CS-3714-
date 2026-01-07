package com.example.hw4hokiecomposer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MusicCompletionReceiver(val playFragment: PlayFragment?=null) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val musicName = intent.getStringExtra(MusicService.MUSIC_NAME)
        playFragment?.updatePic(musicName)
    }
}