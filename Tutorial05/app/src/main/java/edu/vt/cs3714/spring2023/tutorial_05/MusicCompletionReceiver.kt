package edu.vt.cs3714.spring2023.tutorial_05

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class MusicCompletionReceiver(val mainActivity: MainActivity? = null) : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

        val musicName = intent?.getStringExtra(MusicService.MUSICNAME)
        Log.i("receiving name", musicName!!)
        //update main activity
        mainActivity?.updateName(musicName)
    }


}