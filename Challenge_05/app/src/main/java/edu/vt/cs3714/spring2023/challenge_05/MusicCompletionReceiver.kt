package edu.vt.cs3714.spring2023.challenge_05

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MusicCompletionReceiver(val playFragment: PlayFragment? = null) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val musicName = intent?.getStringExtra(MusicService.MUSICNAME)
        Log.i("receiving name", musicName!!)
        playFragment?.updateName(musicName)
    }
}
